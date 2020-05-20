package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.create.domain.model.CreateVoucherParam
import com.tokopedia.vouchercreation.create.domain.model.MerchantPromotionCreateMvData
import com.tokopedia.vouchercreation.create.domain.usecase.CreateVoucherUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class ReviewVoucherViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val createVoucherUseCase: CreateVoucherUseCase) : BaseViewModel(dispatcher) {

    private val mCreateVoucherResponseLiveData = MutableLiveData<Result<MerchantPromotionCreateMvData>>()
    val createVoucherResponseLiveData: LiveData<Result<MerchantPromotionCreateMvData>>
        get() = mCreateVoucherResponseLiveData

    fun createVoucher(createVoucherParam: CreateVoucherParam) {
        launchCatchError(
                block = {
                    mCreateVoucherResponseLiveData.value = Success(withContext(Dispatchers.IO) {
                        createVoucherUseCase.params = CreateVoucherUseCase.createRequestParam(createVoucherParam)
                        createVoucherUseCase.executeOnBackground()
                    })
                },
                onError = {
                    mCreateVoucherResponseLiveData.value = Fail(it)
                }
        )
    }
}