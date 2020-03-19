package com.tokopedia.dynamicfeatures

import android.app.Application
import android.content.Context
import android.os.Build
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.dynamicfeatures.DFInstaller.logSuccessStatus
import com.tokopedia.dynamicfeatures.config.DFRemoteConfig
import com.tokopedia.dynamicfeatures.service.DFDownloader
import com.tokopedia.dynamicfeatures.track.DFTracking
import com.tokopedia.dynamicfeatures.utils.DFInstallerLogUtil
import com.tokopedia.dynamicfeatures.utils.ErrorUtils
import com.tokopedia.dynamicfeatures.utils.StorageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Dynamic Installer, object that handle installing dynamic feature in background for application
 * Also handle the unify logging
 */
object DFInstaller {

    internal var manager: SplitInstallManager? = null
    private const val TAG_DFM_DEFERRED = "DFM_DEFERRED"
    const val TAG_LOG_DFM_BG = "Background"
    private const val TAG_LOG_DFM_DEFERRED_INSTALL = "Install"
    private const val TAG_LOG_DFM_DEFERRED_UNINSTALL = "Uninstall"

    var sessionId: Int? = null
    internal var moduleSize = 0L
    internal var freeInternalSpaceBeforeDownload: Long = 0L

    private var viewRef: WeakReference<DFInstallerView?>? = null

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
    }

    @JvmStatic
    fun isInstalled(application: Application, moduleName: String): Boolean {
        val manager = getManager(application) ?: return false
        return manager.installedModules.contains(moduleName)
    }

    internal fun getManager(context: Context): SplitInstallManager? {
        if (manager == null) {
            manager = SplitInstallManagerFactory.create(context)
        }
        return manager
    }

    suspend fun startInstallInBackground(context: Context,
                                         moduleNames: List<String>,
                                         onSuccessInstall: (() -> Unit)? = null,
                                         onFailedInstall: (() -> Unit)? = null): Boolean {
        return withContext(Dispatchers.IO) {
            val applicationContext = context.applicationContext
            moduleSize = 0

            val moduleNameToDownload = getFilteredModuleList(context, moduleNames)
            if (moduleNameToDownload.isEmpty()) return@withContext true

            val requestBuilder = SplitInstallRequest.newBuilder()
            moduleNameToDownload.forEach { name ->
                requestBuilder.addModule(name)
            }
            val request = requestBuilder.build()
            freeInternalSpaceBeforeDownload = StorageUtils.getFreeSpaceBytes(applicationContext)

            // SplitInstallManager only allow the installation from Main Thread.
            withContext(Dispatchers.Main) {
                suspendCoroutine<Boolean> { continuation ->
                    registerListener(context, moduleNameToDownload, onSuccessInstall, onFailedInstall, continuation)
                    getManager(applicationContext)?.startInstall(request)?.addOnSuccessListener {
                        if (it == 0) {
                            // success
                            val viewRef = viewRef
                            val view = viewRef?.get()
                            if (view != null) {
                                view.onInstalled()
                            } else {
                                logSuccessStatus(TAG_LOG_DFM_BG, applicationContext, moduleNameToDownload)
                            }
                            onSuccessInstall?.invoke()
                            continuation.resume(true)
                        } else {
                            sessionId = it
                        }
                    }?.addOnFailureListener {
                        val errorCode = (it as? SplitInstallException)?.errorCode
                        sessionId = null
                        val viewRef = viewRef
                        val view = viewRef?.get()
                        if (view != null) {
                            view.onFailed(errorCode?.toString() ?: it.toString())
                        } else {
                            logFailedStatus(TAG_LOG_DFM_BG, applicationContext, moduleNameToDownload,
                                listOf(errorCode?.toString() ?: it.toString()))
                        }
                        onFailedInstall?.invoke()
                        continuation.resume(false)
                    }
                }
            }
        }
    }

    /**
     * Non suspended function to trigger the schedule of the service.
     * The service will run suspend function of install on background.
     */
    @JvmStatic
    fun installOnBackground(context: Context, moduleNames: List<String>, message: String) {
        val dfConfig = DFRemoteConfig.getConfig(context.applicationContext)
        if (dfConfig.downloadInBackground && !dfConfig.downloadInBackgroundExcludedSdkVersion.contains(Build.VERSION.SDK_INT)) {
            DFDownloader.startSchedule(context.applicationContext, moduleNames, true)
        } else {
            startDeferredInstall(context, moduleNames, message)
        }
    }

    private fun startDeferredInstall(context: Context, moduleNameToDownload: List<String>, message: String) {
        val filteredModuleNameToDownload = getFilteredModuleList(context, moduleNameToDownload)
        val messageLog = "$TAG_LOG_DFM_DEFERRED_INSTALL {$message}"
        getManager(context.applicationContext)?.deferredInstall(filteredModuleNameToDownload)?.addOnSuccessListener {
            logDeferredStatus(context.applicationContext, messageLog, filteredModuleNameToDownload)
        }?.addOnFailureListener {
            val errorCode = (it as? SplitInstallException)?.errorCode
            logDeferredStatus(context.applicationContext, messageLog, filteredModuleNameToDownload, listOf(errorCode?.toString()
                ?: it.toString()))
        }
    }

    private fun logDeferredStatus(context: Context, message: String, moduleNames: List<String>, errorCode: List<String> = emptyList()) {
        val errorCodeTemp = ErrorUtils.getValidatedErrorCode(context, errorCode, freeInternalSpaceBeforeDownload)
        DFInstallerLogUtil.logStatus(context, message, moduleNames.joinToString(),
            freeInternalSpaceBeforeDownload, moduleSize, errorCodeTemp, 0, false, TAG_DFM_DEFERRED)
    }

    /**
     * installed = true
     * will return the moduleList that need to install
     */
    fun getFilteredModuleList(context: Context, moduleNames: List<String>, installed: Boolean = true): List<String> {
        val moduleNameToDownload = mutableListOf<String>()
        val manager = getManager(context.applicationContext) ?: return moduleNameToDownload
        moduleNames.forEach { name ->
            if (installed && !manager.installedModules.contains(name)) {
                moduleNameToDownload.add(name)
            }
            if (!installed && manager.installedModules.contains(name)) {
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
            logDeferredStatus(applicationContext, TAG_LOG_DFM_DEFERRED_UNINSTALL, moduleNameToUninstall,
                listOf(errorCode?.toString() ?: it.toString()))
        }
    }

    fun logSuccessStatus(tag: String, context: Context, moduleNameToDownload: List<String>) {
        DFTracking.trackDownloadDF(moduleNameToDownload, null, tag == TAG_LOG_DFM_BG)
        DFInstallerLogUtil.logStatus(context, tag, moduleNameToDownload.joinToString(),
            freeInternalSpaceBeforeDownload, moduleSize, emptyList(), 1, true)
    }

    fun logFailedStatus(tag: String, context: Context, moduleNameToDownload: List<String>,
                        errorCode: List<String> = emptyList()) {
        val errorCodeTemp = ErrorUtils.getValidatedErrorCode(context, errorCode, freeInternalSpaceBeforeDownload)
        DFTracking.trackDownloadDF(moduleNameToDownload, errorCodeTemp, tag == TAG_LOG_DFM_BG)
        DFInstallerLogUtil.logStatus(context, tag, moduleNameToDownload.joinToString(),
            freeInternalSpaceBeforeDownload, moduleSize, errorCodeTemp, 0, false)
    }

    private fun registerListener(context: Context, moduleNameToDownload: List<String>,
                                 onSuccessInstall: (() -> Unit)? = null,
                                 onFailedInstall: (() -> Unit)? = null,
                                 continuation: Continuation<Boolean>) {
        SplitInstallListener.instance.context = context
        SplitInstallListener.instance.moduleNameToDownload = moduleNameToDownload
        SplitInstallListener.instance.onSuccessInstall = onSuccessInstall
        SplitInstallListener.instance.onFailedInstall = onFailedInstall
        SplitInstallListener.instance.continuation = continuation
        manager?.registerListener(SplitInstallListener.instance)
    }
}

