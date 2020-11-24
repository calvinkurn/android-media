package com.tokopedia.logisticaddaddress.domain.mapper

import com.google.android.gms.maps.model.LatLng
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.district_boundary.DistrictBoundaryResponse
import com.tokopedia.logisticaddaddress.domain.model.district_boundary.Geometry
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticCommon.util.getLatLng
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-06-10.
 */
class DistrictBoundaryMapper @Inject constructor() {

    fun map(response: GraphqlResponse?): DistrictBoundaryResponseUiModel {
        var districtBoundaryGeometryUiModel = DistrictBoundaryGeometryUiModel()
        val responseDistrictBoundary: DistrictBoundaryResponse? = response?.getData(DistrictBoundaryResponse::class.java)
        responseDistrictBoundary?.keroGetDistrictBoundaryArray?.let {keroGetDistrictBoundaryArray ->
            keroGetDistrictBoundaryArray.geometry.let {
                districtBoundaryGeometryUiModel = mapGeometry(it)
            }
        }
        return DistrictBoundaryResponseUiModel(districtBoundaryGeometryUiModel)
    }

    private fun mapGeometry(geo: Geometry?): DistrictBoundaryGeometryUiModel {
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