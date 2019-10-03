package com.tokopedia.dynamicfeatures

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

/**
 * Dynamic Installer, object that handle installing dynamic feature in background for application
 * Also handle the unify logging
 */
object DFInstaller {
    private var manager: SplitInstallManager? = null
    internal var sessionId: Int? = null
    const val TAG_LOG_DFM_BG = "DFM Background"

    private var listener: SplitInstallListener? = null
    internal var moduleSize = 0L

    private class SplitInstallListener(val application: Application,
                                       val moduleNameToDownload: List<String>) : SplitInstallStateUpdatedListener {
        override fun onStateUpdate(it: SplitInstallSessionState) {
            if (it.sessionId() != sessionId) {
                return
            }

            when (it.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    if (moduleSize == 0L) {
                        moduleSize = it.totalBytesToDownload()
                    }
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    logSuccessStatus(application, moduleNameToDownload)
                    unregisterListener()
                }
                SplitInstallSessionStatus.FAILED -> {
                    logFailedStatus(application, moduleNameToDownload, it.errorCode().toString())
                    unregisterListener()
                }
            }
        }
    }

    fun installOnBackground(application: Application, moduleNames: List<String>) {
        val applicationContext = application.applicationContext
        if (moduleNames.isEmpty()) {
            return
        }
        if (manager == null) {
            manager = SplitInstallManagerFactory.create(applicationContext)
        }
        val manager = manager ?: return

        moduleSize = 0

        val requestBuilder = SplitInstallRequest.newBuilder()

        val moduleNameToDownload = mutableListOf<String>()
        moduleNames.forEach { name ->
            if (!manager.installedModules.contains(name)) {
                requestBuilder.addModule(name)
                moduleNameToDownload.add(name)
            }
        }

        if (moduleNameToDownload.isEmpty()) return
        val request = requestBuilder.build()

        unregisterListener()
        registerListener(application, moduleNameToDownload)
        manager.startInstall(request).addOnSuccessListener {
            if (it == 0) {
                // success
                logSuccessStatus(applicationContext, moduleNameToDownload)
                unregisterListener()
            } else {
                sessionId = it
            }
        }.addOnFailureListener {
            val errorCode = (it as? SplitInstallException)?.errorCode
            sessionId = null
            logFailedStatus(applicationContext, moduleNameToDownload, errorCode?.toString() ?: it.toString())
            unregisterListener()
        }
    }

    internal fun logSuccessStatus(context: Context, moduleNameToDownload: List<String>) {
        DFInstallerLogUtil.logStatus(context, TAG_LOG_DFM_BG,
            moduleNameToDownload.joinToString(), moduleSize, null, 1, true)
    }

    internal fun logFailedStatus(applicationContext: Context, moduleNameToDownload: List<String>,
                                 errorCode: String = "") {
        DFInstallerLogUtil.logStatus(applicationContext,
            TAG_LOG_DFM_BG, moduleNameToDownload.joinToString(), moduleSize,
            listOf(errorCode), 1, false)
    }

    internal fun unregisterListener() {
        if (listener != null) {
            manager?.unregisterListener(listener)
            moduleSize = 0
            listener = null
        }
    }

    private fun registerListener(application: Application, moduleNameToDownload: List<String>) {
        listener = SplitInstallListener(application, moduleNameToDownload)
        manager?.registerListener(listener)
    }

}