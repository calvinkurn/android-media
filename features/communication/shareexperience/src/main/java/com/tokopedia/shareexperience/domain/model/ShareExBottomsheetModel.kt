package com.tokopedia.shareexperience.domain.model

import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel

data class ShareExBottomsheetModel(
    val title: String = "",
    val subtitle: String = "",
    val listProperty: List<ShareExPropertyModel> = listOf(),
    val imageGenerator: ShareExImageGeneratorModel? = null
)
