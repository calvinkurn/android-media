package com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.LIST_COUPON_PER_PAGE
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherListParam
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getVoucherListUseCase: GetVoucherListUseCase,
    private val cancelVoucherUseCase: CancelVoucherUseCase
) : BaseViewModel(dispatchers.main) {

    private val _couponList = MutableLiveData<Result<List<VoucherUiModel>>>()
    private val _cancelCoupon = MutableLiveData<Result<Int>>()
    private val _stopCoupon = MutableLiveData<Result<Int>>()

    val couponList: LiveData<Result<List<VoucherUiModel>>>
        get() = _couponList
    val cancelCoupon: LiveData<Result<Int>>
        get() = _cancelCoupon
    val stopCoupon: LiveData<Result<Int>>
        get() = _stopCoupon

    fun getVoucherList(page: Int) {
        launchCatchError(block = {
            val ongoingVoucherRequestParam = VoucherListParam.createParamCouponList(
                status = VoucherStatus.NOT_STARTED_AND_ONGOING,
                page = page,
                perPage = LIST_COUPON_PER_PAGE
            )
            _couponList.value = Success(withContext(dispatchers.io) {
                getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(ongoingVoucherRequestParam)
                getVoucherListUseCase.executeOnBackground()
            })

        }, onError = {
            _couponList.value = Fail(it)
        })
    }

    fun cancelCoupon(couponId: Int, @CancelVoucherUseCase.CancelStatus status: String) {
        val liveData = if (status == CancelVoucherUseCase.CancelStatus.STOP) {
            _stopCoupon
        } else {
            _cancelCoupon
        }

        launchCatchError(block = {
            cancelVoucherUseCase.params = CancelVoucherUseCase.createRequestParam(couponId, status)
            liveData.value = Success(withContext(dispatchers.io) {
                cancelVoucherUseCase.executeOnBackground()
            })
        }, onError = {
            liveData.value = Fail(it)
        })
    }
}