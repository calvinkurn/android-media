package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.data.entity.request.AddAddressParam
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticCommon.data.response.AddAddressResponse
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import javax.inject.Inject

class AddAddressUseCase
@Inject constructor(val gql: GraphqlUseCase, val scheduler: SchedulerProvider) {

    fun execute(model: SaveAddressDataModel, formType: String): Observable<AddAddressResponse> {
        val param = AddAddressParam(
                model.addressName, model.receiverName, model.address1, model.address2,
                model.postalCode, model.phone, model.provinceId.toString(), model.cityId.toString(),
                model.districtId.toString(), model.latitude, model.longitude, formType,
                apply_name_as_new_user_fullname = model.applyNameAsNewUserFullname,
                set_as_primary_address = model.setAsPrimaryAddresss

        )
        val gqlParam = mapOf("input" to param.toMap())
        val gqlRequest = GraphqlRequest(KeroLogisticQuery.kero_add_address_query,
                AddAddressResponse::class.java, gqlParam)
        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { response ->
                    val gqlResponse: AddAddressResponse? =
                            response.getData(AddAddressResponse::class.java)
                    gqlResponse ?: throw MessageErrorException(
                            response.getError(AddAddressResponse::class.java)[0].message
                    )
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
    }

    fun unsubscribe() {
        gql.unsubscribe()
    }
}
