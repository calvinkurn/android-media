package com.tokopedia.shop.common.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ShopPagePreFetchDataModel private constructor(
    val shopName: String,
    val shopAvatarImageUrl: String,
    val shopBadgeImageUrl: String,
) : Parcelable {
    companion object {
        const val PARCEL_KEY = "ShopPagePreFetchDataModel"
    }
    class Builder {
        private var shopName: String = ""
        private var shopAvatarImageUrl: String = ""
        private var shopBadgeImageUrl: String = ""

        fun setShopName(shopName: String): Builder {
            this.shopName = shopName
            return this
        }

        fun setShopAvatarImageUrl(shopAvatarImageUrl: String): Builder {
            this.shopAvatarImageUrl = shopAvatarImageUrl
            return this
        }

        fun setShopBadgeImageUrl(shopBadgeImageUrl: String): Builder {
            this.shopBadgeImageUrl = shopBadgeImageUrl
            return this
        }

        fun build(): ShopPagePreFetchDataModel {
            return ShopPagePreFetchDataModel(
                shopName = shopName,
                shopAvatarImageUrl = shopAvatarImageUrl,
                shopBadgeImageUrl = shopBadgeImageUrl,
            )
        }
    }
}
