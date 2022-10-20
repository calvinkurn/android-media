package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponQuotaUpdateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponUpdateStatusUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponInitialUsecase
import com.tokopedia.tokomember_seller_dashboard.model.*
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

    private val _tmCouponListStateLiveData = MutableLiveData<Int>()
    val tmCouponListStateLiveData: LiveData<Int> = _tmCouponListStateLiveData

    val couponList:MutableList<CouponItem> = mutableListOf()

    fun refreshListState(state: Int){
        _tmCouponListStateLiveData.postValue(state)
    }

    fun getInitialCouponList(voucherStatus: String, voucherType: Int?, page: Int = 1, perPage: Int = 10){
        if(couponListLiveData.value == null || couponList.isEmpty()){
            getCouponList(voucherStatus, voucherType, page, perPage)
        }
    }

    fun getCouponList(voucherStatus: String, voucherType: Int?, page: Int = 1, perPage: Int = 10){
        tmCouponUsecase.cancelJobs()
        if(page>1){
            addLoader()
            _couponListLiveData.postValue(TokoLiveDataResult.infiniteLoading())
        }
        else {
            _couponListLiveData.postValue(TokoLiveDataResult.loading())
        }
        if(page==1){
            couponList.clear()
        }
        tmCouponUsecase.getCouponList({
            removeLoader()
            it.merchantPromotionGetMVList?.data?.vouchers?.forEach { it1 ->
                it1?.let { it2 -> couponList.add(CouponItem(it2)) }
            }
            _couponListLiveData.postValue(TokoLiveDataResult.success(it))
        },
            {
                removeLoader()
                _couponListLiveData.postValue(TokoLiveDataResult.error(it))
            }, voucherStatus, voucherType, page, perPage)
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

    private fun addLoader(){
        couponList.add(CouponItem(VouchersItem(), LayoutType.LOADER))
    }

    private fun removeLoader() {
        if(couponList.isNotEmpty() && couponList.last().layout == LayoutType.LOADER) {
            couponList.removeLast()
        }
    }

}
