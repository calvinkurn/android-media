package com.tokopedia.logisticCommon.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
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
const val PARAM_ADDRESS_LIMIT: Int = 10

/**
 * Created by fajarnuha on 2019-05-21.
 */
class GetAddressCornerUseCase
@Inject constructor(
    @ApplicationContext val context: Context,
    val usecase: GraphqlUseCaseInterface,
    val mapper: AddressCornerMapper
) {

    fun execute(
        query: String,
        prevState: Int?,
        localChosenAddrId: Long?,
        isWhitelistChosenAddress: Boolean,
        excludeSharedAddress: Boolean = false
    ): Observable<AddressListModel> =
        this.getObservable(
            query = query,
            page = 1,
            prevState = prevState,
            localChosenAddrId = localChosenAddrId,
            isWhitelistChosenAddress = isWhitelistChosenAddress,
            excludeSharedAddress = excludeSharedAddress
        )

    fun loadMore(
        query: String,
        page: Int,
        prevState: Int?,
        localChosenAddrId: Long?,
        isWhitelistChosenAddress: Boolean,
        excludeSharedAddress: Boolean = false
    ): Observable<AddressListModel> =
        this.getObservable(
            query = query,
            page = page,
            prevState = prevState,
            localChosenAddrId = localChosenAddrId,
            isWhitelistChosenAddress = isWhitelistChosenAddress,
            excludeSharedAddress = excludeSharedAddress
        )

    private fun getObservable(
        query: String,
        page: Int,
        prevState: Int?,
        localChosenAddrId: Long?,
        isWhitelistChosenAddress: Boolean,
        excludeSharedAddress: Boolean
    ): Observable<AddressListModel> {
        val request = AddressRequest(
            searchKey = query,
            page = page,
            showAddress = true,
            showCorner = false,
            limit = PARAM_ADDRESS_LIMIT,
            whitelistChosenAddress = isWhitelistChosenAddress,
            previousState = prevState,
            localStateChosenAddressId = localChosenAddrId,
            excludeSharedAddress = excludeSharedAddress
        )
        val param = mapOf<String, Any>(PARAM_ADDRESS_USECASE to request)
        val gqlRequest = GraphqlRequest(
            KeroLogisticQuery.addressCorner,
            GetPeopleAddressResponse::class.java,
            param
        )

        usecase.clearRequest()
        usecase.addRequest(gqlRequest)


        return usecase.getExecuteObservable(null)
            .map { graphqlResponse ->
                val response: GetPeopleAddressResponse? =
                    graphqlResponse.getData(GetPeopleAddressResponse::class.java)
                response ?: throw MessageErrorException(
                    graphqlResponse.getError(GetPeopleAddressResponse::class.java)[0].message
                )
            }
            .map(mapper)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun unsubscribe() {
        usecase.unsubscribe()
    }

}
