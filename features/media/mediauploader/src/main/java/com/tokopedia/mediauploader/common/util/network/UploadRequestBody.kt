package com.tokopedia.mediauploader.common.util.network

import android.os.Handler
import android.os.Looper
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.ProgressUploader
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.http.CallServerInterceptor
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadRequestBody(
    private val file: File,
    private val contentType: MediaType?,
    private val uploader: ProgressUploader?
) : RequestBody() {

    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val handler = Handler(Looper.getMainLooper())
        val fileLength = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = FileInputStream(file)
        var uploaded = 0L

        inputStream.use { stream ->
            var read = 0
            while (read != -1) {
                val isCalledByCallServerInterceptor =
                    Thread.currentThread().stackTrace.any { stackTraceElement ->
                        stackTraceElement.className == CallServerInterceptor::class.java.canonicalName
                    }

                if (isCalledByCallServerInterceptor) {
                    handler.post(ProgressUpdater(uploaded, fileLength, uploader))
                }

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = stream.read(buffer)
            }
        }
    }

    private class ProgressUpdater(
        private val uploaded: Long,
        private val total: Long,
        private val uploader: ProgressUploader?
    ) : Runnable {
        override fun run() {
            uploader?.onProgress(
                percentage = (MAX_PROGRESS_LOADER * uploaded / total).toInt(),
                type = ProgressType.Upload
            )
        }
    }

    companion object {
        private const val MAX_PROGRESS_LOADER = 100
    }

}
