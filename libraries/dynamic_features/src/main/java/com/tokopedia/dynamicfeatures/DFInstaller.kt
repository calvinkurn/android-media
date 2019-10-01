package com.tokopedia.dynamicfeatures

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import java.io.File

object DFInstaller {
    private var manager: SplitInstallManager? = null
    private const val MEGA_BYTE = 1024 * 1024
    internal var sessionId: Int? = null

    private var listener: SplitInstallListener? = null
    private var moduleSize: Long = 0

    private class SplitInstallListener(val application: Application,
                                       val moduleNameToDownload: List<String>) : SplitInstallStateUpdatedListener {
        var initialDownloading = true
        override fun onStateUpdate(it: SplitInstallSessionState) {
            if (it.sessionId() != sessionId) {
                return
            }

            when (it.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    if (initialDownloading) {
                        moduleSize = it.totalBytesToDownload()
                        logDownloadingStatus(application, moduleNameToDownload)
                        initialDownloading = false
                    }
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    logSuccessStatus(application, moduleNameToDownload)
                }
                SplitInstallSessionStatus.FAILED -> {
                    logFailedStatus(application, moduleNameToDownload, it.errorCode().toString())
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
            logFailedStatus(applicationContext, moduleNameToDownload,
                errorCode?.toString() ?: it.toString())
            unregisterListener()
        }
        registerListener(application, moduleNameToDownload)
    }

    private fun unregisterListener() {
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

    internal fun logDownloadingStatus(context: Context, moduleNameToDownload: List<String>) {
        logStatus(context, "Downloading Module Background", moduleNameToDownload.joinToString(), moduleSize)
    }

    internal fun logSuccessStatus(context: Context, moduleNameToDownload: List<String>) {
        logStatus(context, "Installed Module Background", moduleNameToDownload.joinToString(), moduleSize)
    }

    internal fun logFailedStatus(context: Context, moduleNameToDownload: List<String>, errorCode: String = "") {
        logStatus(context, "Failed Module Background", moduleNameToDownload.joinToString(), moduleSize, errorCode)
    }

    internal fun logStatus(context: Context,
                           tag: String = "",
                           modulesName: String,
                           moduleSize: Long = 0,
                           errorCode: String = "") {
        val messageStringBuilder = StringBuilder()
        messageStringBuilder.append("P1$tag {$modulesName}; ")

        val totalSize = File(context.filesDir.absoluteFile.toString()).freeSpace.toDouble()
        val totalFreeSpaceSizeInMB = String.format("%.2fMB", totalSize / MEGA_BYTE)
        messageStringBuilder.append("free: {$totalFreeSpaceSizeInMB}; ")

        if (moduleSize > 0) {
            val moduleSizeinMB = String.format("%.2fMB", moduleSize.toDouble() / MEGA_BYTE)
            messageStringBuilder.append("size: {$moduleSizeinMB}; ")
        }
        if (errorCode > 0) {
            messageStringBuilder.append("err: {$errorCode} ")
        }
    }

}