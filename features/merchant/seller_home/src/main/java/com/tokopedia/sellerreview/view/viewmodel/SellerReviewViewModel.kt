package com.tokopedia.sellerreview.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerreview.domain.usecase.SendReviewUseCase
import com.tokopedia.sellerreview.view.model.SendReviewParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 28/01/21
 */

@SellerHomeScope
class SellerReviewViewModel @Inject constructor(
        private val reviewUseCase: Lazy<SendReviewUseCase>,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _reviewStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val reviewStatus: LiveData<Result<Boolean>>
        get() = _reviewStatus

    fun submitReview(param: SendReviewParam) {
        launchCatchError(block = {
            reviewUseCase.get().params = SendReviewUseCase.createParams(param)
            val result = reviewUseCase.get().executeOnBackground()
            _reviewStatus.postValue(Success(result))
        }, onError = {
            _reviewStatus.postValue(Fail(it))
        })
    }
}