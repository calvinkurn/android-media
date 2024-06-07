package com.tokopedia.tokochat.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.tokochat.config.util.TokoChatErrorLogger
import com.tokopedia.tokochat.config.util.TokoChatErrorLogger.ErrorType.ERROR_PAGE
import com.tokopedia.tokochat.config.util.TokoChatErrorLogger.PAGE.TOKOCHAT
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.data.MediaException
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.listener.MediaListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.utils.ImageCompressor
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.EIGHT_DP
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil.DEFAULT_DIRECTORY
import timber.log.Timber
import java.io.File
import java.io.InputStream
import javax.inject.Inject
import com.tokopedia.tokochat_common.R as tokochat_commonR

class TokoChatViewUtil @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userSession: UserSessionInterface
) {

    fun downloadAndSaveByteArrayImage(
        fileName: String,
        inputStream: InputStream,
        onImageReady: (File?) -> Unit,
        onError: () -> Unit,
        onDirectLoad: () -> Unit,
        imageView: ImageView?
    ) {
        try {
            val imageResult: File? = writeImageToTokoChatPath(inputStream.readBytes(), fileName)
            onImageReady(imageResult)
        } catch (throwable: Throwable) {
            loadByteArrayImage(context, imageView, inputStream, onError, onDirectLoad)
        } finally {
            closeInputStream(inputStream)
        }
    }

    private fun closeInputStream(inputStream: InputStream) {
        try {
            inputStream.close()
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun writeImageToTokoChatPath(buffer: ByteArray?, fileName: String): File? {
        if (buffer != null) {
            val photo: File = getTokoChatPhotoPath(fileName)
            if (photo.exists()) {
                photo.delete()
            }
            if (FileUtil.writeBufferToFile(buffer, photo.path)) {
                return photo
            }
        }
        return null
    }

    // Handle fail save image and directly load the image

    private fun loadByteArrayImage(
        context: Context,
        imageView: ImageView?,
        inputStream: InputStream,
        onError: () -> Unit,
        onDirectLoad: () -> Unit
    ) {
        try {
            if (imageView != null) {
                directLoadImageOnUiThread(imageView, inputStream, onError, onDirectLoad)
            } else {
                onError()
            }
        } catch (throwable: Throwable) {
            logError(throwable, ::loadByteArrayImage.name)
        } finally {
            closeInputStream(inputStream)
        }
    }

    private fun directLoadImageOnUiThread(
        imageView: ImageView,
        inputStream: InputStream,
        onError: () -> Unit,
        onDirectLoad: () -> Unit
    ) {
        Handler(Looper.getMainLooper()).post {
            imageView.loadImage(inputStream.readBytes()) {
                setPlaceHolder(tokochat_commonR.drawable.tokochat_bg_image_bubble_gradient)
                setErrorDrawable(tokochat_commonR.drawable.tokochat_bg_image_bubble_gradient)
                transforms(listOf(CenterCrop(), RoundedCorners(EIGHT_DP.toPx())))
                listener(
                    onSuccess = { _, _ ->
                        onDirectLoad()
                        closeInputStream(inputStream)
                    },
                    onError = { e ->
                        onError()
                        closeInputStream(inputStream)
                        e?.let {
                            logError(it, MediaListener::onFailed.name)
                        }
                    }
                )
            }
        }
    }

    private fun logError(throwable: Throwable, description: String) {
        TokoChatErrorLogger.logExceptionToServerLogger(
            TOKOCHAT,
            throwable,
            ERROR_PAGE,
            userSession.deviceId.orEmpty(),
            description
        )
    }

    fun compressImageToTokoChatPath(originalFilePath: String): Uri? {
        return ImageCompressor.compress(
            context = context,
            imagePath = originalFilePath,
            subDirectory = TOKOCHAT_RELATIVE_PATH
        )
    }

    fun renameAndMoveFileToTokoChatDir(originalFileUri: Uri, newFileName: String): String? {
        FileUtil.getPath(context.contentResolver, originalFileUri)?.let { resultPath ->
            val tempResultFile = File(resultPath)
            val renamedResultFile = getTokoChatPhotoPath(newFileName)
            tempResultFile.renameTo(renamedResultFile)

            return renamedResultFile.path
        }
        return null
    }

    companion object {
        private const val TOKOCHAT_RELATIVE_PATH = "TokoChat"
        private const val JPEG_EXT = ".jpeg"

        fun getTokoChatPhotoPath(fileName: String): File {
            return File(
                getTokopediaTokoChatCacheDirectory().absolutePath,
                fileName + JPEG_EXT
            )
        }

        fun getTokopediaTokoChatCacheDirectory(): File {
            return FileUtil.getTokopediaInternalDirectory(
                "${DEFAULT_DIRECTORY}$TOKOCHAT_RELATIVE_PATH"
            )
        }

        fun getInternalCacheDirectory(): File {
            val directory = File(GlobalConfig.INTERNAL_CACHE_DIR, TOKOCHAT_RELATIVE_PATH)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            return directory
        }
    }
}
