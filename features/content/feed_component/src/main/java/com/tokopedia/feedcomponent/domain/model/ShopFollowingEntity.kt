package com.tokopedia.feedcomponent.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 16/01/23
 */
data class ShopFollowingEntity(
    @SerializedName("shopInfoByID")
    @Expose
    val shopInfoById: ShopInfoById = ShopInfoById(),
    @SerializedName("feedXProfileIsFollowing")
    @Expose
    val feedXProfileIsFollowing: FeedXProfileIsFollowing = FeedXProfileIsFollowing()
)

data class ShopInfoById(
    @SerializedName("result")
    @Expose
    val result: List<ResultItem> = listOf()
)

data class ResultItem(
    @SerializedName("shopCore")
    @Expose
    val shopCore: ShopCore,
    @SerializedName("favoriteData")
    @Expose
    val favoriteData: FavoriteData = FavoriteData()
)

data class ShopCore(
    @SerializedName("shopID")
    @Expose
    val shopID: String = ""
)

data class FavoriteData(
    @SerializedName("alreadyFavorited")
    @Expose
    val alreadyFavorited: Int = 0
) {
    val isFollowing: Boolean get() = alreadyFavorited == IS_FOLLOWING

    companion object {
        const val IS_FOLLOWING = 1
    }
}

data class FeedXProfileIsFollowing(
    @SerializedName("isUserFollowing")
    @Expose
    val isUserFollowing: List<IsUserFollowing> = listOf()
)

data class IsUserFollowing(
    @SerializedName("userID")
    @Expose
    val userId: String = "",
    @SerializedName("status")
    @Expose
    val status: Boolean = false
)
