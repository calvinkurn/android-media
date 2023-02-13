package com.tokopedia.dynamicfeatures

import android.app.Application
import android.content.Context
import android.os.Build
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.dynamicfeatures.DFInstallerActivity.Companion.DOWNLOAD_MODE_PAGE
import com.tokopedia.dynamicfeatures.config.DFRemoteConfig
import com.tokopedia.dynamicfeatures.constant.CommonConstant
import com.tokopedia.dynamicfeatures.service.DFDownloader
import com.tokopedia.dynamicfeatures.service.DFErrorCache
import com.tokopedia.dynamicfeatures.service.DFQueue
import com.tokopedia.dynamicfeatures.track.DFTracking
import com.tokopedia.dynamicfeatures.utils.DFInstallerLogUtil
import com.tokopedia.dynamicfeatures.utils.ErrorUtils
import com.tokopedia.dynamicfeatures.utils.StorageUtils
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import kotlin.coroutines.resume

/**
 * Dynamic Installer, object that handle installing dynamic feature in background for application
 * Also handle the unify logging
 */
object DFInstaller {

    internal var manager: SplitInstallManager? = null
    private const val TAG_DFM_DEFERRED = "DFM_DEFERRED"
    private const val DOWNLOAD_MODE_BACKGROUND = "Background"
    private const val TAG_LOG_DFM_DEFERRED_INSTALL = "Install"
    private const val TAG_LOG_DFM_DEFERRED_UNINSTALL = "Uninstall"

    var sessionId: Int? = null
    internal var previousState: SplitInstallSessionState? = null
    internal var moduleSize = 0L
    internal var freeInternalSpaceBeforeDownload: Long = 0L
    internal var startDownloadPercentage = -1f
    internal var deeplink = ""
    internal var fallbackUrl = ""

    private var viewRef: WeakReference<DFInstallerView?>? = null
    var startDownloadTimestamp: Long = 0L

    fun attachView(view: DFInstallerView) {
        viewRef = WeakReference(view)
    }

    fun clearRef() {
        viewRef = null
    }

    fun getView(): DFInstallerView? = viewRef?.get()

    interface DFInstallerView {
        fun onDownload(state: SplitInstallSessionState)
        fun onRequireUserConfirmation(state: SplitInstallSessionState)
        fun onInstalled()
        fun onInstalling(state: SplitInstallSessionState)
        fun onFailed(errorString: String)
        fun getModuleNameView(): String
        fun getDeeplink(): String
        fun getFallbackUrl(): String
    }

    @JvmStatic
    fun isInstalled(context: Context, moduleName: String): Boolean {
        val manager = getManager(context.applicationContext) ?: return false
        return manager.installedModules.contains(moduleName)
    }

    internal fun getManager(context: Context): SplitInstallManager? {
        if (manager == null) {
            manager = SplitInstallManagerFactory.create(context)
        }
        return manager
    }

    suspend fun startInstallInBackground(
        context: Context,
        moduleName: String,
        onSuccessInstall: (() -> Unit)? = null,
        onFailedInstall: (() -> Unit)? = null
    ): Pair<Boolean, Boolean> {
        return withContext(Dispatchers.IO) {
            if (getView() == null) { // no foreground activity
                // check max retry
                val maxRetry = DFRemoteConfig.getConfig(context).downloadInBackgroundMaxRetry
                val errorCount = DFErrorCache.getErrorCounter(context, moduleName)
                if (errorCount >= maxRetry) {
                    return@withContext (true to true)
                }
            }

            val applicationContext = context.applicationContext
            resetDFInfo()

            val moduleNameToDownload = getFilteredModuleList(context, listOf(moduleName))
            if (moduleNameToDownload.isEmpty()) return@withContext (true to false)

            val requestBuilder = SplitInstallRequest.newBuilder()
            moduleNameToDownload.forEach { name ->
                requestBuilder.addModule(name)
            }
            val request = requestBuilder.build()
            freeInternalSpaceBeforeDownload = StorageUtils.getFreeSpaceBytes(applicationContext)

            // SplitInstallManager only allow the installation from Main Thread.
            withContext(Dispatchers.Main) {
                suspendCancellableCoroutine<Pair<Boolean, Boolean>> { continuation ->
                    registerListener(applicationContext, moduleNameToDownload, onSuccessInstall, onFailedInstall, continuation)
                    // if has view will continue from the last state download
                    getView()?.let { view ->
                        if (view.getModuleNameView() != SplitInstallListener.moduleNameToDownload.first()) {
                            return@let
                        }
                        previousState?.let { state ->
                            if (state.status() == SplitInstallSessionStatus.DOWNLOADING) {
                                view.onDownload(state)
                            } else if (state.status() == SplitInstallSessionStatus.INSTALLING) {
                                view.onInstalling(state)
                            }
                        }
                    }
                    startDownloadTimestamp = System.currentTimeMillis()
                    getManager(applicationContext)?.startInstall(request)?.addOnSuccessListener {
                        if (it == 0) {
                            // success
                            onSuccessInstall(context, moduleName, onSuccessInstall, continuation)
                        } else {
                            sessionId = it
                        }
                    }?.addOnFailureListener {
                        val errorCode = (it as? SplitInstallException)?.errorCode
                        val errorString = errorCode?.toString() ?: it.toString()
                        onErrorInstall(context, errorString, moduleName, onFailedInstall, continuation)
                        if (getView() == null) {
                            DFErrorCache.addErrorCounter(context, moduleName)
                        }
                    }
                }
            }
        }
    }

