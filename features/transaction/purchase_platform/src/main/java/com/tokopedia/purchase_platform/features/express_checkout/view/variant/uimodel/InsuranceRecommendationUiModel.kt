package com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.common.view.model.InsuranceCartShopsViewModel
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

data class InsuranceRecommendationUiModel(

        var cartShopsList: ArrayList<InsuranceCartShopsViewModel>

) : Visitable<CheckoutVariantAdapterTypeFactory>, Parcelable {

    constructor(parcel: Parcel? = null) : this(
            arrayListOf<InsuranceCartShopsViewModel>().apply {
                parcel?.readList(this, InsuranceCartShopsViewModel::class.java.classLoader)
            }
    )

    override fun type(typeFactory: CheckoutVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(cartShopsList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<InsuranceRecommendationUiModel> {
            override fun createFromParcel(parcel: Parcel): InsuranceRecommendationUiModel {
                return InsuranceRecommendationUiModel(parcel)
            }

            override fun newArray(size: Int): Array<InsuranceRecommendationUiModel?> {
                return arrayOfNulls(size)
            }
        }
    }

}