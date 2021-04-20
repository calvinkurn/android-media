package com.tokopedia.search.result.shop.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.ShopStatus.*
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactory

internal data class ShopDataView(
        val source: String = "",
        val searchUrl: String = "",
        val paging: Paging = Paging(),
        val tabName: String = "",
        val shopItemList: List<ShopItem> = listOf(),
        val recommendationShopItemList: List<ShopItem> = listOf()
): Parcelable {

    val hasNextPage = paging.uriNext != ""

    data class Paging(
            val uriNext: String = "",
            val uriPrevious: String = ""
    ) : Parcelable {
        constructor(parcel: Parcel) : this(parcel.readString() ?: "", parcel.readString() ?: "")

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(uriNext)
            parcel.writeString(uriPrevious)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Paging> {
            override fun createFromParcel(parcel: Parcel): Paging {
                return Paging(parcel)
            }

            override fun newArray(size: Int): Array<Paging?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class ShopItem(
            val id: String = "",
            val name: String = "",
            val domain: String = "",
            val url: String = "",
            val applink: String = "",
            val image: String = "",
            val image300: String = "",
            val description: String = "",
            val tagLine: String = "",
            val location: String = "",
            val totalTransaction: String = "",
            val totalFavorite: String = "",
            val goldShop: Int = 0,
            val isOwner: Int = 0,
            val rateSpeed: Int = 0,
            val rateAccuracy: Int = 0,
            val rateService: Int = 0,
            val status: Int = 0,
            val productList: List<ShopItemProduct> = listOf(),
            val voucher: ShopItemVoucher = ShopItemVoucher(),
            val lucky: String = "",
            val reputationImageUri: String = "",
            val reputationScore: Int = 0,
            val isOfficial: Boolean = false,
            val gaKey: String = "",
            val isRecommendation: Boolean = false
    ) : Parcelable, Visitable<ShopListTypeFactory> {

        var position: Int = 0

        val isGoldShop = goldShop == KEY_SHOP_IS_GOLD

        val isClosed = status == KEY_SHOP_STATUS_CLOSED

        val isModerated = status == KEY_SHOP_STATUS_MODERATED

        val isInactive = status == KEY_SHOP_STATUS_INACTIVE

        fun getShopAsObjectDataLayer(): Any {
            return DataLayer.mapOf(
                    "id", id,
                    "name", "/search result - shop",
                    "creative", name,
                    "position", position
            )
        }

        fun getShopRecommendationAsObjectDataLayer(): Any {
            return DataLayer.mapOf(
                    "id", id,
                    "name", "/search result - shop - alternative",
                    "creative", name,
                    "position", position
            )
        }

        override fun type(typeFactory: ShopListTypeFactory?): Int {
            return typeFactory?.type(this) ?: 0
        }

        override fun toString(): String {
            return "ShopItem id = $id, position = $position"
        }

        data class ShopItemProduct(
                val id: Int = 0,
                val name: String = "",
                val url: String = "",
                val applink: String = "",
                val price: Int = 0,
                val priceFormat: String = "",
                val imageUrl: String = "",
                val isRecommendation: Boolean = false
        ) : Parcelable {

            var position: Int = 0

            fun getShopProductPreviewAsObjectDataLayerList(): Any {
                return DataLayer.mapOf(
                        "name", name,
                        "id", id,
                        "price", price,
                        "brand", "none / other",
                        "category", "none / other",
                        "variant", "none / other",
                        "list", "/searchproduct - shop product list",
                        "position", position
                )
            }

            fun getShopProductPreviewAsObjectDataLayer(): Any {
                return DataLayer.mapOf(
                        "name", name,
                        "id", id,
                        "price", price,
                        "brand", "none / other",
                        "category", "none / other",
                        "variant", "none / other",
                        "position", position
                )
            }

            fun getShopRecommendationProductPreviewAsObjectDataLayerList(): Any {
                return DataLayer.mapOf(
                        "name", name,
                        "id", id,
                        "price", price,
                        "brand", "none / other",
                        "category", "none / other",
                        "variant", "none / other",
                        "list", "/searchproduct - shop product list",
                        "position", position
                )
            }

            fun getShopRecommendationProductPreviewAsObjectDataLayer(): Any {
                return DataLayer.mapOf(
                        "name", name,
                        "id", id,
                        "price", price,
                        "brand", "none / other",
                        "category", "none / other",
                        "variant", "none / other",
                        "list", "/searchproduct - shop product list",
                        "position", position,
                        "attribution", "none / other"
                )
            }

            constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readInt(),
                    parcel.readString() ?: "",
                    parcel.readString() ?: "") {
                position = parcel.readInt()
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(name)
                parcel.writeString(url)
                parcel.writeString(applink)
                parcel.writeInt(price)
                parcel.writeString(priceFormat)
                parcel.writeString(imageUrl)
                parcel.writeInt(position)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<ShopItemProduct> {
                override fun createFromParcel(parcel: Parcel): ShopItemProduct {
                    return ShopItemProduct(parcel)
                }

                override fun newArray(size: Int): Array<ShopItemProduct?> {
                    return arrayOfNulls(size)
                }
            }
        }

        data class ShopItemVoucher(
                val freeShipping: Boolean = false,
                val cashback: ShopItemVoucherCashback = ShopItemVoucherCashback()
        ) : Parcelable {

            data class ShopItemVoucherCashback(
                    val cashbackValue: Int = 0,
                    val isPercentage: Boolean = false
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                        parcel.readInt(),
                        parcel.readByte() != 0.toByte())

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(cashbackValue)
                    parcel.writeByte(if (isPercentage) 1 else 0)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<ShopItemVoucherCashback> {
                    override fun createFromParcel(parcel: Parcel): ShopItemVoucherCashback {
                        return ShopItemVoucherCashback(parcel)
                    }

                    override fun newArray(size: Int): Array<ShopItemVoucherCashback?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            constructor(parcel: Parcel) : this(
                    parcel.readByte() != 0.toByte(),
                    parcel.readParcelable(ShopItemVoucherCashback::class.java.classLoader) ?: ShopItemVoucherCashback())

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeByte(if (freeShipping) 1 else 0)
                parcel.writeParcelable(cashback, flags)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<ShopItemVoucher> {
                override fun createFromParcel(parcel: Parcel): ShopItemVoucher {
                    return ShopItemVoucher(parcel)
                }

                override fun newArray(size: Int): Array<ShopItemVoucher?> {
                    return arrayOfNulls(size)
                }
            }
        }

        constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.createTypedArrayList(ShopItemProduct) ?: listOf(),
                parcel.readParcelable(ShopItemVoucher::class.java.classLoader) ?: ShopItemVoucher(),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readInt(),
                parcel.readByte() != 0.toByte(),
                parcel.readString() ?: "") {
            position = parcel.readInt()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(name)
            parcel.writeString(domain)
            parcel.writeString(url)
            parcel.writeString(applink)
            parcel.writeString(image)
            parcel.writeString(image300)
            parcel.writeString(description)
            parcel.writeString(tagLine)
            parcel.writeString(location)
            parcel.writeString(totalTransaction)
            parcel.writeString(totalFavorite)
            parcel.writeInt(goldShop)
            parcel.writeInt(isOwner)
            parcel.writeInt(rateSpeed)
            parcel.writeInt(rateAccuracy)
            parcel.writeInt(rateService)
            parcel.writeInt(status)
            parcel.writeTypedList(productList)
            parcel.writeParcelable(voucher, flags)
            parcel.writeString(lucky)
            parcel.writeString(reputationImageUri)
            parcel.writeInt(reputationScore)
            parcel.writeByte(if (isOfficial) 1 else 0)
            parcel.writeString(gaKey)
            parcel.writeInt(position)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ShopItem> {
            override fun createFromParcel(parcel: Parcel): ShopItem {
                return ShopItem(parcel)
            }

            override fun newArray(size: Int): Array<ShopItem?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readParcelable(Paging::class.java.classLoader) ?: Paging(),
            parcel.readString() ?: "",
            parcel.createTypedArrayList(ShopItem) ?: listOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeString(searchUrl)
        parcel.writeParcelable(paging, flags)
        parcel.writeString(tabName)
        parcel.writeTypedList(shopItemList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopDataView> {
        override fun createFromParcel(parcel: Parcel): ShopDataView {
            return ShopDataView(parcel)
        }

        override fun newArray(size: Int): Array<ShopDataView?> {
            return arrayOfNulls(size)
        }
    }
}