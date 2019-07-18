package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable

data class InsuranceCartShopsViewModel(

        var shopId: Long,
        var shopItemsList: ArrayList<InsuranceCartShopItemsViewModel> = ArrayList()

) : Parcelable {

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<InsuranceCartShopsViewModel> {

            override fun createFromParcel(parcel: Parcel): InsuranceCartShopsViewModel {
                return InsuranceCartShopsViewModel(parcel)
            }

            override fun newArray(size: Int): Array<InsuranceCartShopsViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readLong() ?: 0,
            arrayListOf<InsuranceCartShopItemsViewModel>().apply {
                parcel?.readList(this, InsuranceCartShopItemsViewModel::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(shopId)
        parcel.writeList(shopItemsList)
    }

    override fun describeContents(): Int {
        return 0
    }
}

data class InsuranceCartShopItemsViewModel(

        var productId: Long,
        var digitalProductList: ArrayList<InsuranceCartDigitalProductViewModel> = ArrayList()

) : Parcelable {

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<InsuranceCartShopItemsViewModel> {

            override fun createFromParcel(parcel: Parcel): InsuranceCartShopItemsViewModel {
                return InsuranceCartShopItemsViewModel(parcel)
            }

            override fun newArray(size: Int): Array<InsuranceCartShopItemsViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readLong() ?: 0,
            arrayListOf<InsuranceCartDigitalProductViewModel>().apply {
                parcel?.readList(this, InsuranceCartDigitalProductViewModel::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(productId)
        parcel.writeList(digitalProductList)
    }

    override fun describeContents(): Int {
        return 0
    }
}

data class InsuranceCartDigitalProductViewModel(
        var digitalProductId: Long,
        var cartItemId: Long,
        var typeId: Long,
        var pricePerProduct: Long,
        var totalPrice: Long,
        var optIn: Boolean,
        var isProductLevel: Boolean,
        var isPurchaseProtection: Boolean,
        var isSellerMoney: Boolean,
        var isApplicationNeeded: Boolean,
        var isNew: Boolean,
        var productInfo: InsuranceCartProductInfoViewModel = InsuranceCartProductInfoViewModel(),
        var applicationDetails: ArrayList<InsuranceProductApplicationDetailsViewModel> = ArrayList()

) : Parcelable {
    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<InsuranceCartDigitalProductViewModel> {

            override fun createFromParcel(parcel: Parcel): InsuranceCartDigitalProductViewModel {
                return InsuranceCartDigitalProductViewModel(parcel)
            }

            override fun newArray(size: Int): Array<InsuranceCartDigitalProductViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readLong() ?: 0,
            parcel?.readLong() ?: 0,
            parcel?.readLong() ?: 0,
            parcel?.readLong() ?: 0,
            parcel?.readLong() ?: 0,
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readParcelable<InsuranceCartProductInfoViewModel>(InsuranceCartProductInfoViewModel::class.java.classLoader)!!,
            arrayListOf<InsuranceProductApplicationDetailsViewModel>().apply {
                parcel?.readList(this, InsuranceProductApplicationDetailsViewModel::class.java.classLoader)
            }

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(digitalProductId)
        parcel.writeLong(cartItemId)
        parcel.writeLong(typeId)
        parcel.writeLong(pricePerProduct)
        parcel.writeLong(totalPrice)
        parcel.writeByte(if (optIn) 1 else 0)
        parcel.writeByte(if (isProductLevel) 1 else 0)
        parcel.writeByte(if (isPurchaseProtection) 1 else 0)
        parcel.writeByte(if (isSellerMoney) 1 else 0)
        parcel.writeByte(if (isApplicationNeeded) 1 else 0)
        parcel.writeByte(if (isNew) 1 else 0)
        parcel.writeParcelable(productInfo, flags)
        parcel.writeList(applicationDetails)

    }

    override fun describeContents(): Int {
        return 0
    }
}

data class InsuranceCartProductInfoViewModel(
        var title: String,
        var subTitle: String,
        var description: String,
        var iconUrl: String,
        var tickerText: String,
        var detailInfoTitle: String,
        var sectionTitle: String,
        var webLinkUrl: String,
        var infoText: String,
        var appLinkUrl: String,
        var linkName: String

) : Parcelable {
    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<InsuranceCartProductInfoViewModel> {

            override fun createFromParcel(parcel: Parcel): InsuranceCartProductInfoViewModel {
                return InsuranceCartProductInfoViewModel(parcel)
            }

            override fun newArray(size: Int): Array<InsuranceCartProductInfoViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subTitle)
        parcel.writeString(description)
        parcel.writeString(iconUrl)
        parcel.writeString(tickerText)
        parcel.writeString(detailInfoTitle)
        parcel.writeString(sectionTitle)
        parcel.writeString(webLinkUrl)
        parcel.writeString(infoText)
        parcel.writeString(appLinkUrl)
        parcel.writeString(linkName)
    }

    override fun describeContents(): Int {
        return 0
    }
}

data class InsuranceProductApplicationDetailsViewModel(
        var id: Int,
        var label: String,
        var placeHolder: String,
        var type: String,
        var isRequired: Boolean,
        var isError: Boolean = false,
        var value: String,
        var valuesList: ArrayList<InsuranceApplicationValueViewModel> = ArrayList(),
        var validationsList: ArrayList<InsuranceApplicationValidationViewModel> = ArrayList()
) : Parcelable {
    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<InsuranceProductApplicationDetailsViewModel> {

            override fun createFromParcel(parcel: Parcel): InsuranceProductApplicationDetailsViewModel {
                return InsuranceProductApplicationDetailsViewModel(parcel)
            }

            override fun newArray(size: Int): Array<InsuranceProductApplicationDetailsViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readInt() ?: 0,

            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",

            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readString() ?: "",

            arrayListOf<InsuranceApplicationValueViewModel>().apply {
                parcel?.readList(this, InsuranceApplicationValueViewModel::class.java.classLoader)
            },
            arrayListOf<InsuranceApplicationValidationViewModel>().apply {
                parcel?.readList(this, InsuranceApplicationValidationViewModel::class.java.classLoader)
            })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(label)
        parcel.writeString(placeHolder)
        parcel.writeString(type)
        parcel.writeByte(if (isRequired) 1 else 0)
        parcel.writeByte(if (isError) 1 else 0)

        parcel.writeString(value)
        parcel.writeList(valuesList)
        parcel.writeList(validationsList)
    }

    override fun describeContents(): Int {
        return 0
    }
}

data class InsuranceApplicationValueViewModel(
        var valuesId: Int,
        var value: String
) : Parcelable {

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<InsuranceApplicationValueViewModel> {

            override fun createFromParcel(parcel: Parcel): InsuranceApplicationValueViewModel {
                return InsuranceApplicationValueViewModel(parcel)
            }

            override fun newArray(size: Int): Array<InsuranceApplicationValueViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(valuesId)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }
}

data class InsuranceApplicationValidationViewModel(
        var validationId: Int,
        var type: String,
        var validationValue: String,
        var validationErrorMessage: String
) : Parcelable {
    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<InsuranceApplicationValidationViewModel> {

            override fun createFromParcel(parcel: Parcel): InsuranceApplicationValidationViewModel {
                return InsuranceApplicationValidationViewModel(parcel)
            }

            override fun newArray(size: Int): Array<InsuranceApplicationValidationViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(validationId)
        parcel.writeString(type)
        parcel.writeString(validationValue)
        parcel.writeString(validationErrorMessage)

    }

    override fun describeContents(): Int {
        return 0
    }
}