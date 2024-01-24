package com.tokopedia.shareexperience.domain.model

import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExBottomSheetModel(
    val shareId: String? = null,
    val title: String = "",
    val subtitle: String = "",
    val bottomSheetPage: ShareExBottomSheetPageModel = ShareExBottomSheetPageModel()
) : Parcelable
