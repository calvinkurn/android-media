package com.tokopedia.logisticaddaddress.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.model.add_address.AddAddressResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-28.
 */
class AddAddressUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {
    var queryString: String = ""

    companion object {
        val PARAM_ADDRESS_NAME = "#addrName"
        val PARAM_RECEIVER_NAME = "#receiverName"
        val PARAM_ADDRESS_1 = "#address1"
        val PARAM_ADDRESS_2 = "#address2"
        val PARAM_POSTAL_CODE = "#postalCode"
        val PARAM_PHONE_NO = "#phoneNo"
        val PARAM_CITY_ID = "#cityId"
        val PARAM_PROVINCE_ID = "#provinceId"
        val PARAM_DISTRICT_ID = "#districtId"
        val PARAM_LAT = "#lat"
        val PARAM_LONG = "#long"
    }

    fun setParams(saveAddressDataModel: SaveAddressDataModel) {
        queryString = GraphqlHelper.loadRawString(context.resources, R.raw.add_address)
        queryString = queryString.replace(PARAM_ADDRESS_NAME, saveAddressDataModel.addressName, false)
        queryString = queryString.replace(PARAM_RECEIVER_NAME, saveAddressDataModel.receiverName, false)
        queryString = queryString.replace(PARAM_ADDRESS_1, saveAddressDataModel.address1, false)
        queryString = queryString.replace(PARAM_ADDRESS_2, saveAddressDataModel.address2, false)
        queryString = queryString.replace(PARAM_POSTAL_CODE, saveAddressDataModel.postalCode, false)
        queryString = queryString.replace(PARAM_PHONE_NO, saveAddressDataModel.phone, false)
        queryString = queryString.replace(PARAM_CITY_ID, saveAddressDataModel.cityId.toString(), false)
        queryString = queryString.replace(PARAM_PROVINCE_ID, saveAddressDataModel.provinceId.toString(), false)
        queryString = queryString.replace(PARAM_DISTRICT_ID, saveAddressDataModel.districtId.toString(), false)
        queryString = queryString.replace(PARAM_LAT, saveAddressDataModel.latitude, false)
        queryString = queryString.replace(PARAM_LONG, saveAddressDataModel.longitude, false)
    }

    override fun createObservable(params: RequestParams?): Observable<GraphqlResponse> {
        val graphqlRequest = GraphqlRequest(queryString, AddAddressResponse::class.java)
        clearRequest()
        addRequest(graphqlRequest)

        return super.createObservable(params)
    }
}