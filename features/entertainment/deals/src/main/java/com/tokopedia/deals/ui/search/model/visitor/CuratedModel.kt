package com.tokopedia.deals.ui.search.model.visitor

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.deals.data.entity.Category
import com.tokopedia.deals.ui.search.ui.typefactory.DealsSearchTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CuratedModel(
    var categories: List<Category> = arrayListOf()
) : Parcelable, Visitable<DealsSearchTypeFactory> {

    override fun type(typeFactoryDeals: DealsSearchTypeFactory): Int {
        return typeFactoryDeals.type(this)
    }

}
