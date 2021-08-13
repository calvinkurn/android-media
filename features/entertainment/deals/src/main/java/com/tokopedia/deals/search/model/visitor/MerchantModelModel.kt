package com.tokopedia.deals.search.model.visitor

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.search.ui.typefactory.DealsSearchTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MerchantModelModel (
        var merchantList: List<Brand> = arrayListOf()
) : Parcelable, Visitable<DealsSearchTypeFactory> {

    override fun type(typeFactoryDeals: DealsSearchTypeFactory): Int {
        return typeFactoryDeals.type(this)
    }

}