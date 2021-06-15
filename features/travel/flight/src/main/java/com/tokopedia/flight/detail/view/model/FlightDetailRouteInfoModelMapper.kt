package com.tokopedia.flight.detail.view.model

import com.tokopedia.flight.orderlist.data.cloud.entity.Amenity
import com.tokopedia.flight.orderlist.data.cloud.entity.AmenityEntity
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailRouteInfoViewModel
import com.tokopedia.flight.search.data.cloud.single.Info
import java.util.*
import javax.inject.Inject

/**
 * Created by alvarisi on 12/8/17.
 */
class FlightDetailRouteInfoModelMapper @Inject constructor() {

    private fun transform(info: Info?): FlightDetailRouteInfoModel? {
        var viewModel: FlightDetailRouteInfoModel? = null
        if (info != null) {
            viewModel = FlightDetailRouteInfoModel()
            viewModel.label = info.label
            viewModel.value = info.value
        }
        return viewModel
    }

    private fun transform(info: FlightOrderDetailRouteInfoViewModel?)
            : FlightDetailRouteInfoModel? {
        var viewModel: FlightDetailRouteInfoModel? = null
        if (info != null) {
            viewModel = FlightDetailRouteInfoModel()
            viewModel.label = info.label
            viewModel.value = info.value
        }
        return viewModel
    }

    fun transform(infos: List<Info?>?)
            : List<FlightDetailRouteInfoModel> {
        val viewModels: MutableList<FlightDetailRouteInfoModel> = ArrayList()
        if (infos != null) {
            for (info in infos) {
                val viewModel: FlightDetailRouteInfoModel? = transform(info)
                if (viewModel != null) {
                    viewModels.add(viewModel)
                }
            }
        }
        return viewModels
    }

    fun transformOrderInfo(infos: List<FlightOrderDetailRouteInfoViewModel?>?)
            : List<FlightDetailRouteInfoModel> {
        val viewModels: MutableList<FlightDetailRouteInfoModel> = ArrayList()
        if (infos != null) {
            for (info in infos) {
                val viewModel: FlightDetailRouteInfoModel? = transform(info)
                if (viewModel != null) {
                    viewModels.add(viewModel)
                }
            }
        }
        return viewModels
    }

    fun transform(freeAmenities: AmenityEntity?): List<FlightDetailRouteInfoModel> {
        val routeInfoViewModels: MutableList<FlightDetailRouteInfoModel> = ArrayList()
        if (freeAmenities != null) {
            if (freeAmenities.cabinBaggage.unit.isNotEmpty() &&
                    freeAmenities.cabinBaggage.value.isNotEmpty()) {
                val infoViewModel = FlightDetailRouteInfoModel()
                infoViewModel.label = freeAmenities.cabinBaggage.value
                infoViewModel.value = freeAmenities.cabinBaggage.unit
                routeInfoViewModels.add(infoViewModel)
            }
            if (freeAmenities.freeBaggage.unit.isNotEmpty() &&
                    freeAmenities.freeBaggage.value.isNotEmpty()) {
                val infoViewModel = FlightDetailRouteInfoModel()
                infoViewModel.label = freeAmenities.freeBaggage.value
                infoViewModel.value = freeAmenities.freeBaggage.unit
                routeInfoViewModels.add(infoViewModel)
            }
            if (!freeAmenities.isMeal) {
                val infoViewModel = FlightDetailRouteInfoModel()
                infoViewModel.label = "Meal"
                infoViewModel.value = "-"
                routeInfoViewModels.add(infoViewModel)
            }
            if (!freeAmenities.isUsbPort) {
                val infoViewModel = FlightDetailRouteInfoModel()
                infoViewModel.label = "Usb"
                infoViewModel.value = "-"
                routeInfoViewModels.add(infoViewModel)
            }
            if (!freeAmenities.isWifi) {
                val infoViewModel = FlightDetailRouteInfoModel()
                infoViewModel.label = "Wifi"
                infoViewModel.value = "-"
                routeInfoViewModels.add(infoViewModel)
            }
        }
        return routeInfoViewModels
    }

    fun transformOrderAmenities(amenities: List<Amenity>?): List<com.tokopedia.flight.search.data.cloud.single.Amenity> {
        val dataList: MutableList<com.tokopedia.flight.search.data.cloud.single.Amenity> = ArrayList()
        if (amenities != null) {
            for (item in amenities) {
                dataList.add(transform(item))
            }
        }
        return dataList
    }

    private fun transform(amenity: Amenity): com.tokopedia.flight.search.data.cloud.single.Amenity {
        val data = com.tokopedia.flight.search.data.cloud.single.Amenity()
        data.icon = amenity.icon
        data.label = amenity.label
        return data
    }
}