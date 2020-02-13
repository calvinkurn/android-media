package com.tokopedia.mediauploader.util

import android.os.Handler
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import android.os.Looper
import com.tokopedia.mediauploader.data.state.ProgressCallback
import java.io.File
import java.io.FileInputStream


class ProgressRequestBody(
        private val file: File,
        private val contentType: MediaType?,
        private val callback: ProgressCallback
): RequestBody() {

    override fun contentType(): MediaType? {
        return contentType
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = FileInputStream(file)
        var uploaded: Long = 0

        inputStream.use { stream ->
            var read = stream.read(buffer)
            val handler = Handler(Looper.getMainLooper())
            while (read != -1) {
                handler.post(ProgressUpdater(uploaded, fileLength, callback))
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = stream.read(buffer)
            }
        }
    }

    inner class ProgressUpdater(
            private val uploaded: Long,
            private val total: Long,
            private val callback: ProgressCallback): Runnable {
        override fun run() {
            callback.onProgress((100 * uploaded / total).toInt())
        }
    }

}