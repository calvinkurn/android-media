package com.tokopedia.shareexperience.domain.model.property.linkproperties

data class ShareExLinkProperties(
    val message: String = "",
    val ogTitle: String = "",
    val ogDescription: String = "",
    val ogType: String = "",
    val ogImageUrl: String = "",
    val ogVideo: String = "",
    val desktopUrl: String = "",
    val androidUrl: String = "",
    val iosUrl: String = "",
    val iosDeeplinkPath: String = "",
    val canonicalUrl: String = "",
    val feature: ShareExFeatureEnum = ShareExFeatureEnum.SHARE,
    /**
     Fill the campaignName with following format: {Page} - {Page ID} - {User ID} - {Sharing Date}
     Page: Whether page using this feature eg: PDP, Shop Page, Feed, etc
     Page ID: Specific id for the page eg: Product ID, Shop ID, Channel ID
     For non login user, you can skip the user ID part
     Sharing Date use DDMMYY format
     Example: PDP - 123456 - 765389 - 230321
     */
    val campaign: String = ""
)
