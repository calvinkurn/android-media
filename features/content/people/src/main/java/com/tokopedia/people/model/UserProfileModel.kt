package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

data class ProfileHeaderBase(
    @SerializedName("feedXProfileHeader")
    val profileHeader: FeedXProfileHeader = FeedXProfileHeader()
)

data class FeedXProfileHeader(
    @SerializedName("profile")
    val profile: Profile = Profile(),

    @SerializedName("stats")
    val stats: Stats = Stats(),

    @SerializedName("hasAcceptTnC")
    val hasAcceptTnC: Boolean = false,

    @SerializedName("shouldSeoIndex")
    val shouldSeoIndex: Boolean = false,

    @SerializedName("isBlocking")
    val isBlocking: Boolean = false,

    @SerializedName("isBlockedBy")
    val isBlockedBy: Boolean = false
)

data class Profile(
    @SerializedName("userID")
    val userID: String = "",

    @SerializedName("encryptedUserID")
    val encryptedUserID: String = "",

    @SerializedName("imageCover")
    val imageCover: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("username")
    val username: String = "",

    @SerializedName("biography")
    var biography: String = "",

    @SerializedName("sharelink")
    val sharelink: Link = Link(),

    /**
     * "badges": [
     *   "Official Store",
     *   "https://images.tokopedia.net/img/official_store/badge_os.png"
     *  ]
     */
    @SerializedName("badges")
    val badges: List<String> = emptyList(),

    @SerializedName("liveplaychannel")
    val liveplaychannel: Liveplaychannel = Liveplaychannel(),

    /**
     * "userBadges": [
     *   {
     *   "link": "https://images.tokopedia.net/img/feeds-profile/badge/verified_badge.png",
     *   "isClickable": true,
     *   "bottomsheetTitle": "Akun terverifikasi",
     *   "bottomsheetDescription": "Terverifikasi sejak Januari 2017 karena dikelola oleh Tokopedia."
     *   }
     *   ]
     */
    @SerializedName("userBadges")
    val profileBadges: List<Badge> = emptyList()
) {
    data class Badge(
        @SerializedName("link")
        val badgeUrl: String = "",

        @SerializedName("isClickable")
        val isClickable: Boolean = false,

        @SerializedName("bottomsheetTitle")
        val bottomSheetTitle: String = "",

        @SerializedName("bottomsheetDescription")
        val bottomSheetDesc: String = ""
    )
}

data class Liveplaychannel(
    @SerializedName("islive")
    val islive: Boolean = false,

    @SerializedName("liveplaychannelid")
    val liveplaychannelid: String = "",

    @SerializedName("liveplaychannellink")
    val liveplaychannellink: Link = Link()
)

data class Link(
    @SerializedName("applink")
    val applink: String = "",

    @SerializedName("weblink")
    val weblink: String = ""
)

data class Stats(
    @SerializedName("totalPost")
    val totalPost: Long = 0L,

    @SerializedName("totalPostFmt")
    val totalPostFmt: String = "",

    @SerializedName("totalFollower")
    val totalFollower: Long = 0L,

    @SerializedName("totalFollowerFmt")
    val totalFollowerFmt: String = "",

    @SerializedName("totalFollowing")
    val totalFollowing: Long = 0L,

    @SerializedName("totalFollowingFmt")
    val totalFollowingFmt: String = "",

    @SerializedName("ExtraStats")
    val extraStats: List<ExtraStats> = emptyList()
)

data class ExtraStats(
    @SerializedName("field")
    val field: String = "",

    @SerializedName("count")
    val count: Int = 0,

    @SerializedName("countFmt")
    val countFmt: String = ""
)
