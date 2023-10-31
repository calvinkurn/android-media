package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageSecurePolicyUseCase
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import com.tokopedia.picker.common.utils.isImageFormat
import java.io.File
import javax.inject.Inject

class GetSourcePolicyUseCase @Inject constructor(
    @UploaderQualifier private val policyManager: SourcePolicyManager,
    private val imagePolicyUseCase: GetImagePolicyUseCase,
    private val imageSecurePolicyUseCase: GetImageSecurePolicyUseCase,
    private val videoPolicyUseCase: GetVideoPolicyUseCase,
) {

    suspend operator fun invoke(param: Param): SourcePolicy {
        val policy = when {
            isImageFormat(param.file.path) -> {
                if (param.isSecure.not()) imagePolicyUseCase(param.sourceId)
                else imageSecurePolicyUseCase(param.sourceId)
            }
            else -> videoPolicyUseCase(param.sourceId)
        }

        return policy.also {
            policyManager.set(it)
        }
    }

    data class Param(
        val sourceId: String,
        val file: File,
        val isSecure: Boolean,
    )
}
