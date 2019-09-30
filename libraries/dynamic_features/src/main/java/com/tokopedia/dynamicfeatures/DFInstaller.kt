package com.tokopedia.dynamicfeatures

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import timber.log.Timber
import java.io.File

object DFInstaller {
    private var manager: SplitInstallManager? = null
    private const val MEGA_BYTE = 1024 * 1024
    internal var sessionId: Int? = null

    private var listener: SplitInstallListener? = null

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
                        logDownloadingStatus(application, moduleNameToDownload, it.totalBytesToDownload())
                        initialDownloading = false
                    }
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    logSuccessStatus(application, moduleNameToDownload)
                }
                SplitInstallSessionStatus.FAILED -> {
                    logFailedStatus(application, moduleNameToDownload, it.errorCode())
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
            val errorCode = (it as? SplitInstallException)?.errorCode ?: 0
            sessionId = null
            logFailedStatus(applicationContext, moduleNameToDownload, errorCode)
            unregisterListener()
        }
        registerListener(application, moduleNameToDownload)
    }

    private fun unregisterListener() {
        if (listener != null) {
            manager?.unregisterListener(listener)
            listener = null
        }
    }

    private fun registerListener(application: Application, moduleNameToDownload: List<String>) {
        listener = SplitInstallListener(application, moduleNameToDownload)
        manager?.registerListener(listener)
    }

    internal fun logDownloadingStatus(context: Context, moduleNameToDownload: List<String>, moduleSize: Long = 0) {
        val totalSize = File(context.filesDir.absoluteFile.toString()).freeSpace.toDouble()
        val totalFreeSpaceSizeInMB = String.format("%.2fMB", totalSize / MEGA_BYTE)
        val moduleSizeinMB = String.format("%.2fMB", moduleSize.toDouble() / MEGA_BYTE)
        Timber.w("P1Downloading Module Background {${moduleNameToDownload.joinToString()}} {$moduleSizeinMB:$totalFreeSpaceSizeInMB}")
    }

    internal fun logSuccessStatus(context: Context, moduleNameToDownload: List<String>) {
        val totalSize = File(context.filesDir.absoluteFile.toString()).freeSpace.toDouble()
        val totalFreeSpaceSizeInMB = String.format("%.2fMB", totalSize / MEGA_BYTE)
        Timber.w("P1Installed Module Background {${moduleNameToDownload.joinToString()}} {$totalFreeSpaceSizeInMB}")
    }

    internal fun logFailedStatus(context: Context, moduleNameToDownload: List<String>, errorCode: Int = 0) {
        val totalSize = File(context.filesDir.absoluteFile.toString()).freeSpace.toDouble()
        val totalFreeSpaceSizeInMB = String.format("%.2fMB", totalSize / MEGA_BYTE)
        Timber.w("P1Failed Module Background {${moduleNameToDownload.joinToString()}} {$totalFreeSpaceSizeInMB} - {$errorCode}")
    }

}