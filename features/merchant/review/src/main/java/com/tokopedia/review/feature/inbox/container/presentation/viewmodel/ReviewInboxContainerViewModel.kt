package com.tokopedia.review.feature.inbox.container.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.reputation.common.data.model.ProductrevReviewTabCounterList
import com.tokopedia.reputation.common.domain.usecase.ProductrevReviewTabCounterUseCase
import com.tokopedia.review.common.coroutine.CoroutineDispatchers
import com.tokopedia.review.feature.inbox.container.data.ReviewInboxTabs
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewInboxContainerViewModel @Inject constructor(
        userSessionInterface: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers,
        private val productrevReviewTabCounterUseCase: ProductrevReviewTabCounterUseCase
) : BaseViewModel(dispatchers.io){

    private val _reviewTabs = MutableLiveData<Result<ProductrevReviewTabCounterList>>()
    val reviewTabs: LiveData<List<ReviewInboxTabs>> = Transformations.map(_reviewTabs) {
        updateCounters(it)
    }

    private val isShopOwner = userSessionInterface.hasShop()

    fun getTabCounter() {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                productrevReviewTabCounterUseCase.executeOnBackground()
            }
            _reviewTabs.postValue(Success(response.productrevReviewTabCounterList))
        }) {
            _reviewTabs.postValue(Fail(it))
        }
    }

    private fun updateCounters(tabCounters: Result<ProductrevReviewTabCounterList>): List<ReviewInboxTabs> {
        val result = mutableListOf<ReviewInboxTabs>()
        when(tabCounters) {
            is Success -> {
                with(tabCounters.data.tabs) {
                    result.add(ReviewInboxTabs.ReviewInboxPending(this.first().count.toString()))
                    result.add(ReviewInboxTabs.ReviewInboxHistory(this.last().count.toString()))
                }
            }
            is Fail -> {
                result.add(ReviewInboxTabs.ReviewInboxPending())
                result.add(ReviewInboxTabs.ReviewInboxHistory())
            }
        }
        if(isShopOwner) {
            result.add(ReviewInboxTabs.ReviewInboxSeller)
        }
        return result
    }
}