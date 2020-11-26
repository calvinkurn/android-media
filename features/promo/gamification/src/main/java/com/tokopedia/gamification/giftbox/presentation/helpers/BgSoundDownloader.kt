package com.tokopedia.gamification.giftbox.presentation.helpers

import android.os.Handler
import android.os.Looper
import com.tkpd.remoteresourcerequest.callback.DeferredTaskCallback
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager
import com.tkpd.remoteresourcerequest.type.AudioType
import java.util.concurrent.ConcurrentHashMap

class BgSoundDownloader {
    private val fileNameFilePathMap: ConcurrentHashMap<String, String> = ConcurrentHashMap()
    private val runnablesMap = hashMapOf<String, Runnable>()
    private val handler = Handler(Looper.getMainLooper())

    fun downloadAndPlay(fileName: String, callback: (String?) -> Unit, delayMills: Long = -1) {

        runnablesMap[fileName] = Runnable { callback.invoke(fileNameFilePathMap[fileName]) }
        handler.postDelayed({ runnablesMap[fileName] }, delayMills)

        ResourceDownloadManager.getManager().startDownload(
                AudioType(remoteFileName = fileName),
                object : DeferredTaskCallback {
                    override fun onTaskCompleted(resourceUrl: String?, filePath: String?) {
                        if (!filePath.isNullOrEmpty()) {
                            try {
                                fileNameFilePathMap[fileName] = filePath
                                handler.removeCallbacks(runnablesMap[fileName])
                                handler.post { callback.invoke(filePath) }
                            } catch (ex: Exception) {
                            }
                        }
                    }

                    override fun onTaskFailed(resourceUrl: String?) {
                    }
                })
    }

    fun removeAll() {
        try {
            runnablesMap.forEach {
                handler.removeCallbacks(it.value)
            }
        } catch (ex: Exception) {
        }
    }
}