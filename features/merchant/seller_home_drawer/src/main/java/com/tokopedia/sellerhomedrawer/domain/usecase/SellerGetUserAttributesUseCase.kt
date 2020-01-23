package com.tokopedia.sellerhomedrawer.domain.usecase

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.sellerhomedrawer.data.SellerUserData
import com.tokopedia.sellerhomedrawer.domain.repository.SellerUserAttributesRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SellerGetUserAttributesUseCase(private val userRepository: SellerUserAttributesRepository): UseCase<SellerUserData>() {

    companion object {
        @JvmStatic
        val PARAM_USER_ID = "userID"
        @JvmStatic
        val OPERATION_NAME_VALUE = "ConsumerDrawerData"
    }

    override fun createObservable(requestParams: RequestParams): Observable<SellerUserData> {
        return userRepository.getSellerUserAttributes(requestParams)
    }

    fun getUserAttrParam(loginId: String, query: String): RequestParams {

        val variables = hashMapOf<String, Any>()

        variables.put(GetSellerHomeUserAttributesUseCase.PARAM_USER_ID, loginId.toInt())

        return RequestParams.create().apply {
            putObject(GraphqlHelper.QUERY, query)
            putObject(GraphqlHelper.VARIABLES, variables)
            putObject(GraphqlHelper.OPERATION_NAME, GetSellerHomeUserAttributesUseCase.OPERATION_NAME_VALUE)
        }
    }
}