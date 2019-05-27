package com.tokopedia.checkout.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.datamodel.addresscorner.GqlKeroWithAddressResponse
import com.tokopedia.checkout.domain.datamodel.newaddresscorner.Data
import com.tokopedia.checkout.domain.datamodel.newaddresscorner.NewAddressCornerResponse
import com.tokopedia.checkout.view.feature.addressoptions.AddressListModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel
import com.tokopedia.transactiondata.entity.request.AddressRequest
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

const val PARAM_ADDRESS_USECASE: String = "input"
/**
 * Created by fajarnuha on 2019-05-21.
 */
class GetAddressCornerUseCase
@Inject constructor(val context: Context, val usecase: GraphqlUseCase) {

    fun execute(query: String): Observable<AddressListModel> =
            this.getObservable(query = query, page = 1, isAddress = true, isCorner = false)

    fun loadMore(query: String, page: Int): Observable<AddressListModel> =
            this.getObservable(query = query, page = page, isAddress = true, isCorner = false)

    private fun getObservable(query: String, page: Int, isAddress: Boolean, isCorner: Boolean):
            Observable<AddressListModel> {
        val request = AddressRequest(searchKey = query, page = page, showAddress = isAddress,
                showCorner = isCorner)
        val param = mapOf<String, Any>(PARAM_ADDRESS_USECASE to request)
        val gqlQuery = GraphqlHelper.loadRawString(context.resources, R.raw.address_corner)
        val gqlRequest = GraphqlRequest(gqlQuery, GqlKeroWithAddressResponse::class.java, param)

        usecase.clearRequest()
        usecase.addRequest(gqlRequest)
        return usecase.getExecuteObservable(null)
                .map { graphqlResponse -> graphqlResponse.getData<NewAddressCornerResponse>(NewAddressCornerResponse::class.java) }
                .map(mapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private val mapper: (NewAddressCornerResponse) -> AddressListModel = {
        val token = Token()
        token.districtRecommendation = it.keroAddressCorner.token.districtRecommendation
        token.ut = it.keroAddressCorner.token.ut.toInt()

        AddressListModel().apply {
            this.token = token
            this.listAddress = it.keroAddressCorner.data.map(recipientModelMapper)
        }
    }

    private val recipientModelMapper: (Data) -> RecipientAddressModel = {
        RecipientAddressModel().apply {
            this.id = it.addrId.toString()
            this.recipientName = it.receiverName
            this.addressName = it.addrName
            this.street = it.address1
            this.postalCode = it.postalCode
            this.provinceId = it.province.toString()
            this.cityId = it.city.toString()
            this.destinationDistrictId = it.district.toString()
            this.recipientPhoneNumber = it.phone
            this.countryName = it.country
            this.provinceName = it.provinceName
            this.cityName = it.cityName
            this.destinationDistrictName = it.districtName
            this.latitude = it.latitude
            this.longitude = it.longitude
            this.addressStatus = it.status
            this.isCornerAddress = it.isCorner
        }
    }


}