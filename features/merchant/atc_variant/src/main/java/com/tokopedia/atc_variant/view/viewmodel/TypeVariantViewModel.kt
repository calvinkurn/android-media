package com.tokopedia.atc_variant.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_variant.view.adapter.AddToCartVariantAdapterTypeFactory
import com.tokopedia.atc_variant.view.viewmodel.OptionVariantViewModel.Companion.STATE_SELECTED
import com.tokopedia.product.detail.common.data.model.variant.Variant.Companion.COLOR
import com.tokopedia.product.detail.common.data.model.variant.Variant.Companion.SIZE
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 30/11/18.
 */
@Parcelize
data class TypeVariantViewModel(
        var variantId: Int = 0,
        var variantName: String = "",
        var variantSelectedValue: String = "",
        var variantGuideline: String = "",
        var variantIdentifier: String = "",
        var variantOptions: ArrayList<OptionVariantViewModel> = arrayListOf()
) : Visitable<AddToCartVariantAdapterTypeFactory>, Parcelable {

    fun getSelectedOption(): Int? {
        return variantOptions.find { it.currentState == STATE_SELECTED }?.optionId
    }

    override fun type(typeFactory: AddToCartVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    val isSizeIdentifier: Boolean
        get() = SIZE.equals(variantIdentifier, false)
    val isColorIdentifier: Boolean
        get() = COLOR.equals(variantIdentifier, false)

}