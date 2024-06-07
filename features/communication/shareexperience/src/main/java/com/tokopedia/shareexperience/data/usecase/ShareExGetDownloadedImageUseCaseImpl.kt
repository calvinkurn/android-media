package com.tokopedia.shareexperience.data.usecase

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.shareexperience.domain.usecase.ShareExGetDownloadedImageUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ShareExGetDownloadedImageUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetDownloadedImageUseCase {

    override suspend fun downloadImageThumbnail(mediaUrl: String): Flow<ShareExResult<Uri>> {
        return callbackFlow {
            trySend(ShareExResult.Loading)
            mediaUrl.getBitmapImageUrl(
                context = context,
                properties = {
                    this.listener(
                        onSuccess = { bitmap, _ ->
                            if (bitmap != null) {
                                onSuccessDownloadThumbnail(this@callbackFlow, bitmap)
                            }
                        },
                        onError = {
                            if (it != null) {
                                launch {
                                    send(ShareExResult.Error(it))
                                    close()
                                }
                            }
                        },
                    )
                }
            )
            awaitClose { channel.close() }
        }.flowOn(dispatchers.io)
    }

    private fun onSuccessDownloadThumbnail(
        scope: ProducerScope<ShareExResult<Uri>>,
        bitmap: Bitmap
    ) {
        val outputFile = getImageCacheFile()
        // Save Image Thumbnail
        val outputStream = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        // Get Uri
        val contentUri = MethodChecker.getUri(context, outputFile)
        scope.launch {
            scope.send(ShareExResult.Success(contentUri))
            scope.close()
        }
    }

    private fun getImageCacheFile(): File {
        val filename = getFileName()
        val shareFolder = File("${context.externalCacheDir}/share")
        if (!shareFolder.exists()) {
            shareFolder.mkdirs()
        }
        return File(shareFolder, filename)
    }

    private fun getFileName(): String {
        return "downloaded_image_${System.currentTimeMillis()}.jpeg"
    }
}
