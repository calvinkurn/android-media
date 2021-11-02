package com.tokopedia.mediauploader.common.data.mapper

import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.data.entity.UploaderPolicy

object PolicyMapper {

    fun map(dataPolicy: UploaderPolicy): SourcePolicy {
        return dataPolicy.sourcePolicy
    }

}