package com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.data.model.response.addresscorner.NewAddressCornerResponse
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.mapper.corner.AddressCornerMapper
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.model.AddressListModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.data.model.request.AddressRequest
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

const val PARAM_CORNER_USECASE: String = "input"

open class GetCornerList
@Inject constructor(@ApplicationContext val context: Context, val graphqlUseCase: GraphqlUseCase, val mapper: AddressCornerMapper) {

    open fun execute(query: String): Observable<AddressListModel> =
            this.getObservable(query = query, page = 1, isAddress = false, isCorner = true)

    fun loadMore(query: String, page: Int): Observable<AddressListModel> =
            this.getObservable(query = query, page = page, isAddress = false, isCorner = true)

    private fun getObservable(query: String, page: Int, isAddress: Boolean, isCorner: Boolean):
            Observable<AddressListModel> {
        val request = AddressRequest(searchKey = query, page = page, showAddress = isAddress,
                showCorner = isCorner)
        val param = mapOf<String, Any>(PARAM_CORNER_USECASE to request)
        val gqlQuery = GraphqlHelper.loadRawString(context.resources, R.raw.address_corner)
        val gqlRequest = GraphqlRequest(gqlQuery, NewAddressCornerResponse::class.java, param)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        return graphqlUseCase.getExecuteObservable(null)
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

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }


}