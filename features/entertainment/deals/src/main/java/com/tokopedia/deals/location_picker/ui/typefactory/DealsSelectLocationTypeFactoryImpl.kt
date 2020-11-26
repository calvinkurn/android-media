package com.tokopedia.deals.location_picker.ui.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.location_picker.listener.DealsLocationListener
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.model.visitor.PopularCityModel
import com.tokopedia.deals.location_picker.model.visitor.SectionTitleModel
import com.tokopedia.deals.location_picker.ui.adapter.viewholder.LocationSectionTitleViewHolder
import com.tokopedia.deals.location_picker.ui.adapter.viewholder.PopularCityViewHolder
import com.tokopedia.deals.location_picker.ui.adapter.viewholder.PopularLocationViewHolder

class DealsSelectLocationTypeFactoryImpl (
        private val locationListener: DealsLocationListener
): BaseAdapterTypeFactory(), DealsSelectLocationTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            LocationSectionTitleViewHolder.LAYOUT -> LocationSectionTitleViewHolder(parent)
            PopularLocationViewHolder.LAYOUT -> PopularLocationViewHolder(parent, locationListener)
            PopularCityViewHolder.LAYOUT -> PopularCityViewHolder(parent, locationListener)
            else -> { super.createViewHolder(parent, type) }
        }
    }

    override fun type(viewItem: SectionTitleModel): Int {
        return LocationSectionTitleViewHolder.LAYOUT
    }

    override fun type(viewItem: Location): Int {
        return PopularLocationViewHolder.LAYOUT
    }

    override fun type(viewItem: PopularCityModel): Int {
        return PopularCityViewHolder.LAYOUT
    }
}