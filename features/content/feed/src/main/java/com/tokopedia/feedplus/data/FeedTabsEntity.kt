package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedTabsEntity(
    @SerializedName("data")
    val data: List<Data> = emptyList(),
    @SerializedName("meta")
    val meta: Meta = Meta()
) {
    fun getSortedData(): List<Data> = data.sortedBy {
        it.position
    }

    class Response(
        @SerializedName("feedTabs")
        val feedTabs: FeedTabsEntity = FeedTabsEntity()
    )
}

class Data(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("key")
    val key: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("position")
    val position: Int = 0,
    @SerializedName("isActive")
    val isActive: Boolean = false
)

class Meta(
    @SerializedName("selectedIndex")
    val selectedIndex: Int = 0,
    @SerializedName("myProfileAppLink")
    val myProfileApplink: String = "",
    @SerializedName("myProfileWebLink")
    val myProfileWeblink: String = "",
    @SerializedName("myProfilePhotoURL")
    val myProfilePhotoUrl: String = "",
    @SerializedName("showMyProfile")
    val showMyProfile: Boolean = false
)
