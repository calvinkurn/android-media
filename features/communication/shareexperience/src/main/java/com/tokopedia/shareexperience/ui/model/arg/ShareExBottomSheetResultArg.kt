package com.tokopedia.shareexperience.ui.model.arg

import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExBottomSheetResultArg(
    val bottomSheetModel: ShareExBottomSheetModel? = null,
    val throwable: Throwable? = null
) : Parcelable
