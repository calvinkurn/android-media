package com.tokopedia.mediauploader.common.util

import android.os.Handler
import android.os.Looper
import com.tokopedia.mediauploader.common.state.ProgressCallback
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadRequestBody(
    private val file: File,
    private val contentType: MediaType?,
    private val callback: ProgressCallback?
) : RequestBody() {

    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = FileInputStream(file)
        var uploaded = 0L

        inputStream.use { stream ->
            val handler = Handler(Looper.getMainLooper())
            var read = 0
            while (read != -1) {
                handler.post(ProgressUpdater(uploaded, fileLength, callback))
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = stream.read(buffer)
            }
        }
    }

    private class ProgressUpdater(
        private val uploaded: Long,
        private val total: Long,
        private val callback: ProgressCallback?
    ): Runnable {
        override fun run() {
            callback?.onProgress((MAX_PROGRESS_UPDATER * uploaded / total).toInt())
        }
    }

    companion object {
        private const val MAX_PROGRESS_UPDATER = 100
    }

}