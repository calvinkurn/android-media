package com.tokopedia.checkout.domain.usecase

import com.tokopedia.checkout.domain.model.cartmultipleshipment.SetShippingAddressData
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class CheckoutGqlUseCase @Inject constructor(@Named(CHECKOUT_MUTATION) private val queryString: String,
                                             private val graphqlUseCase: GraphqlUseCase,
                                             private val schedulers: ExecutorSchedulers) : UseCase<SetShippingAddressData>() {

    companion object {
        const val CHECKOUT_MUTATION = "CHECKOUT_MUTATION"


    }

    override fun createObservable(requestParam: RequestParams): Observable<SetShippingAddressData> {
        return Observable.empty()
//        val params = requestParam.parameters[CHANGE_SHIPPING_ADDRESS_PARAMS] as HashMap<String, Any>
//        val graphqlRequest = GraphqlRequest(queryString, ChangeShippingAddressGqlResponse::class.java, params)
//        graphqlUseCase.clearRequest()
//        graphqlUseCase.addRequest(graphqlRequest)
//        return graphqlUseCase.createObservable(RequestParams.EMPTY)
//                .map {
//                    val gqlResponse = it.getData<ChangeShippingAddressGqlResponse>(ChangeShippingAddressGqlResponse::class.java)
//                    if (gqlResponse != null) {
//                        SetShippingAddressData.Builder()
//                                .success(gqlResponse.changeShippingAddressResponse.dataResponse.success == 1)
//                                .messages(gqlResponse.changeShippingAddressResponse.dataResponse.messages ?: emptyList())
//                                .build()
//                    } else {
//                        throw ResponseErrorException()
//                    }
//                }
//                .subscribeOn(schedulers.io)
//                .observeOn(schedulers.main)
    }

}