package com.tokopedia.sellerhomedrawer.domain.datasource

import com.tokopedia.sellerhomedrawer.data.SellerUserData
import com.tokopedia.sellerhomedrawer.domain.service.SellerDrawerService
import com.tokopedia.usecase.RequestParams
import rx.Observable

class SellerCloudAttrDataSource(val sellerDrawerService: SellerDrawerService) {

    companion object {
        @JvmStatic
        val KEY_TOKOCASH_BALANCE_CACHE = "TOKOCASH_BALANCE_CACHE"
    }

    private val DURATION_SAVE_TO_CACHE: Long = 60_000

    fun getSellerUserAttributes(requestParams: RequestParams): Observable<SellerUserData> {
        return sellerDrawerService.api
                .getSellerDrawerData(requestParams.parameters)
                .map{ graphqlResponse ->
                    graphqlResponse.body()?.data }
    }

}