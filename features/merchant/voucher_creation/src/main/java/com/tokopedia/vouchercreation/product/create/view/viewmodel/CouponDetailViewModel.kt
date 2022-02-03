package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCouponDetailUseCase: GetCouponDetailUseCase
) : BaseViewModel(dispatchers.main) {


    private val _couponDetail = MutableLiveData<Result<VoucherUiModel>>()
    val couponDetail: LiveData<Result<VoucherUiModel>> = _couponDetail


    fun getCouponDetail(couponId : Long) {
        launchCatchError(
            block = {
                getCouponDetailUseCase.params = GetCouponDetailUseCase.createRequestParam(couponId.toInt())
                val result = withContext(dispatchers.io) {
                    getCouponDetailUseCase.executeOnBackground()
                }
                _couponDetail.value = Success(result)
            },
            onError = {
                _couponDetail.value = Fail(it)
            }
        )
    }

}