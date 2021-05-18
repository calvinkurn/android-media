package com.tokopedia.manageaddress.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.domain.mapper.AddressCornerMapper
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.request.AddressRequest
import com.tokopedia.logisticCommon.domain.response.GetPeopleAddressResponse
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

const val PARAM_CORNER_USECASE: String = "input"

open class GetCornerList
@Inject constructor(@ApplicationContext val context: Context, val graphqlUseCase: GraphqlUseCase, val mapper: AddressCornerMapper) {

    open fun execute(query: String, prevState: Int, localChosenAddrId: Int): Observable<AddressListModel> =
            this.getObservable(query = query, page = 1, isAddress = false, isCorner = true,
                    prevState = prevState, localChosenAddrId = localChosenAddrId)

    fun loadMore(query: String, page: Int, prevState: Int, localChosenAddrId: Int): Observable<AddressListModel> =
            this.getObservable(query = query, page = page, isAddress = false, isCorner = true,
            prevState = prevState, localChosenAddrId = localChosenAddrId)

    private fun getObservable(query: String, page: Int, isAddress: Boolean, isCorner: Boolean,
                              prevState: Int, localChosenAddrId: Int):
            Observable<AddressListModel> {
        val request = AddressRequest(searchKey = query, page = page, showAddress = isAddress,
                showCorner = isCorner, whitelistChosenAddress = true, previousState = prevState,
                localStateChosenAddressId = localChosenAddrId)
        val param = mapOf<String, Any>(PARAM_CORNER_USECASE to request)
        val gqlQuery = GraphqlHelper.loadRawString(context.resources, com.tokopedia.logisticCommon.R.raw.address_corner)
        val gqlRequest = GraphqlRequest(gqlQuery, GetPeopleAddressResponse::class.java, param)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        return graphqlUseCase.getExecuteObservable(null)
                .map { graphqlResponse ->
                    val response: GetPeopleAddressResponse? =
                            graphqlResponse.getData(GetPeopleAddressResponse::class.java)
                    response ?: throw MessageErrorException(
                            graphqlResponse.getError(GetPeopleAddressResponse::class.java)[0].message)
                }
                .map(mapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }


}