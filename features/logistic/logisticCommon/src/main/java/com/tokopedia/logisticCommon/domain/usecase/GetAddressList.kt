package com.tokopedia.logisticCommon.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.R
import com.tokopedia.logisticCommon.domain.mapper.AddressCornerMapper
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.request.AddressRequest
import com.tokopedia.logisticCommon.domain.response.GetPeopleAddressResponse
import com.tokopedia.network.exception.MessageErrorException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

const val PARAM_ADDRESS_USECASE: String = "input"

/**
 * Created by fajarnuha on 2019-05-21.
 */
class GetAddressCornerUseCase
@Inject constructor(@ApplicationContext val context: Context, val usecase: GraphqlUseCase, val mapper: AddressCornerMapper) {

    fun execute(query: String, prevState: Int?, localChosenAddrId: Int?, isWhitelistChosenAddress: Boolean): Observable<AddressListModel> =
            this.getObservable(query = query, page = 1, isAddress = true, isCorner = false, limit = 10,
                    prevState = prevState, localChosenAddrId = localChosenAddrId, isWhitelistChosenAddress)

    fun getAll(query: String, prevState: Int, localChosenAddrId: Int, isWhitelistChosenAddress: Boolean): Observable<AddressListModel> =
            this.getObservable(query = query, page = 1, isAddress = true, isCorner = false, limit = 0,
                    prevState = prevState, localChosenAddrId = localChosenAddrId, isWhitelistChosenAddress = isWhitelistChosenAddress)

    fun loadMore(query: String, page: Int, prevState: Int?, localChosenAddrId: Int?, isWhitelistChosenAddress: Boolean): Observable<AddressListModel> =
            this.getObservable(query = query, page = page, isAddress = true, isCorner = false, limit = 10,
                    prevState = prevState, localChosenAddrId = localChosenAddrId, isWhitelistChosenAddress = isWhitelistChosenAddress)

    private fun getObservable(query: String, page: Int, isAddress: Boolean, isCorner: Boolean, limit: Int,
                              prevState: Int?, localChosenAddrId: Int?, isWhitelistChosenAddress: Boolean):
            Observable<AddressListModel> {
        val request = AddressRequest(searchKey = query, page = page, showAddress = isAddress,
                showCorner = isCorner, limit = limit, whitelistChosenAddress = isWhitelistChosenAddress, previousState = prevState,
                localStateChosenAddressId = localChosenAddrId)
        val param = mapOf<String, Any>(PARAM_ADDRESS_USECASE to request)
        val gqlQuery = GraphqlHelper.loadRawString(context.resources, R.raw.address_corner)
        val gqlRequest = GraphqlRequest(gqlQuery, GetPeopleAddressResponse::class.java, param)

        usecase.clearRequest()
        usecase.addRequest(gqlRequest)


        return usecase.getExecuteObservable(null)
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
        usecase.unsubscribe()
    }

}