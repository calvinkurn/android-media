package com.tokopedia.sellerhomedrawer.domain.usecase

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.sellerhomedrawer.data.SellerUserData
import com.tokopedia.sellerhomedrawer.domain.repository.SellerUserAttributesRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetSellerHomeUserAttributesUseCase(val sellerUserRepository: SellerUserAttributesRepository): UseCase<SellerUserData>() {

    companion object {
        @JvmStatic
        val PARAM_USER_ID = "userID"
        @JvmStatic
        val OPERATION_NAME_VALUE = "SellerDrawerData"
    }

    override fun createObservable(requestParams: RequestParams): Observable<SellerUserData> {
        return sellerUserRepository.getSellerUserAttributes(requestParams)
    }

    fun getUserAttrParams(loginId: String, query: String): RequestParams {
        val variables = hashMapOf<String, Any>()

        variables.put(PARAM_USER_ID, loginId.toInt())

        return RequestParams.create().apply {
            putObject(GraphqlHelper.QUERY, query)
            putObject(GraphqlHelper.VARIABLES, variables)
            putObject(GraphqlHelper.OPERATION_NAME, OPERATION_NAME_VALUE)
        }

    }

}