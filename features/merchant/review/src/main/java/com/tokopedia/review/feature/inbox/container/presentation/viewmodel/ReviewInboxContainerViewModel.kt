package com.tokopedia.review.feature.inbox.container.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.reputation.common.data.model.ProductrevReviewTabCounterList
import com.tokopedia.reputation.common.domain.usecase.ProductrevReviewTabCounterUseCase
import com.tokopedia.review.common.coroutine.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewInboxContainerViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val productrevReviewTabCounterUseCase: ProductrevReviewTabCounterUseCase
) : BaseViewModel(dispatchers.io){

    private val _reviewTabs = MutableLiveData<Result<ProductrevReviewTabCounterList>>()
    val reviewTabs: LiveData<Result<ProductrevReviewTabCounterList>>
        get() = _reviewTabs

    fun getReviewTabs() {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                productrevReviewTabCounterUseCase.executeOnBackground()
            }
            _reviewTabs.postValue(Success(response.productrevReviewTabCounterList))
        }) {
            _reviewTabs.postValue(Fail(it))
        }
    }
}