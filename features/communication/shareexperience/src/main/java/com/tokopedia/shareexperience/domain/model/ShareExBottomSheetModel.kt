package com.tokopedia.shareexperience.domain.model

import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel

data class ShareExBottomSheetModel(
    val title: String = "",
    val subtitle: String = "",
    val bottomSheetPage: ShareExBottomSheetPageModel = ShareExBottomSheetPageModel()
)
