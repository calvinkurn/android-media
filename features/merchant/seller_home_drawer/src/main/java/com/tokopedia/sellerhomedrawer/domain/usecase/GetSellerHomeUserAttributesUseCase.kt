package com.tokopedia.sellerhomedrawer.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeParamConstant
import com.tokopedia.sellerhomedrawer.data.SellerUserData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class GetSellerHomeUserAttributesUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase,
                                                             @Named(SellerHomeParamConstant.SELLER_DRAWER_DATA) val query: String): UseCase<SellerUserData>() {

    companion object {
        @JvmStatic
        val PARAM_USER_ID = "userID"
        @JvmStatic
        val OPERATION_NAME_VALUE = "SellerDrawerData"
        fun getUserAttrParams(loginId: String, query: String): RequestParams {

            return RequestParams.create().apply {
                putInt(PARAM_USER_ID, loginId.toInt())
            }

        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<SellerUserData> {
        val graphqlRequest = GraphqlRequest(query, SellerUserData::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams)
                .map{ response ->
                    val responseError = response.getError(SellerUserData::class.java)
                    responseError?.let {
                        throw MessageErrorException(it.getOrNull(0)?.message)
                    }
                    response.getData<SellerUserData>(SellerUserData::class.java)
                }
    }



}