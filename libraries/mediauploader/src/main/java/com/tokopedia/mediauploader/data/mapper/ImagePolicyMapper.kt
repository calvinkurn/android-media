package com.tokopedia.mediauploader.data.mapper

import com.tokopedia.mediauploader.data.entity.SourcePolicy
import com.tokopedia.mediauploader.data.entity.UploaderPolicy

object ImagePolicyMapper {

    fun mapToSourcePolicy(dataPolicy: UploaderPolicy): SourcePolicy {
        return dataPolicy.sourcePolicy
    }

}