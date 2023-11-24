package com.tokopedia.mediauploader.common

import com.tokopedia.mediauploader.common.model.SourcePolicyModel
import com.tokopedia.mediauploader.common.util.isImageFormat
import com.tokopedia.mediauploader.common.util.parseErrorMessage
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageSecurePolicyUseCase
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import com.tokopedia.network.exception.MessageErrorException
import java.io.File
import javax.inject.Inject

class GetSourcePolicyUseCase @Inject constructor(
    private val imagePolicyUseCase: GetImagePolicyUseCase,
    private val imageSecurePolicyUseCase: GetImageSecurePolicyUseCase,
    private val videoPolicyUseCase: GetVideoPolicyUseCase,
) {

    suspend operator fun invoke(param: Param): SourcePolicyModel {
        return try {
            val response = when {
                isImageFormat(param.file.path) -> {
                    if (param.isSecure.not()) imagePolicyUseCase(param.sourceId)
                    else imageSecurePolicyUseCase(param.sourceId)
                }
                else -> videoPolicyUseCase(param.sourceId)
            }

            SourcePolicyModel(response)
        } catch (t: MessageErrorException) {
            val (message, requestId) = t.message?.parseErrorMessage()
                ?: return SourcePolicyModel()

            SourcePolicyModel(
                errorMessage = message,
                requestId = requestId
            )
        }
    }

    data class Param(
        val file: File,
        val sourceId: String,
        val isSecure: Boolean
    )
}
