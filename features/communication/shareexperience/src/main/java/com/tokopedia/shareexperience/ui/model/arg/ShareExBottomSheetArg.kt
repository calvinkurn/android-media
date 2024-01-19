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
    val trackerArg: ShareExTrackerArg,

    /**
     * Optional
     */
    val selectedChip: String = "",

    /**
     * Based on response
     * Do not fill this
     */
    val bottomSheetModel: ShareExBottomSheetModel? = null,
    val throwable: Throwable? = null
) : Parcelable
