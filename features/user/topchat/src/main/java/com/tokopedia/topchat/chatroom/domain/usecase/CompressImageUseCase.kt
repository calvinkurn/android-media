package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author : Steven 2019-08-27
 */
class CompressImageUseCase @Inject constructor(private val dispatcher: CoroutineDispatchers) {

    suspend fun compressImage(imageUrl: String): String {
        return withContext(dispatcher.io) {
            val file = ImageProcessingUtil.compressImageFile(imageUrl, QUALITY_COMPRESS)
            file.absolutePath
        }
    }

    companion object {
        private var QUALITY_COMPRESS = 80
    }
}