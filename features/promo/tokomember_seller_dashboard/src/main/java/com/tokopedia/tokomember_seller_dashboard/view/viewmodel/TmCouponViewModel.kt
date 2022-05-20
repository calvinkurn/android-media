package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponUsecase
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponListResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmCouponViewModel @Inject constructor(
    private val tmCouponUsecase: TmCouponUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
    ) : BaseViewModel(dispatcher) {

    private val _couponListLiveData = MutableLiveData<Result<TmCouponListResponse>>()
    val couponListLiveData: LiveData<Result<TmCouponListResponse>> = _couponListLiveData

    fun getCouponList(voucherStatus: String, voucherType: Int){
        tmCouponUsecase.cancelJobs()
        tmCouponUsecase.getCouponList({
            _couponListLiveData.postValue(Success(it))
        },
            {
                _couponListLiveData.postValue(Fail(it))
            }, voucherStatus, voucherType)
    }

}