    fun onSuccessInstall(
        context: Context,
        moduleName: String,
        onSuccessInstall: (() -> Unit)? = null,
        continuation: CancellableContinuation<Pair<Boolean, Boolean>>? = null
    ) {
        val viewRef = viewRef
        val view = viewRef?.get()
        var tag: String
        if (view != null && view.getModuleNameView() == moduleName) {
            view.onInstalled()
            tag = DOWNLOAD_MODE_PAGE
            deeplink = view.getDeeplink()
            fallbackUrl = view.getFallbackUrl()
        } else {
            tag = DOWNLOAD_MODE_BACKGROUND
        }
        logSuccessStatus(tag, context, listOf(moduleName))
        onSuccessInstall?.invoke()
        handleContinuationOnSuccess(continuation)
    }

    fun onErrorInstall(
        context: Context,
        errorString: String,
        moduleName: String,
        onFailedInstall: (() -> Unit)? = null,
        continuation: CancellableContinuation<Pair<Boolean, Boolean>>? = null
    ) {
        sessionId = null
        val viewRef = viewRef
        val view = viewRef?.get()
        val tag: String
        if (view != null && view.getModuleNameView() == moduleName) {
            view.onFailed(errorString)
            // to stop download other DFs in queue
            DFQueue.clear(context)
            tag = DOWNLOAD_MODE_PAGE
            deeplink = view.getDeeplink()
            fallbackUrl = view.getFallbackUrl()
        } else {
            tag = DOWNLOAD_MODE_BACKGROUND
        }
        logFailedStatus(tag, context.applicationContext, listOf(moduleName), listOf(errorString))
        onFailedInstall?.invoke()
        handleContinuationOnError(view, moduleName, continuation)
    }

    fun handleContinuationOnError(
        view: DFInstallerView?,
        moduleName: String,
        continuation: CancellableContinuation<Pair<Boolean, Boolean>>? = null
    ) {
        try {
            if (continuation?.isActive == true) {
                if (view != null && view.getModuleNameView() != moduleName) {
                    continuation.resume(false to true)
                } else {
                    continuation.resume(false to false)
                }
            }
        } catch (e: Exception) {
            // no-op
        }
    }

    fun handleContinuationOnSuccess(continuation: CancellableContinuation<Pair<Boolean, Boolean>>? = null) {
        try {
            if (continuation?.isActive == true) {
                continuation.resume(true to true)
            }
        } catch (e: Exception) {
            // no-op
        }
    }

    @JvmStatic
    fun installOnBackground(context: Context, moduleName: String, message: String) {
        if (isInstalled(context, moduleName)) {
            return
        }
        val moduleNameList = ArrayList<String>()
        moduleNameList.add(moduleName)
        installOnBackground(context, moduleNameList, message)
    }

    @JvmStatic
    fun stopInstall(context: Context) {
        DFDownloader.stopService(context)
        sessionId?.let {
            getManager(context)?.cancelInstall(it)
        }
        sessionId = null
        clearRef()
    }

