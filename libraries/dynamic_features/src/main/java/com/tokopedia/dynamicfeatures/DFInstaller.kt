package com.tokopedia.dynamicfeatures

import android.app.Application
import android.content.Context
import android.os.Build
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.dynamicfeatures.config.DFRemoteConfig
import com.tokopedia.dynamicfeatures.service.DFDownloader
import com.tokopedia.dynamicfeatures.track.DFTracking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Dynamic Installer, object that handle installing dynamic feature in background for application
 * Also handle the unify logging
 */
class DFInstaller {
    internal var sessionId: Int? = null

    companion object {
        internal var manager: SplitInstallManager? = null
        private const val TAG_DFM_DEFERRED = "DFM_DEFERRED"
        const val TAG_LOG_DFM_BG = "Background"
        private const val TAG_LOG_DFM_DEFERRED_INSTALL = "Install"
        private const val TAG_LOG_DFM_DEFERRED_UNINSTALL = "Uninstall"
        @JvmStatic
        fun isInstalled(application: Application, moduleName: String): Boolean {
            val manager = getManager(application) ?: return false
            return manager.installedModules.contains(moduleName)
        }

        internal fun getManager(context: Context) : SplitInstallManager? {
            if (manager == null) {
                manager = SplitInstallManagerFactory.create(context)
            }
            return manager
        }
    }

    private var listener: SplitInstallListener? = null
    internal var moduleSize = 0L
    internal var usableSpaceBeforeDownload: Long = 0L

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
            usableSpaceBeforeDownload = DFInstallerLogUtil.getFreeSpaceBytes(applicationContext)

            // SplitInstallManager only allow the installation from Main Thread.
            withContext(Dispatchers.Main) { suspendCoroutine<Boolean> { continuation ->
                    registerListener(context, moduleNameToDownload, onSuccessInstall, onFailedInstall, continuation)
                    getManager(applicationContext)?.startInstall(request)?.addOnSuccessListener {
                        if (it == 0) {
                            // success
                            logSuccessStatus(TAG_LOG_DFM_BG, applicationContext, moduleNameToDownload)
                            onSuccessInstall?.invoke()
                            unregisterListener()
                            continuation.resume(true)
                        } else {
                            sessionId = it
                        }
                    }?.addOnFailureListener {
                        val errorCode = (it as? SplitInstallException)?.errorCode
                        sessionId = null
                        logFailedStatus(TAG_LOG_DFM_BG, applicationContext, moduleNameToDownload,
                                listOf(errorCode?.toString() ?: it.toString()))
                        onFailedInstall?.invoke()
                        unregisterListener()
                        continuation.resume(false)
                    }
            } }
        }
    }

    fun installOnBackground(context: Context, moduleNames: List<String>,
                            onSuccessInstall: (() -> Unit)? = null,
                            onFailedInstall: (() -> Unit)? = null,
                            message: String) {
        val moduleNameToDownload = getFilteredModuleList(context, moduleNames)
        val dfConfig = DFRemoteConfig().getConfig(context.applicationContext)
        if (dfConfig.dowloadInBackground && !dfConfig.downloadInBackgroundExcludedSdkVersion.contains(Build.VERSION.SDK_INT)) {
            DFDownloader.startSchedule(context.applicationContext, moduleNameToDownload, true)
        } else {
            startDeferredInstall(context, moduleNameToDownload, message)
        }
    }

    private fun startDeferredInstall(context: Context, moduleNameToDownload: List<String>, message: String) {
        val messageLog = "$TAG_LOG_DFM_DEFERRED_INSTALL {$message}"
        getManager(context.applicationContext)?.deferredInstall(moduleNameToDownload)?.addOnSuccessListener {
            logDeferredStatus(context.applicationContext, messageLog, moduleNameToDownload)
        }?.addOnFailureListener {
            val errorCode = (it as? SplitInstallException)?.errorCode
            logDeferredStatus(context.applicationContext, messageLog, moduleNameToDownload, listOf(errorCode?.toString() ?: it.toString()))
        }
    }

    private fun getFilteredModuleList(context: Context, moduleNames: List<String>, installed: Boolean = true): List<String> {
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

    internal fun logSuccessStatus(tag: String, context: Context, moduleNameToDownload: List<String>) {
        DFTracking.trackDownloadDF(moduleNameToDownload, null, tag == TAG_LOG_DFM_BG)
        DFInstallerLogUtil.logStatus(context, tag, moduleNameToDownload.joinToString(),
                usableSpaceBeforeDownload, moduleSize, emptyList(), 1, true)
    }

    internal fun logFailedStatus(tag: String, applicationContext: Context, moduleNameToDownload: List<String>,
                                 errorCode: List<String> = emptyList()) {
        DFTracking.trackDownloadDF(moduleNameToDownload, errorCode, tag == TAG_LOG_DFM_BG)
        DFInstallerLogUtil.logStatus(applicationContext, tag, moduleNameToDownload.joinToString(),
                usableSpaceBeforeDownload, moduleSize, errorCode, 0, false)
    }

    private fun logDeferredStatus(applicationContext: Context, message: String, moduleNameToDownload: List<String>, errorCode: List<String> = emptyList()){
        DFInstallerLogUtil.logStatus(applicationContext, message, moduleNameToDownload.joinToString(),
                usableSpaceBeforeDownload, moduleSize, errorCode, 0, false, TAG_DFM_DEFERRED)
    }

    internal fun unregisterListener() {
        if (listener != null) {
            manager?.unregisterListener(listener)
            moduleSize = 0
            listener = null
        }
    }

    private fun registerListener(context: Context, moduleNameToDownload: List<String>,
                                 onSuccessInstall: (() -> Unit)? = null,
                                 onFailedInstall: (() -> Unit)? = null,
                                 continuation: Continuation<Boolean>) {
        listener = SplitInstallListener(this, context, moduleNameToDownload, onSuccessInstall, onFailedInstall, continuation)
        manager?.registerListener(listener)
    }
}

private class SplitInstallListener(val dfInstaller: DFInstaller,
                                   val context: Context,
                                   val moduleNameToDownload: List<String>,
                                   val onSuccessInstall: (() -> Unit)? = null,
                                   val onFailedInstall: (() -> Unit)? = null,
                                   val continuation: Continuation<Boolean>) : SplitInstallStateUpdatedListener {
    override fun onStateUpdate(it: SplitInstallSessionState) {
        if (it.sessionId() != dfInstaller.sessionId) {
            return
        }

        when (it.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                if (dfInstaller.moduleSize == 0L) {
                    dfInstaller.moduleSize = it.totalBytesToDownload()
                }
            }
            SplitInstallSessionStatus.INSTALLED -> {
                dfInstaller.logSuccessStatus(DFInstaller.TAG_LOG_DFM_BG, context, moduleNameToDownload)
                onSuccessInstall?.invoke()
                dfInstaller.unregisterListener()
                continuation.resume(true)
            }
            SplitInstallSessionStatus.FAILED -> {
                DFTracking.trackDownloadDF(moduleNameToDownload, listOf(it.errorCode().toString()), true)
                dfInstaller.logFailedStatus(DFInstaller.TAG_LOG_DFM_BG, context, moduleNameToDownload, listOf(it.errorCode().toString()))
                onFailedInstall?.invoke()
                dfInstaller.unregisterListener()
                continuation.resume(false)
            }
        }
    }
}