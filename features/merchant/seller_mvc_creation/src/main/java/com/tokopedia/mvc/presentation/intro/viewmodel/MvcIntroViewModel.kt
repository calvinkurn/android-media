package com.tokopedia.mvc.presentation.intro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class MvcIntroViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase
) : BaseViewModel(dispatchers.main) {

    private val _voucherCreationMetadata = MutableLiveData<Result<VoucherCreationMetadata>>()
    val voucherCreationMetadata: LiveData<Result<VoucherCreationMetadata>> get() = _voucherCreationMetadata

    fun getVoucherCreationMetadata() {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherCreationMetadata = getInitiateVoucherPageUseCase.execute()
                _voucherCreationMetadata.postValue(Success(voucherCreationMetadata))
            },
            onError = { error ->
                _voucherCreationMetadata.postValue(Fail(error))
            }
        )
    }

}
