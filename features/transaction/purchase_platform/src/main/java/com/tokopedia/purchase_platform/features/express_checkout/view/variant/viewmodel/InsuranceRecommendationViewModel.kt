package com.tokopedia.purchase_platform.features.express_checkout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

data class InsuranceRecommendationViewModel(

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
        val CREATOR = object : Parcelable.Creator<InsuranceRecommendationViewModel> {
            override fun createFromParcel(parcel: Parcel): InsuranceRecommendationViewModel {
                return InsuranceRecommendationViewModel(parcel)
            }

            override fun newArray(size: Int): Array<InsuranceRecommendationViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

}