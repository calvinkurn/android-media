package com.tokopedia.deals.location_picker.ui.typefactory

import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.model.visitor.PopularCityModel
import com.tokopedia.deals.location_picker.model.visitor.SectionTitleModel

interface DealsSelectLocationTypeFactory {
    fun type(viewItem: SectionTitleModel): Int
    fun type(viewItem: Location): Int
    fun type(viewItem: PopularCityModel): Int
}