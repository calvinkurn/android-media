package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponQuotaUpdateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponUpdateStatusUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponInitialUsecase
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponInitialResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponListResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponUpdateResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmUpdateCouponQuotaResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmCouponViewModel @Inject constructor(
    private val tmCouponUsecase: TmCouponUsecase,
    private val tmKuponInitialUsecase: TmKuponInitialUsecase,
    private val tmCouponUpdateStatusUsecase: TmCouponUpdateStatusUsecase,
    private val tmCouponQuotaUpdateUsecase: TmCouponQuotaUpdateUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
    ) : BaseViewModel(dispatcher) {

    private val _couponListLiveData = MutableLiveData<Result<TmCouponListResponse>>()
    val couponListLiveData: LiveData<Result<TmCouponListResponse>> = _couponListLiveData

    private val _tmCouponInitialLiveData = MutableLiveData<Result<TmCouponInitialResponse>>()
    val tmCouponInitialLiveData: LiveData<Result<TmCouponInitialResponse>> =
        _tmCouponInitialLiveData

    private val _tmCouponUpdateLiveData = MutableLiveData<Result<TmCouponUpdateResponse>>()
    val tmCouponUpdateLiveData: LiveData<Result<TmCouponUpdateResponse>> =
        _tmCouponUpdateLiveData

    private val _tmCouponQuotaUpdateLiveData = MutableLiveData<Result<TmUpdateCouponQuotaResponse>>()
    val tmCouponQuotaUpdateLiveData: LiveData<Result<TmUpdateCouponQuotaResponse>> =
        _tmCouponQuotaUpdateLiveData

    fun getCouponList(voucherStatus: String, voucherType: Int?){
        tmCouponUsecase.cancelJobs()
        tmCouponUsecase.getCouponList({
            _couponListLiveData.postValue(Success(it))
        },
            {
                _couponListLiveData.postValue(Fail(it))
            }, voucherStatus, voucherType)
    }

    fun getInitialCouponData(action: String, couponType: String){
        tmKuponInitialUsecase.cancelJobs()
        tmKuponInitialUsecase.getInitialCoupon( {
            _tmCouponInitialLiveData.postValue(Success(it))
        }, {
            _tmCouponInitialLiveData.postValue(Fail(it))
        }, action, couponType)
    }

    fun updateStatus(voucherId: Int, token: String, status: String){
        tmCouponUpdateStatusUsecase.cancelJobs()
        tmCouponUpdateStatusUsecase.updateCouponStatus( {
            _tmCouponUpdateLiveData.postValue(Success(it))
        }, {
            _tmCouponUpdateLiveData.postValue(Fail(it))
        }, voucherId, voucherStatus = status, token)
    }

    fun updateQuota(quota: Int, voucherId: Int, token: String){
        tmCouponQuotaUpdateUsecase.cancelJobs()
        tmCouponQuotaUpdateUsecase.updateCouponQuota( {
            _tmCouponQuotaUpdateLiveData.postValue(Success(it))
        }, {
            _tmCouponQuotaUpdateLiveData.postValue(Fail(it))
        }, quota, voucherId, token)
    }

}