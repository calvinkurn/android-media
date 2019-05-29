package com.tokopedia.checkout.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.datamodel.newaddresscorner.NewAddressCornerResponse
import com.tokopedia.checkout.domain.mapper.AddressCornerMapper
import com.tokopedia.checkout.domain.datamodel.newaddresscorner.AddressListModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
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
@Inject constructor(val context: Context, val usecase: GraphqlUseCase, val mapper: AddressCornerMapper) {

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
        val gqlRequest = GraphqlRequest(gqlQuery, NewAddressCornerResponse::class.java, param)

        usecase.clearRequest()
        usecase.addRequest(gqlRequest)
        return usecase.getExecuteObservable(null)
                .map { graphqlResponse ->
                    val response: NewAddressCornerResponse? =
                            graphqlResponse.getData(NewAddressCornerResponse::class.java)
                    response ?: throw MessageErrorException(
                            graphqlResponse.getError(NewAddressCornerResponse::class.java)[0].message)
                }
                .map(mapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}