    /**
     * Non suspended function to trigger the schedule of the service.
     * The service will run suspend function of install on background.
     */
    @JvmStatic
    fun installOnBackground(context: Context, moduleNameList: List<String>, message: String) {
        val filteredModuleNameList = mutableListOf<String>()

        for (moduleName in moduleNameList) {
            if (!isInstalled(context, moduleName)) {
                filteredModuleNameList.add(moduleName)
            }
        }
        val dfConfig = DFRemoteConfig.getConfig(context.applicationContext)
        if (dfConfig.downloadInBackground && !dfConfig.downloadInBackgroundExcludedSdkVersion.contains(Build.VERSION.SDK_INT)) {
            // this is to filter which module that download in background based on remote config
            val eligibleInBgModuleNameList: List<String>
            if (dfConfig.moduleRestrictInBackground?.isNotEmpty() == true) {
                eligibleInBgModuleNameList = filteredModuleNameList.filter { it !in dfConfig.moduleRestrictInBackground }
            } else {
                eligibleInBgModuleNameList = filteredModuleNameList
            }
            // start downloading the modules using service
            if (eligibleInBgModuleNameList.isNotEmpty()) {
                DFDownloader.startSchedule(context.applicationContext, eligibleInBgModuleNameList, true, false)
            }
        } else {
            startDeferredInstall(context, filteredModuleNameList, message)
        }
    }

    private fun startDeferredInstall(context: Context, moduleNameToDownload: List<String>, message: String) {
        val filteredModuleNameToDownload = getFilteredModuleList(context, moduleNameToDownload)
        if (filteredModuleNameToDownload.isEmpty()) {
            return
        }
        val messageLog = "$TAG_LOG_DFM_DEFERRED_INSTALL {$message}"
        getManager(context.applicationContext)?.deferredInstall(filteredModuleNameToDownload)?.addOnSuccessListener {
            logDeferredStatus(context.applicationContext, messageLog, filteredModuleNameToDownload)
        }?.addOnFailureListener {
            val errorCode = (it as? SplitInstallException)?.errorCode
            logDeferredStatus(
                context.applicationContext, messageLog, filteredModuleNameToDownload,
                listOf(
                    errorCode?.toString()
                        ?: it.toString()
                )
            )
        }
    }

    private fun logDeferredStatus(context: Context, message: String, moduleNames: List<String>, errorCode: List<String> = emptyList()) {
        val errorCodeTemp = errorCode.map { ErrorUtils.getValidatedErrorCode(context, it, freeInternalSpaceBeforeDownload) }
        DFInstallerLogUtil.logStatus(
            context, TAG_DFM_DEFERRED, message, moduleNames.joinToString(),
            freeInternalSpaceBeforeDownload, moduleSize, errorCodeTemp, 1, false
        )
    }

    /**
     * installed = true
     * will return the moduleList that need to install
     */
    fun getFilteredModuleList(context: Context, moduleNames: List<String>, installed: Boolean = true): List<String> {
        val moduleNameToDownload = mutableListOf<String>()
        getManager(context.applicationContext) ?: return moduleNameToDownload
        moduleNames.forEach { name ->
            if (installed && !isInstalled(context, name)) {
                moduleNameToDownload.add(name)
            }
            if (!installed && isInstalled(context, name)) {
                moduleNameToDownload.add(name)
            }
        }
        return moduleNameToDownload
    }

    fun uninstallOnBackground(application: Application, moduleNames: List<String>) {
        val applicationContext = application.applicationContext

        val moduleNameToUninstall = getFilteredModuleList(applicationContext, moduleNames, false)
        if (moduleNameToUninstall.isEmpty()) return

        val manager = getManager(applicationContext) ?: return
        manager.deferredUninstall(moduleNameToUninstall).addOnSuccessListener {
            logDeferredStatus(applicationContext, TAG_LOG_DFM_DEFERRED_UNINSTALL, moduleNameToUninstall)
        }.addOnFailureListener {
            val errorCode = (it as? SplitInstallException)?.errorCode
            logDeferredStatus(
                applicationContext,
                TAG_LOG_DFM_DEFERRED_UNINSTALL,
                moduleNameToUninstall,
                listOf(errorCode?.toString() ?: it.toString())
            )
        }
    }

