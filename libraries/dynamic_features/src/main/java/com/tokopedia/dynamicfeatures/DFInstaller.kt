package com.tokopedia.dynamicfeatures

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitinstall.*
import com.tokopedia.dynamicfeatures.track.DFTracking
import kotlinx.coroutines.*
import java.lang.Exception
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
        private const val TAG_LOG_DFM_DEFERRED_INSTALL = "Install"
        private const val TAG_LOG_DFM_DEFERRED_UNINSTALL = "Uninstall"
        @JvmStatic
        fun isInstalled(application: Application, moduleName: String): Boolean {
            initManager(application)
            val manager = manager ?: return false
            return manager.installedModules.contains(moduleName)
        }

        internal fun initManager(context: Context) {
            if (manager == null) {
                manager = SplitInstallManagerFactory.create(context)
            }
        }
    }

    internal var moduleSize = 0L
    internal var usableSpaceBeforeDownload: Long = 0L

    suspend fun installOnBackgroundDefer(context: Context,
                                         moduleNames: List<String>,
                                         onSuccessInstall: (() -> Unit)? = null,
                                         onFailedInstall: (() -> Unit)? = null,
                                         isInitial: Boolean = true,
                                         message: String = ""): Boolean {
        return withContext(Dispatchers.IO) {
            val applicationContext = context.applicationContext
            if (moduleNames.isEmpty()) {
                return@withContext true
            }
            initManager(applicationContext)
            val manager = manager ?: return@withContext false

            moduleSize = 0

            val requestBuilder = SplitInstallRequest.newBuilder()

            val moduleNameToDownload = mutableListOf<String>()
            moduleNames.forEach { name ->
                if (!manager.installedModules.contains(name)) {
                    requestBuilder.addModule(name)
                    moduleNameToDownload.add(name)
                }
            }

            if (moduleNameToDownload.isEmpty()) return@withContext true

            usableSpaceBeforeDownload = DFInstallerLogUtil.getFreeSpaceBytes(applicationContext)

            // SplitInstallManager only allow the installation from Main Thread.
            withContext(Dispatchers.Main) { suspendCoroutine<Boolean> { continuation ->
                manager.deferredInstall(moduleNameToDownload).addOnSuccessListener {
                    logSuccessStatus("$TAG_LOG_DFM_DEFERRED_INSTALL {$message}", applicationContext, moduleNameToDownload)
                    onSuccessInstall?.invoke()
                    continuation.resume(true)
                }.addOnFailureListener {
                    val errorCode = (it as? SplitInstallException)?.errorCode
                    sessionId = null
                    logFailedStatus("$TAG_LOG_DFM_DEFERRED_INSTALL {$message}", applicationContext, moduleNameToDownload, errorCode?.toString()
                        ?: it.toString())
                    onFailedInstall?.invoke()
                    continuation.resume(false)
                }
            } }
        }
    }

    fun installOnBackground(context: Context, moduleNames: List<String>,
                            onSuccessInstall: (() -> Unit)? = null,
                            onFailedInstall: (() -> Unit)? = null,
                            message: String) {
        GlobalScope.launch {
            try {
                installOnBackgroundDefer(context, moduleNames, onSuccessInstall, onFailedInstall, message = message)
            } catch (ignored: Exception) {

            }
        }
    }

    fun uninstallOnBackground(application: Application, moduleNames: List<String>) {
        val applicationContext = application.applicationContext
        if (moduleNames.isEmpty()) {
            return
        }
        initManager(applicationContext)
        val manager = manager ?: return

        val moduleNameToUninstall = mutableListOf<String>()
        moduleNames.forEach { name ->
            if (manager.installedModules.contains(name)) {
                moduleNameToUninstall.add(name)
            }
        }

        if (moduleNameToUninstall.isEmpty()) return

        manager.deferredUninstall(moduleNameToUninstall).addOnSuccessListener {
            logDeferredStatus(applicationContext, TAG_LOG_DFM_DEFERRED_UNINSTALL, moduleNameToUninstall)
        }.addOnFailureListener {
            val errorCode = (it as? SplitInstallException)?.errorCode
            logDeferredStatus(applicationContext, TAG_LOG_DFM_DEFERRED_UNINSTALL, moduleNameToUninstall,
                errorCode?.toString() ?: it.toString())
        }
    }

    private fun logSuccessStatus(tag: String, context: Context, moduleNameToDownload: List<String>) {
        if (tag == TAG_LOG_DFM_DEFERRED_INSTALL || tag == DFInstallerActivity.TAG_LOG) {
            DFTracking.trackDownloadDF(moduleNameToDownload,
                null,
                tag == TAG_LOG_DFM_DEFERRED_INSTALL)
        }
        DFInstallerLogUtil.logStatus(context, tag,
            moduleNameToDownload.joinToString(), usableSpaceBeforeDownload, moduleSize, null, 0, false)
    }

    private fun logFailedStatus(tag: String, applicationContext: Context, moduleNameToDownload: List<String>,
                                 errorCode: String = "") {
        if (tag == TAG_LOG_DFM_DEFERRED_INSTALL || tag == DFInstallerActivity.TAG_LOG) {
            DFTracking.trackDownloadDF(moduleNameToDownload,
                listOf(errorCode),
                tag == TAG_LOG_DFM_DEFERRED_INSTALL)
        }
        DFInstallerLogUtil.logStatus(applicationContext,
            tag, moduleNameToDownload.joinToString(), usableSpaceBeforeDownload, moduleSize,
            listOf(errorCode), 0, false)
    }

    private fun logDeferredStatus(applicationContext: Context, message: String, moduleNameToDownload: List<String>, errorCode: String = ""){
        DFInstallerLogUtil.logStatus(applicationContext,
                message, moduleNameToDownload.joinToString(), usableSpaceBeforeDownload, moduleSize,
                listOf(errorCode), 0, false, TAG_DFM_DEFERRED)
    }
}