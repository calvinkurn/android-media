package com.tokopedia.mvc.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class VoucherDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase
) : BaseViewModel(dispatchers.main){

    private var _voucherDetail = MutableLiveData<Result<VoucherDetailData>>()
    val voucherDetail: LiveData<Result<VoucherDetailData>>
        get() = _voucherDetail

    fun getVoucherDetail(voucherId: Long){
        launchCatchError(
            dispatchers.io,
            block = {
                val param = MerchantPromotionGetMVDataByIDUseCase.Param(voucherId)
                val response = merchantPromotionGetMVDataByIDUseCase.execute(param)
                _voucherDetail.postValue(Success(response))
            },
            onError = { error ->
                _voucherDetail.postValue(Fail(error))
            }
        )
    }
}
