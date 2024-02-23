package com.tokopedia.shareexperience.data.usecase

import android.content.Context
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shareexperience.domain.usecase.ShareExGetDownloadedImageUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import com.tokopedia.shareexperience.domain.util.asFlowResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

class ShareExGetDownloadedImageUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetDownloadedImageUseCase {

    override suspend fun downloadImage(imageUrl: String): Flow<ShareExResult<Uri>> {
        return flow {
            val filename = getFileName()
            val outputFile = File(context.cacheDir, filename)
            val inputStream = URL(imageUrl).openStream()
            val outputStream = FileOutputStream(outputFile)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            val contentUri = MethodChecker.getUri(context, outputFile)
            emit(contentUri)
        }
            .asFlowResult()
            .flowOn(dispatchers.io)
    }

    private fun getFileName(): String {
        return "downloaded_image_${System.currentTimeMillis()}.jpeg"
    }
}