    private fun logSuccessStatus(tag: String, context: Context, moduleNameToDownload: List<String>) {
        DFTracking.trackDownloadDF(moduleNameToDownload, null, tag == DOWNLOAD_MODE_BACKGROUND)
        DFInstallerLogUtil.logStatus(
            context, CommonConstant.DFM_TAG, tag, moduleNameToDownload.joinToString(),
            freeInternalSpaceBeforeDownload, moduleSize, emptyList(), 1, true,
            startDownloadTimestamp, System.currentTimeMillis(), startDownloadPercentage,
            true, deeplink, fallbackUrl
        )
    }

    private fun logFailedStatus(
        tag: String,
        context: Context,
        moduleNameToDownload: List<String>,
        errorCode: List<String> = emptyList()
    ) {
        val errorCodeTemp = errorCode.map { ErrorUtils.getValidatedErrorCode(context, it, freeInternalSpaceBeforeDownload) }
        DFTracking.trackDownloadDF(moduleNameToDownload, errorCodeTemp, tag == DOWNLOAD_MODE_BACKGROUND)
        DFInstallerLogUtil.logStatus(
            context, CommonConstant.DFM_TAG, tag, moduleNameToDownload.joinToString(),
            freeInternalSpaceBeforeDownload, moduleSize, errorCodeTemp, 1, false,
            startDownloadTimestamp, System.currentTimeMillis(), startDownloadPercentage,
            true, deeplink, fallbackUrl
        )
    }

    private fun registerListener(
        context: Context,
        moduleNameToDownload: List<String>,
        onSuccessInstall: (() -> Unit)? = null,
        onFailedInstall: (() -> Unit)? = null,
        continuation: CancellableContinuation<Pair<Boolean, Boolean>>
    ) {
        SplitInstallListener.instance.context = context
        SplitInstallListener.instance.moduleNameToDownload = moduleNameToDownload
        SplitInstallListener.instance.onSuccessInstall = onSuccessInstall
        SplitInstallListener.instance.onFailedInstall = onFailedInstall
        SplitInstallListener.instance.continuation = continuation
        manager?.registerListener(SplitInstallListener.instance)
    }

    private fun resetDFInfo() {
        moduleSize = 0
        freeInternalSpaceBeforeDownload = 0L
        startDownloadPercentage = -1f
        deeplink = ""
        fallbackUrl = ""
    }
}

object SplitInstallListener : SplitInstallStateUpdatedListener {
    var onSuccessInstall: (() -> Unit)? = null
    var onFailedInstall: (() -> Unit)? = null
    var instance: SplitInstallListener = this
    var context: Context? = null
    var moduleNameToDownload: List<String> = emptyList()
    var continuation: CancellableContinuation<Pair<Boolean, Boolean>>? = null
    override fun onStateUpdate(state: SplitInstallSessionState) {
        if (state.sessionId() != DFInstaller.sessionId) {
            return
        }
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                DFInstaller.previousState = state
                DFInstaller.getView()?.onDownload(state)
                if (DFInstaller.moduleSize == 0L) {
                    DFInstaller.moduleSize = state.totalBytesToDownload()
                }
                if (DFInstaller.startDownloadPercentage < 0) {
                    val totalBytesToDowload = state.totalBytesToDownload().toInt()
                    val bytesDownloaded = state.bytesDownloaded().toInt()
                    DFInstaller.startDownloadPercentage = bytesDownloaded.toFloat() * 100 / totalBytesToDowload
                }
            }
            SplitInstallSessionStatus.INSTALLED -> {
                DFInstaller.previousState = null
                context?.let { context ->
                    DFInstaller.onSuccessInstall(
                        context,
                        moduleNameToDownload.first(),
                        onSuccessInstall,
                        continuation
                    )
                }
            }
            SplitInstallSessionStatus.FAILED -> {
                DFInstaller.previousState = null
                context?.let { context ->
                    DFInstaller.onErrorInstall(
                        context,
                        state.errorCode().toString(),
                        moduleNameToDownload.first(),
                        onFailedInstall,
                        continuation
                    )
                }
            }
            SplitInstallSessionStatus.INSTALLING -> {
                DFInstaller.previousState = state
                val view = DFInstaller.getView()
                if (view != null && view.getModuleNameView() == moduleNameToDownload.first()) {
                    view.onInstalling(state)
                }
            }
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                DFInstaller.previousState = null
                DFInstaller.getView()?.onRequireUserConfirmation(state)
            }
        }
    }
}
