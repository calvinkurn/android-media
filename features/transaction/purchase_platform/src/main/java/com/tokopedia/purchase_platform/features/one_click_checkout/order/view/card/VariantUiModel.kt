package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.product.detail.common.data.model.variant.Variant

sealed class VariantUiModel

data class SelectedTypeVariantUiModel(
        var selectedList: ArrayList<TypeVariantUiModel> = ArrayList()
) : VariantUiModel()

data class TypeVariantUiModel(
        var variantId: Int,
        var variantName: String,
        var variantSelectedValue: String,
        var variantGuideline: String,
        var variantIdentifier: String,
        var variantOptions: ArrayList<OptionVariantUiModel>
) : VariantUiModel(), Parcelable {

    fun getSelectedOption(): Int? {
        return variantOptions.find { it.currentState == OptionVariantUiModel.STATE_SELECTED }?.optionId
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            arrayListOf<OptionVariantUiModel>().apply {
                parcel?.readList(this, OptionVariantUiModel::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(variantId)
        parcel.writeString(variantName)
        parcel.writeString(variantSelectedValue)
        parcel.writeString(variantGuideline)
        parcel.writeString(variantIdentifier)
        parcel.writeList(variantOptions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<TypeVariantUiModel> {
            override fun createFromParcel(parcel: Parcel): TypeVariantUiModel {
                return TypeVariantUiModel(parcel)
            }

            override fun newArray(size: Int): Array<TypeVariantUiModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    val isSizeIdentifier: Boolean
        get() = Variant.SIZE.equals(variantIdentifier, false)
    val isColorIdentifier: Boolean
        get() = Variant.COLOR.equals(variantIdentifier, false)

}

data class OptionVariantUiModel(
        var variantId: Int,
        var optionId: Int,
        var currentState: Int,
        var variantHex: String,
        var variantName: String,
        var hasAvailableChild: Boolean
) : Parcelable {

    companion object {
        val STATE_SELECTED = 1
        val STATE_NOT_SELECTED = 0
        val STATE_NOT_AVAILABLE = -1

        @JvmField
        val CREATOR = object : Parcelable.Creator<com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel> {

            override fun createFromParcel(parcel: Parcel): com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel {
                return OptionVariantUiModel(parcel)
            }

            override fun newArray(size: Int): Array<com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel? = null) : this(
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(variantId)
        parcel.writeInt(optionId)
        parcel.writeInt(currentState)
        parcel.writeString(variantHex)
        parcel.writeString(variantName)
        parcel.writeByte(if (hasAvailableChild) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

}