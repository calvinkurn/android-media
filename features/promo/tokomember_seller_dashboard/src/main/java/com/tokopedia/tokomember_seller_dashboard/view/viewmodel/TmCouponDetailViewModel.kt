package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponDetailUsecase
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponDetailResponseData
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmCouponDetailViewModel @Inject constructor(
    val couponDetailUsecase: TmCouponDetailUsecase,
    @CoroutineMainDispatcher private val dispatcher:CoroutineDispatcher
):BaseViewModel(dispatcher) {
    val couponDetailResult  = MutableLiveData<TokoLiveDataResult<TmCouponDetailResponseData>>()

    fun getCouponDetails(voucherId:Int){
        couponDetailResult.postValue(TokoLiveDataResult.loading())
        couponDetailUsecase.getCouponDetail(
            {
                couponDetailResult.postValue(TokoLiveDataResult.success(it))
            },
            {
                couponDetailResult.postValue(TokoLiveDataResult.error(it))
            },
            voucherId
        )
    }
}