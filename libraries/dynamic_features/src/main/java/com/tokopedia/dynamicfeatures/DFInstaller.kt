package com.tokopedia.dynamicfeatures

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.dynamicfeatures.service.DFJobService
import com.tokopedia.dynamicfeatures.track.DFTracking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Dynamic Installer, object that handle installing dynamic feature in background for application
 * Also handle the unify logging
 */
class DFInstaller {
    internal var sessionId: Int? = null

    companion object {
        internal var manager: SplitInstallManager? = null
        const val TAG_LOG_DFM_BG = "DFM Background"
        const val TAG_LOG_DFM_BG_UNINSTALL = "DFM BGUninstall"
        @JvmStatic
        fun isInstalled(application: Application, moduleName: String):Boolean {
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

    private var listener: SplitInstallListener? = null
    internal var moduleSize = 0L
    internal var usableSpaceBeforeDownload: Long = 0L

    fun installOnBackground(application: Application, moduleNames: List<String>,
                            onSuccessInstall: (() -> Unit)? = null,
                            onFailedInstall: (() -> Unit)? = null) {
        GlobalScope.launch (Dispatchers.Default) {
            val applicationContext = application.applicationContext
            if (moduleNames.isEmpty()) {
                return@launch
            }
            initManager(applicationContext)
            val manager = manager ?: return@launch

            moduleSize = 0

            val requestBuilder = SplitInstallRequest.newBuilder()

            val moduleNameToDownload = mutableListOf<String>()
            moduleNames.forEach { name ->
                if (!manager.installedModules.contains(name)) {
                    requestBuilder.addModule(name)
                    moduleNameToDownload.add(name)
                }
            }

            if (moduleNameToDownload.isEmpty()) return@launch
            val request = requestBuilder.build()

            usableSpaceBeforeDownload = DFInstallerLogUtil.getFreeSpaceBytes(applicationContext)

            unregisterListener()
            registerListener(application, moduleNameToDownload, onSuccessInstall, onFailedInstall)

            val forDebug = true
            if (forDebug) {
                onFailedInstall?.invoke()
                unregisterListener()
                DFJobService.startService(applicationContext)
            } else {
                manager.startInstall(request).addOnSuccessListener {
                    if (it == 0) {
                        // success
                        logSuccessStatus(TAG_LOG_DFM_BG, applicationContext, moduleNameToDownload)
                        onSuccessInstall?.invoke()
                        unregisterListener()
                    } else {
                        sessionId = it
                    }
                }.addOnFailureListener {
                    val errorCode = (it as? SplitInstallException)?.errorCode
                    sessionId = null
                    logFailedStatus(TAG_LOG_DFM_BG, applicationContext, moduleNameToDownload, errorCode?.toString()
                        ?: it.toString())
                    onFailedInstall?.invoke()
                    unregisterListener()
                    DFJobService.startService(applicationContext)
                }
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
            // current no op
        }.addOnFailureListener {
            val errorCode = (it as? SplitInstallException)?.errorCode
            logFailedStatus(TAG_LOG_DFM_BG_UNINSTALL, applicationContext, moduleNameToUninstall,
                errorCode?.toString() ?: it.toString())
        }
    }

    internal fun logSuccessStatus(tag: String, context: Context, moduleNameToDownload: List<String>) {
        if (tag == TAG_LOG_DFM_BG || tag == DFInstallerActivity.TAG_LOG) {
            DFTracking.trackDownloadDF(moduleNameToDownload,
                null,
                tag == TAG_LOG_DFM_BG)
        }
        DFInstallerLogUtil.logStatus(context, tag,
            moduleNameToDownload.joinToString(), moduleSize, usableSpaceBeforeDownload, null, 1, true)
    }

    internal fun logFailedStatus(tag: String, applicationContext: Context, moduleNameToDownload: List<String>,
                                 errorCode: String = "") {
        if (tag == TAG_LOG_DFM_BG || tag == DFInstallerActivity.TAG_LOG) {
            DFTracking.trackDownloadDF(moduleNameToDownload,
                listOf(errorCode),
                tag == TAG_LOG_DFM_BG)
        }
        DFInstallerLogUtil.logStatus(applicationContext,
            tag, moduleNameToDownload.joinToString(), usableSpaceBeforeDownload, moduleSize,
            listOf(errorCode), 1, false)
    }

    internal fun unregisterListener() {
        if (listener != null) {
            manager?.unregisterListener(listener)
            moduleSize = 0
            listener = null
        }
    }

    private fun registerListener(application: Application, moduleNameToDownload: List<String>,
                                 onSuccessInstall: (() -> Unit)? = null,
                                 onFailedInstall: (() -> Unit)? = null) {
        listener = SplitInstallListener(this, application, moduleNameToDownload, onSuccessInstall, onFailedInstall)
        manager?.registerListener(listener)
    }
}

private class SplitInstallListener(val dfInstaller: DFInstaller,
                                   val application: Application,
                                   val moduleNameToDownload: List<String>,
                                   val onSuccessInstall: (() -> Unit)? = null,
                                   val onFailedInstall: (() -> Unit)? = null) : SplitInstallStateUpdatedListener {
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
                dfInstaller.logSuccessStatus(DFInstaller.TAG_LOG_DFM_BG, application, moduleNameToDownload)
                onSuccessInstall?.invoke()
                dfInstaller.unregisterListener()
            }
            SplitInstallSessionStatus.FAILED -> {
                DFTracking.trackDownloadDF(moduleNameToDownload,
                    listOf(it.errorCode().toString()),
                    true)
                dfInstaller.logFailedStatus(DFInstaller.TAG_LOG_DFM_BG, application, moduleNameToDownload, it.errorCode().toString())
                onFailedInstall?.invoke()
                dfInstaller.unregisterListener()
            }
        }
    }
}