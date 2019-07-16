package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.search.result.presentation.view.typefactory.ShopListTypeFactory

data class ShopViewModelKt(
    val source: String = "",
    val totalShop: Int = 0,
    val searchUrl: String = "",
    val paging: Paging = Paging(),
    val tabName: String = "",
    val shopItemList: List<ShopItem> = listOf(),
    val topSellerData: List<ShopItem> = listOf(),
        val topOfficialSellerData: List<ShopItem> = listOf()
): Parcelable {

    data class Paging(
        val uriNext: String = "",
        val uriPrevious: String = ""
    ): Parcelable {
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
        val shopId: String = "",
        val shopName: String = "",
        val shopDomain: String = "",
        val shopUrl: String = "",
        val shopApplink: String = "",
        val shopImage: String = "",
        val shopImage300: String = "",
        val shopDescription: String = "",
        val shopTagLine: String = "",
        val shopLocation: String = "",
        val shopTotalTransaction: String = "",
        val shopTotalFavorite: String = "",
        val shopGoldShop: Int = 0,
        val shopIsOwner: Int = 0,
        val shopRateSpeed: Int = 0,
        val shopRateAccuracy: Int = 0,
        val shopRateService: Int = 0,
        val shopStatus: Int = 0,
        val shopItemProductList: List<ShopItemProduct> = listOf(),
        val voucher: ShopItemVoucher = ShopItemVoucher(),
        val shopLucky: String = "",
        val reputationImageUri: String = "",
        val reputationScore: Int = 0,
        val isOfficial: Boolean = false,
        val gaKey: String = ""
    ): Parcelable, Visitable<ShopListTypeFactory> {

        var position : Int = 0

        fun getPage(): Int {
            return (position - 1) / Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS) + 1
        }

        override fun type(typeFactory: ShopListTypeFactory?): Int {
            return typeFactory?.type(this) ?: 0
        }

        data class ShopItemProduct(
            val id: Int = 0,
            val name: String = "",
            val url: String = "",
            val applink: String = "",
            val price: Int = 0,
            val imageUrl: String = ""
        ): Parcelable {

            constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readInt(),
                    parcel.readString() ?: "")

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(name)
                parcel.writeString(url)
                parcel.writeString(applink)
                parcel.writeInt(price)
                parcel.writeString(imageUrl)
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
        ): Parcelable {

            data class ShopItemVoucherCashback(
                val cashbackValue: Int = 0,
                val isPercentage: Boolean = false
            ): Parcelable {
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
                    parcel.readParcelable(ShopItemVoucherCashback::class.java.classLoader))

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
                parcel.createTypedArrayList(ShopItemProduct),
                parcel.readParcelable(ShopItemVoucher::class.java.classLoader),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readInt(),
                parcel.readByte() != 0.toByte(),
                parcel.readString() ?: "")

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(shopId)
            parcel.writeString(shopName)
            parcel.writeString(shopDomain)
            parcel.writeString(shopUrl)
            parcel.writeString(shopApplink)
            parcel.writeString(shopImage)
            parcel.writeString(shopImage300)
            parcel.writeString(shopDescription)
            parcel.writeString(shopTagLine)
            parcel.writeString(shopLocation)
            parcel.writeString(shopTotalTransaction)
            parcel.writeString(shopTotalFavorite)
            parcel.writeInt(shopGoldShop)
            parcel.writeInt(shopIsOwner)
            parcel.writeInt(shopRateSpeed)
            parcel.writeInt(shopRateAccuracy)
            parcel.writeInt(shopRateService)
            parcel.writeInt(shopStatus)
            parcel.writeTypedList(shopItemProductList)
            parcel.writeParcelable(voucher, flags)
            parcel.writeString(shopLucky)
            parcel.writeString(reputationImageUri)
            parcel.writeInt(reputationScore)
            parcel.writeByte(if (isOfficial) 1 else 0)
            parcel.writeString(gaKey)
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
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readParcelable(Paging::class.java.classLoader),
            parcel.readString() ?: "",
            parcel.createTypedArrayList(ShopItem),
            parcel.createTypedArrayList(ShopItem),
            parcel.createTypedArrayList(ShopItem))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeInt(totalShop)
        parcel.writeString(searchUrl)
        parcel.writeParcelable(paging, flags)
        parcel.writeString(tabName)
        parcel.writeTypedList(shopItemList)
        parcel.writeTypedList(topSellerData)
        parcel.writeTypedList(topOfficialSellerData)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopViewModelKt> {
        override fun createFromParcel(parcel: Parcel): ShopViewModelKt {
            return ShopViewModelKt(parcel)
        }

        override fun newArray(size: Int): Array<ShopViewModelKt?> {
            return arrayOfNulls(size)
        }
    }
}