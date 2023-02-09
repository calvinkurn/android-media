package com.tokopedia.feedplus.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedTabsEntity(
    @SerializedName("data")
    @Expose
    val data: List<Data> = emptyList(),
    @SerializedName("meta")
    @Expose
    val meta: Meta = Meta()
) {
    fun getSortedData(): List<Data> = data.sortedBy {
        it.position
    }

    class Response(
        @SerializedName("feedTabs")
        @Expose
        val feedTabs: FeedTabsEntity = FeedTabsEntity()
    )
}

class Data(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("key")
    @Expose
    val key: String = "",
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("position")
    @Expose
    val position: Int = 0,
    @SerializedName("isActive")
    @Expose
    val isActive: Boolean = false
)

class Meta(
    @SerializedName("selectedIndex")
    @Expose
    val selectedIndex: Int = 0,
    @SerializedName("myProfileAppLink")
    @Expose
    val myProfileApplink: String = "",
    @SerializedName("myProfileWebLink")
    @Expose
    val myProfileWeblink: String = "",
    @SerializedName("myProfilePhotoURL")
    @Expose
    val myProfilePhotoUrl: String = "",
    @SerializedName("showMyProfile")
    @Expose
    val showMyProfile: Boolean = false
)
