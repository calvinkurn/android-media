package com.tokopedia.deals.search.model.visitor

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.deals.search.ui.typefactory.DealsSearchTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SectionTitleModel (
        var title: String = ""
): Parcelable, Visitable<DealsSearchTypeFactory> {

    override fun type(typeFactoryDeals: DealsSearchTypeFactory): Int {
        return typeFactoryDeals.type(this)
    }

}