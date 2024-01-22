package com.tokopedia.shareexperience.ui.model.arg

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExTrackerArg(
    /**
     * Add [SHARE_ID_KEY] to any label, it will be replaced to actual share id (for session tracking)
     */
    val labelImpressionBottomSheet: String = "",
    val labelActionClickShareIcon: String = "",
    val labelActionCloseIcon: String = "",

    /**
     * Add [CHANNEL_KEY] & [IMAGE_TYPE_KEY] to this label, it will be replaced to actual value
     * Channel : The app / channel user clicked, ex: whatsapp, copy link
     * Image Type : No Image, Default, Contextual Image (depends on the response from BE)
     */
    val labelActionClickChannel: String = "",

    /**
     * Affiliate trackers
     *  Add string {commission_amount} to this label, it will be replaced to actual value
     */
    val labelImpressionAffiliateRegistration: String = "",
    val labelActionClickAffiliateRegistration: String = ""
) : Parcelable {
    companion object {
        const val SHARE_ID_KEY = "{share_id}"
        const val CHANNEL_KEY = "{channel}"
        const val IMAGE_TYPE_KEY = "{image_type}"
    }
}
