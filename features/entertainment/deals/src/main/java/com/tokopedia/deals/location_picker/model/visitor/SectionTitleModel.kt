package com.tokopedia.deals.location_picker.model.visitor

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.deals.location_picker.ui.typefactory.DealsSelectLocationTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SectionTitleModel (
        var title: String = ""
): Parcelable, Visitable<DealsSelectLocationTypeFactory> {

    override fun type(typeFactoryDeals: DealsSelectLocationTypeFactory): Int {
        return typeFactoryDeals.type(this)
    }

}