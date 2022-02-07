package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponDetailWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailFacadeUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCouponDetailFacadeUseCase: GetCouponDetailFacadeUseCase
) : BaseViewModel(dispatchers.main) {

    private val _couponDetail = MutableLiveData<Result<CouponDetailWithMetadata>>()
    val couponDetail: LiveData<Result<CouponDetailWithMetadata>> = _couponDetail

    fun getCouponDetail(couponId : Long) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getCouponDetailFacadeUseCase.execute(this, couponId)
                }
                _couponDetail.value = Success(result)
            },
            onError = {
                _couponDetail.value = Fail(it)
            }
        )
    }

}