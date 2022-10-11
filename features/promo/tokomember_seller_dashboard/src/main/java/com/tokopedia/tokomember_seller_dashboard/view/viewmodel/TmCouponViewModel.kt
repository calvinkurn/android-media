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
import com.tokopedia.tokomember_seller_dashboard.model.TmUpdateCouponQuotaDataExt
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmCouponViewModel @Inject constructor(
    private val tmCouponUsecase: TmCouponUsecase,
    private val tmKuponInitialUsecase: TmKuponInitialUsecase,
    private val tmCouponUpdateStatusUsecase: TmCouponUpdateStatusUsecase,
    private val tmCouponQuotaUpdateUsecase: TmCouponQuotaUpdateUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
    ) : BaseViewModel(dispatcher) {

    private val _couponListLiveData = MutableLiveData<TokoLiveDataResult<TmCouponListResponse>>()
    val couponListLiveData: LiveData<TokoLiveDataResult<TmCouponListResponse>> = _couponListLiveData

    private val _tmCouponInitialLiveData = MutableLiveData<TokoLiveDataResult<TmCouponInitialResponse>>()
    val tmCouponInitialLiveData: LiveData<TokoLiveDataResult<TmCouponInitialResponse>> =
        _tmCouponInitialLiveData

    private val _tmCouponUpdateLiveData = MutableLiveData<TokoLiveDataResult<TmCouponUpdateResponse>>()
    val tmCouponUpdateLiveData: LiveData<TokoLiveDataResult<TmCouponUpdateResponse>> =
        _tmCouponUpdateLiveData

    private val _tmCouponQuotaUpdateLiveData = MutableLiveData<TokoLiveDataResult<TmUpdateCouponQuotaDataExt>>()
    val tmCouponQuotaUpdateLiveData: LiveData<TokoLiveDataResult<TmUpdateCouponQuotaDataExt>> =
        _tmCouponQuotaUpdateLiveData

    fun getCouponList(voucherStatus: String, voucherType: Int?){
        tmCouponUsecase.cancelJobs()
        _couponListLiveData.postValue(TokoLiveDataResult.loading())
        tmCouponUsecase.getCouponList({
            _couponListLiveData.postValue(TokoLiveDataResult.success(it))
        },
            {
                _couponListLiveData.postValue(TokoLiveDataResult.error(it))
            }, voucherStatus, voucherType)
    }

    fun getInitialCouponData(action: String, couponType: String){
        tmKuponInitialUsecase.cancelJobs()
        _tmCouponInitialLiveData.postValue(TokoLiveDataResult.loading())
        tmKuponInitialUsecase.getInitialCoupon( {
            _tmCouponInitialLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmCouponInitialLiveData.postValue(TokoLiveDataResult.error(it))
        }, action, couponType)
    }

    fun updateStatus(voucherId: Int, token: String, status: String){
        tmCouponUpdateStatusUsecase.cancelJobs()
        _tmCouponUpdateLiveData.postValue(TokoLiveDataResult.loading())
        tmCouponUpdateStatusUsecase.updateCouponStatus( {
            _tmCouponUpdateLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmCouponUpdateLiveData.postValue(TokoLiveDataResult.error(it))
        }, voucherId, voucherStatus = status, token)
    }

    fun updateQuota(quota: Int, voucherId: Int, token: String){
        tmCouponQuotaUpdateUsecase.cancelJobs()
        _tmCouponQuotaUpdateLiveData.postValue(TokoLiveDataResult.loading())
        tmCouponQuotaUpdateUsecase.updateCouponQuota( {
            _tmCouponQuotaUpdateLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmCouponQuotaUpdateLiveData.postValue(TokoLiveDataResult.error(it))
        }, quota, voucherId, token)
    }

}