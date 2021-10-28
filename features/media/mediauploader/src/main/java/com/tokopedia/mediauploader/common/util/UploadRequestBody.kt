package com.tokopedia.mediauploader.common.util

import android.os.Handler
import android.os.Looper
import com.tokopedia.mediauploader.common.state.ProgressCallback
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class UploadRequestBody(
    private val byteArray: ByteArray,
    private val contentType: MediaType?,
    private val callback: ProgressCallback?
): RequestBody() {

    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = byteArray.size.toLong()

    override fun writeTo(sink: BufferedSink) {
        val fileLength = byteArray.size
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val handler = Handler(Looper.getMainLooper())

        var uploaded = 0L
        var read = 0

        byteArray.forEach {
            handler.post(ProgressUpdater(uploaded, fileLength.toLong(), callback))
            uploaded += read.toLong()
            sink.write(buffer, 0, read)
            read = it.toInt()
        }
    }

    inner class ProgressUpdater(
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