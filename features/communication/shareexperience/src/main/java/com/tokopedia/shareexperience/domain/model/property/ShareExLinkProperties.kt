package com.tokopedia.shareexperience.domain.model.property

import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.ShareExFeatureEnum
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
     Fill the campaignName with following format: {Page} - {Page ID} - {User ID} - {Sharing Date} - {Image Generator Source}
     Page: Whether page using this feature eg: PDP, Shop Page, Feed, etc
     Page ID: Specific id for the page eg: Product ID, Shop ID, Channel ID
     For non login user, you can skip the user ID part
     Sharing Date use DDMMYY format
     Example: PDP - 123456 - 765389 - 230321 - RXAbs1
     */
    val campaign: String = ""
) : Parcelable
