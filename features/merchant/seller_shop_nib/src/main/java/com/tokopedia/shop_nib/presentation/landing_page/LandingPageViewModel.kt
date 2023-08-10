package com.tokopedia.shop_nib.presentation.landing_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop_nib.domain.entity.NibSubmissionResult
import com.tokopedia.shop_nib.domain.usecase.SellerSubmitNIBStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class LandingPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val sellerSubmitNibStatusUseCase: SellerSubmitNIBStatusUseCase
) : BaseViewModel(dispatchers.main) {

    private val _previousSubmissionState: MutableLiveData<Result<NibSubmissionResult>> = MutableLiveData()
    val previousSubmissionState : LiveData<Result<NibSubmissionResult>>
        get() = _previousSubmissionState

    fun checkPreviousSubmission() {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = sellerSubmitNibStatusUseCase.execute()
                _previousSubmissionState.postValue(Success(result))
            },
            onError = { error ->
                _previousSubmissionState.postValue(Fail(error))
            }
        )
    }

}
