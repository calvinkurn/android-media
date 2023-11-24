package com.tokopedia.mediauploader.common.model

import com.tokopedia.mediauploader.common.data.entity.SourcePolicy

data class SourcePolicyModel(
    val policy: SourcePolicy? = null,
    val errorMessage: String? = "",
    val requestId: String? = "",
)
