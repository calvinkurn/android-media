package com.tokopedia.feedcomponent.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 16/01/23
 */
data class ShopFollowingEntity(
    @SerializedName("shopInfoByID")
    @Expose
    val shopInfoById: ShopInfoById = ShopInfoById()
)

data class ShopInfoById(
    @SerializedName("result")
    @Expose
    val result: List<ResultItem> = listOf()
)

data class ResultItem(
    @SerializedName("ownerInfo")
    @Expose
    val ownerInfo: OwnerInfo,
    @SerializedName("favoriteData")
    @Expose
    val favoriteData: FavoriteData = FavoriteData()
)

data class OwnerInfo(
    @SerializedName("id")
    @Expose
    val id: String = ""
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
