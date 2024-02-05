package com.tokopedia.shareexperience.domain.model.property

import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.ShareExFeatureEnum
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExLinkProperties(
    val message: String = "",
    val ogTitle: String = "",
    val ogDescription: String = "",
    val ogType: String = "",
    val ogImageUrl: String = "",
    val ogVideo: String = "",
    val originalUrl: String = "",
    val androidUrl: String = "",
    val iosUrl: String = "",
    val desktopUrl: String = "",
    val androidDeeplinkPath: String = "",
    val iosDeeplinkPath: String = "",
    val canonicalUrl: String = "",
    val canonicalIdentifier: String = "",
    val customMetaTags: String = "",
    val anMinVersion: String = "",
    val feature: ShareExFeatureEnum = ShareExFeatureEnum.SHARE,
    /**
     * Fill the campaignName from [ShareExTrackerArg] utmCampaign
     * Different implementation of utm campaign each page
     */
    val campaign: String = ""
) : Parcelable
