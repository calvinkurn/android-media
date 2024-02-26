package com.tokopedia.shareexperience.domain.model.imagegenerator

import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum

data class ShareExImageGeneratorModel(
    val imageUrl: String = "",
    val imageTypeEnum: ShareExImageTypeEnum
)
