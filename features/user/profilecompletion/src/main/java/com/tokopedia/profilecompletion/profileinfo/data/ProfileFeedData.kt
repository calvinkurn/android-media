package com.tokopedia.profilecompletion.profileinfo.data

import com.google.gson.annotations.SerializedName

data class ProfileFeedResponse (
    @SerializedName("feedXProfileForm")
    val profileFeedData: ProfileFeedData = ProfileFeedData()
)

data class ProfileFeedData (
    @SerializedName("profile")
    val profile: ProfileFeed = ProfileFeed(),

    @SerializedName("userProfileConfiguration")
    val userProfileConfiguration: UserProfileConfiguration = UserProfileConfiguration()
)

data class ProfileFeed(
    @SerializedName("username")
    val username: String = "",

    @SerializedName("biography")
    val biography: String = "",

    @SerializedName("sharelink")
    val shareLink: ShareLinkProfile = ShareLinkProfile(),
)

data class ShareLinkProfile(
    @SerializedName("weblink")
    val weblink: String = "",

    @SerializedName("applink")
    val applink: String = ""
)

data class UserProfileConfiguration(
    @SerializedName("usernameConfiguration")
    val usernameConfiguration: UsernameConfiguration = UsernameConfiguration(),

    @SerializedName("biographyConfiguration")
    val biographyConfiguration: BiographyConfiguration = BiographyConfiguration()

)

data class UsernameConfiguration(
    @SerializedName("maximumChar")
    val maximumChar: Int = 0,

    @SerializedName("minimumChar")
    val minimumChar: Int = 0
)

data class BiographyConfiguration(
    @SerializedName("maximumChar")
    val maximumChar: Int = 0
)