object SplitInstallListener : SplitInstallStateUpdatedListener {
    var onSuccessInstall: (() -> Unit)? = null
    var onFailedInstall: (() -> Unit)? = null
    var instance: SplitInstallListener = this
    var context: Context? = null
    var moduleNameToDownload: List<String> = emptyList()
    var continuation: Continuation<Boolean>? = null
    override fun onStateUpdate(state: SplitInstallSessionState) {
        if (state.sessionId() != DFInstaller.sessionId) {
            return
        }
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                val view = DFInstaller.getView()
                if (view != null) {
                    view.onDownload(state)
                } else {
                    if (DFInstaller.moduleSize == 0L) {
                        DFInstaller.moduleSize = state.totalBytesToDownload()
                    }
                }
            }
            SplitInstallSessionStatus.INSTALLED -> {
                val view = DFInstaller.getView()
                if (view != null) {
                    view.onInstalled()
                } else {
                    context?.let { context ->
                        logSuccessStatus(DFInstaller.TAG_LOG_DFM_BG, context, moduleNameToDownload)
                    }
                }
                onSuccessInstall?.invoke()
                continuation?.resume(true)
            }
            SplitInstallSessionStatus.FAILED -> {
                val view = DFInstaller.getView()
                if (view != null) {
                    view.onFailed(state.errorCode().toString())
                } else {
                    context?.let { context ->
                        logSuccessStatus(DFInstaller.TAG_LOG_DFM_BG, context, moduleNameToDownload)
                    }
                }
                onSuccessInstall?.invoke()
                continuation?.resume(true)
            }
            SplitInstallSessionStatus.INSTALLING -> {
                DFInstaller.getView()?.onInstalling(state)
            }
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                DFInstaller.getView()?.onRequireUserConfirmation(state)
            }
        }
    }
}