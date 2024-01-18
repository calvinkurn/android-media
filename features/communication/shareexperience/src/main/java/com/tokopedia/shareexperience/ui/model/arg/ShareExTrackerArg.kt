package com.tokopedia.shareexperience.ui.model.arg

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExTrackerArg(
    val labelImpressionBottomSheet: String = "",
    val labelActionClickShareIcon: String = "",
    val labelActionCloseIcon: String = "",

    /**
     * Add string {channel} to this label, it will be replaced to actual channel user clicked
     */
    val labelActionClickChannel: String = "",

    /**
     * Affiliate trackers
     */
    val labelImpressionAffiliateTicker: String = "",
    val labelActionClickAffiliate: String = ""
): Parcelable
