package com.tokopedia.dynamicfeatures

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.dynamicfeatures.service.DFDownloader
import com.tokopedia.dynamicfeatures.track.DFTracking
import kotlinx.coroutines.*
import java.lang.Exception
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
        const val TAG_LOG_DFM_BG = "Deferred Install"
        const val TAG_LOG_DFM_BG_UNINSTALL = "Deferred Uninstall"
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
                                         additionalTag: String = ""): Boolean {
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
                    logSuccessStatus("$TAG_LOG_DFM_BG {$additionalTag}", applicationContext, moduleNameToDownload)
                    onSuccessInstall?.invoke()
                    continuation.resume(true)
                }.addOnFailureListener {
                    val errorCode = (it as? SplitInstallException)?.errorCode
                    sessionId = null
                    logFailedStatus("$TAG_LOG_DFM_BG {$additionalTag}", applicationContext, moduleNameToDownload, errorCode?.toString()
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
                            additionalTag: String) {
        GlobalScope.launch {
            try {
                installOnBackgroundDefer(context, moduleNames, onSuccessInstall, onFailedInstall, additionalTag = additionalTag)
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
            logSuccessStatus(TAG_LOG_DFM_BG_UNINSTALL, applicationContext, moduleNameToUninstall)
        }.addOnFailureListener {
            val errorCode = (it as? SplitInstallException)?.errorCode
            logFailedStatus(TAG_LOG_DFM_BG_UNINSTALL, applicationContext, moduleNameToUninstall,
                errorCode?.toString() ?: it.toString())
        }
    }

    private fun logSuccessStatus(tag: String, context: Context, moduleNameToDownload: List<String>) {
        if (tag == TAG_LOG_DFM_BG || tag == DFInstallerActivity.TAG_LOG) {
            DFTracking.trackDownloadDF(moduleNameToDownload,
                null,
                tag == TAG_LOG_DFM_BG)
        }
        DFInstallerLogUtil.logStatus(context, tag,
            moduleNameToDownload.joinToString(), usableSpaceBeforeDownload, moduleSize, null, 0, false)
    }

    private fun logFailedStatus(tag: String, applicationContext: Context, moduleNameToDownload: List<String>,
                                 errorCode: String = "") {
        if (tag == TAG_LOG_DFM_BG || tag == DFInstallerActivity.TAG_LOG) {
            DFTracking.trackDownloadDF(moduleNameToDownload,
                listOf(errorCode),
                tag == TAG_LOG_DFM_BG)
        }
        DFInstallerLogUtil.logStatus(applicationContext,
            tag, moduleNameToDownload.joinToString(), usableSpaceBeforeDownload, moduleSize,
            listOf(errorCode), 0, false)
    }
}