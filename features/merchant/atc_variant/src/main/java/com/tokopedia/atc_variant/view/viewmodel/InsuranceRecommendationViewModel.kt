package com.tokopedia.atc_variant.view.viewmodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_variant.view.adapter.AddToCartVariantAdapterTypeFactory
import com.tokopedia.purchase_platform.common.feature.insurance.InsuranceCartShopsViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InsuranceRecommendationViewModel(

        var cartShopsList: ArrayList<InsuranceCartShopsViewModel> = arrayListOf()

) : Visitable<AddToCartVariantAdapterTypeFactory>, Parcelable {

    override fun type(typeFactory: AddToCartVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}