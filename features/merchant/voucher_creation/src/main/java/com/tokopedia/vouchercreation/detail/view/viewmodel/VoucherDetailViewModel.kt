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
import com.tokopedia.vouchercreation.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VoucherDetailViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val voucherDetailUseCase: VoucherDetailUseCase,
        private val cancelVoucherUseCase: CancelVoucherUseCase,
        private val shopBasicDataUseCase: ShopBasicDataUseCase) : BaseViewModel(dispatcher) {

    private val mVoucherIdLiveData = MutableLiveData<Int>()

    private val mMerchantVoucherModelLiveData = MediatorLiveData<Result<VoucherUiModel>>().apply {
        addSource(mVoucherIdLiveData) { voucherId ->
            launchCatchError(
                    block = {
                        withContext(Dispatchers.IO) {
                            voucherDetailUseCase.params = VoucherDetailUseCase.createRequestParam(voucherId)
                            val voucherDetail = async { Success(voucherDetailUseCase.executeOnBackground()) }
                            val shopBasicData = async { Success(shopBasicDataUseCase.executeOnBackground()) }
                            postValue(voucherDetail.await())
                            _shopBasicLiveData.postValue(shopBasicData.await())
                        }
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

    private val _shopBasicLiveData = MutableLiveData<Result<ShopBasicDataResult>>()
    val shopBasicLiveData: LiveData<Result<ShopBasicDataResult>>
        get() = _shopBasicLiveData

    fun getVoucherDetail(voucherId: Int) {
        mVoucherIdLiveData.value = voucherId
    }

    fun cancelVoucher(voucherId: Int,
                      @CancelVoucherUseCase.CancelStatus cancelStatus: String) {
        mCancelledVoucherIdLiveData.value = Pair(voucherId, cancelStatus)
    }

}