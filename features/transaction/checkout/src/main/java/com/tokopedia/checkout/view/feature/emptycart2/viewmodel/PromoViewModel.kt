package com.tokopedia.checkout.view.feature.emptycart2.viewmodel

import android.arch.lifecycle.ViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-05-23.
 */

class PromoViewModel @Inject constructor(private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase) : ViewModel() {

    fun clearCacheAutoApplyStack(promoCode: String, onResultSuccess: () -> Unit, onResultError: (e: Throwable) -> Unit) {
        val promoCodes = arrayListOf(promoCode)
        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodes)
        clearCacheAutoApplyStackUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                onResultError(e)
            }

            override fun onNext(response: GraphqlResponse) {
                val responseData = response.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
                if (responseData.successData.success) {
                    onResultSuccess()
                } else {
                    onResultError(RuntimeException())
                }
            }
        })
    }

}