package com.tokopedia.vouchercreation.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.detail.domain.usecase.VoucherDetailUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VoucherDetailViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val voucherDetailUseCase: VoucherDetailUseCase,
        private val cancelVoucherUseCase: CancelVoucherUseCase) : BaseViewModel(dispatcher) {

    private val mVoucherIdLiveData = MutableLiveData<Int>()

    private val mMerchantVoucherModelLiveData = MediatorLiveData<Result<VoucherUiModel>>().apply {
        addSource(mVoucherIdLiveData) { voucherId ->
            launchCatchError(
                    block = {
                        voucherDetailUseCase.params = VoucherDetailUseCase.createRequestParam(voucherId)
                        value = Success(withContext(Dispatchers.IO) {
                            voucherDetailUseCase.executeOnBackground()
                        })
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val merchantVoucherModelLiveData: LiveData<Result<VoucherUiModel>>
        get() = mMerchantVoucherModelLiveData

    private val mCancelledVoucherIdLiveData = MutableLiveData<Pair<Int, String>>()

    private val mCancelVoucherResultLiveData = MediatorLiveData<Result<Int>>().apply {
        addSource(mCancelledVoucherIdLiveData) { voucherPair ->
            launchCatchError(
                    block = {
                        cancelVoucherUseCase.params = CancelVoucherUseCase.createRequestParam(voucherPair.first, voucherPair.second)
                        value = Success(withContext(Dispatchers.IO) {
                            cancelVoucherUseCase.executeOnBackground()
                        })
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val cancelVoucherResultLiveData: LiveData<Result<Int>>
        get() = mCancelVoucherResultLiveData

    fun getVoucherDetail(voucherId: Int) {
        mVoucherIdLiveData.value = voucherId
    }

    fun cancelVoucher(voucherId: Int,
                      @CancelVoucherUseCase.CancelStatus cancelStatus: String) {
        mCancelledVoucherIdLiveData.value = Pair(voucherId, cancelStatus)
    }

}