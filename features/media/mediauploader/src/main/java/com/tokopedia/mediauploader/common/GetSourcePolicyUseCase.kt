package com.tokopedia.mediauploader.common

import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.util.isImageFormat
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageSecurePolicyUseCase
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import java.io.File
import javax.inject.Inject

class GetSourcePolicyUseCase @Inject constructor(
    private val imagePolicyUseCase: GetImagePolicyUseCase,
    private val imageSecurePolicyUseCase: GetImageSecurePolicyUseCase,
    private val videoPolicyUseCase: GetVideoPolicyUseCase,
) {

    suspend operator fun invoke(param: Param): SourcePolicy {
        return when {
            isImageFormat(param.file.path) -> {
                if (param.isSecure.not()) imagePolicyUseCase(param.sourceId)
                else imageSecurePolicyUseCase(param.sourceId)
            }
            else -> videoPolicyUseCase(param.sourceId)
        }
    }

    data class Param(
        val file: File,
        val sourceId: String,
        val isSecure: Boolean
    )
}
