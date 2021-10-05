package com.tokopedia.mediauploader.image.data.mapper

import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.data.entity.UploaderPolicy

object ImagePolicyMapper {

    fun mapToSourcePolicy(dataPolicy: UploaderPolicy): SourcePolicy {
        return dataPolicy.sourcePolicy
    }

}