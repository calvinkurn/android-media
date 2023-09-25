package com.tokopedia.logisticaddaddress.domain.mapper

import com.google.android.gms.maps.model.LatLng
import com.tokopedia.logisticCommon.data.response.GetDistrictBoundaryResponse
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictBoundaryResponseUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-06-10.
 */
class DistrictBoundaryMapper @Inject constructor() {

    fun mapDistrictBoundaryNew(response: GetDistrictBoundaryResponse): DistrictBoundaryResponseUiModel {
        var districtBoundaryGeometryUiModel = DistrictBoundaryGeometryUiModel()
        response?.keroGetDistrictBoundaryArray?.let { keroGetDistrictBoundaryArray ->
            keroGetDistrictBoundaryArray.geometry.let {
                districtBoundaryGeometryUiModel = mapGeometryNew(it)
            }
        }
        return DistrictBoundaryResponseUiModel(districtBoundaryGeometryUiModel)
    }

    private fun mapGeometryNew(geo: com.tokopedia.logisticCommon.data.response.Geometry?): DistrictBoundaryGeometryUiModel {
        val listCoordinates = mutableListOf<LatLng>()

        geo?.coordinates?.forEach { it ->
            it.forEach {
                it.forEach {
                    listCoordinates.add(getLatLng(it[1], it[0]))
                }
            }
        }
        return DistrictBoundaryGeometryUiModel(listCoordinates)
    }
}
