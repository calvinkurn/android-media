package com.tokopedia.shareexperience.domain.model

import com.tokopedia.shareexperience.domain.model.property.ShareExBodyModel

data class ShareExBottomSheetModel(
    val title: String = "",
    val subtitle: String = "",
    val body: ShareExBodyModel = ShareExBodyModel()
)
