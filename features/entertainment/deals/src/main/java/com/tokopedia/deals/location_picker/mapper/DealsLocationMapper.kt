package com.tokopedia.deals.location_picker.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.model.visitor.PopularCityModel
import com.tokopedia.deals.location_picker.model.visitor.SectionTitleModel
import com.tokopedia.deals.location_picker.ui.typefactory.DealsSelectLocationTypeFactory

object DealsLocationMapper {

    private const val POPULAR_CITY_TITLE = "KOTA POPULER"
    private const val POPULAR_LOCATION_TITLE = "LOKASI POPULER"

    var listPopularCities: ArrayList<Location> = arrayListOf()
    var listPopularLocation: ArrayList<Location> = arrayListOf()

    fun displayInitialStateLocationBottomSheet(): ArrayList<Visitable<DealsSelectLocationTypeFactory>> {
        val list: ArrayList<Visitable<DealsSelectLocationTypeFactory>> = arrayListOf()
        if(listPopularCities.isNotEmpty()) {
            val popularCitiesTitle = SectionTitleModel(POPULAR_CITY_TITLE)
            list.add(popularCitiesTitle)

            val popularCities = PopularCityModel()
            popularCities.cityList.addAll(listPopularCities)
            list.add(popularCities)
        }

        if(listPopularLocation.isNotEmpty()) {
            val popularLocationTitle = SectionTitleModel(POPULAR_LOCATION_TITLE)
            list.add(popularLocationTitle)

            list.addAll(listPopularLocation)
        }

        return list
    }

    fun displayLoadMoreLocationBottomSheet(morePopularCities: List<Location>): ArrayList<Visitable<DealsSelectLocationTypeFactory>> {
        val list: ArrayList<Visitable<DealsSelectLocationTypeFactory>> = arrayListOf()
        list.addAll(morePopularCities)
        return list
    }

    fun displayInitialStateLandmarkLocationBottomSheet(landmarkLocationlist: List<Location>): ArrayList<Visitable<DealsSelectLocationTypeFactory>> {
        val list: ArrayList<Visitable<DealsSelectLocationTypeFactory>> = arrayListOf()
        list.addAll(landmarkLocationlist)
        return list
    }

    fun displayMoreLandmarkLocationBottomSheet(moreLandmarkLocationlist: List<Location>): ArrayList<Visitable<DealsSelectLocationTypeFactory>> {
        val list: ArrayList<Visitable<DealsSelectLocationTypeFactory>> = arrayListOf()

        if(moreLandmarkLocationlist.isNotEmpty()) {
            list.addAll(moreLandmarkLocationlist)
        }

        return list
    }
}