package com.tokopedia.checkout.view.feature.emptycart2.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-05-23.
 */

class PromoViewModel @Inject constructor(private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase) : ViewModel() {

    val clearPromoResult = MutableLiveData<Result<Boolean>>()

    fun unsubscribeSubscription() {
        clearCacheAutoApplyStackUseCase.unsubscribe()
    }

    fun clearCacheAutoApplyStack(promoCode: String) {
        val promoCodes = arrayListOf(promoCode)
        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodes)
        clearCacheAutoApplyStackUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                clearPromoResult.value = Fail(RuntimeException())
            }

            override fun onNext(response: GraphqlResponse) {
                val responseData = response.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
                if (responseData.successData.success) {
                    clearPromoResult.value = Success(true)
                } else {
                    clearPromoResult.value = Fail(RuntimeException())
                }
            }
        })
    }

}