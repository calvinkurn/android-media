package com.tokopedia.sellerreview.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerreview.domain.usecase.SendReviewUseCase
import com.tokopedia.sellerreview.view.model.SendReviewParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

/**
 * Created By @ilhamsuaib on 28/01/21
 */

class ReviewViewModel(
        private val reviewUseCase: SendReviewUseCase,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _reviewStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val reviewStatus: LiveData<Result<Boolean>>
        get() = _reviewStatus

    fun submitReview(param: SendReviewParam) {
        launchCatchError(block = {
            reviewUseCase.params = SendReviewUseCase.createParams(param)
            val result = reviewUseCase.executeOnBackground()
            _reviewStatus.postValue(Success(result))
        }, onError = {
            _reviewStatus.postValue(Fail(it))
        })
    }
}