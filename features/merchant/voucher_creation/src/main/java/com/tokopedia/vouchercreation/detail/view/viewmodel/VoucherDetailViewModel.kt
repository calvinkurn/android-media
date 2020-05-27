package com.tokopedia.vouchercreation.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.detail.domain.usecase.VoucherDetailUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class VoucherDetailViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val voucherDetailUseCase: VoucherDetailUseCase) : BaseViewModel(dispatcher) {

    private val mMerchantVoucherModelLiveData = MutableLiveData<Result<VoucherUiModel>>()
    val merchantVoucherModelLiveData: LiveData<Result<VoucherUiModel>>
        get() = mMerchantVoucherModelLiveData

    fun getVoucherDetail(voucherId: Int) {
        launchCatchError(
                block = {
                    voucherDetailUseCase.params = VoucherDetailUseCase.createRequestParam(voucherId)
                    mMerchantVoucherModelLiveData.value = Success(withContext(Dispatchers.IO) {
                        voucherDetailUseCase.executeOnBackground()
                    })
                },
                onError = {
                    mMerchantVoucherModelLiveData.value = Fail(it)
                }
        )
    }

}