package com.tokopedia.shareexperience.ui.model.arg

import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExBottomSheetArg(
    /**
     * Mandatory
     */
    val identifier: String,
    val pageTypeEnum: ShareExPageTypeEnum,
    val defaultUrl: String,

    /**
     * Based on response
     */
    val bottomSheetModel: ShareExBottomSheetModel? = null,
    val throwable: Throwable? = null,

    /**
     * Optional
     */
    val selectedChip: String = "",
): Parcelable
