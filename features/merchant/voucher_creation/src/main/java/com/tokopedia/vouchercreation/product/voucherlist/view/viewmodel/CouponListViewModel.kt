package com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherListParam
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getVoucherListUseCase: GetVoucherListUseCase
) : BaseViewModel(dispatchers.main) {

    private val _couponList = MutableLiveData<Result<List<VoucherUiModel>>>()
    val voucherList: LiveData<Result<List<VoucherUiModel>>>
        get() = _couponList

    fun getVoucherList() {
        launchCatchError(block = {
            val ongoingVoucherRequestParam = VoucherListParam.createParam(
                status = VoucherStatus.ONGOING
            )
            _couponList.value = Success(withContext(dispatchers.io) {
                getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(ongoingVoucherRequestParam)
                getVoucherListUseCase.executeOnBackground()
            })

        }, onError = {
            _couponList.value = Fail(it)
        })
    }
}