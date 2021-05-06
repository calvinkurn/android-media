package com.tokopedia.review.feature.inbox.container.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.reputation.common.data.source.cloud.model.ProductrevReviewTabCount
import com.tokopedia.reputation.common.domain.usecase.ProductrevReviewTabCounterUseCase
import com.tokopedia.review.feature.inbox.common.presentation.InboxUnifiedRemoteConfig.isInboxUnified
import com.tokopedia.review.feature.inbox.container.data.ReviewInboxTabs
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewInboxContainerViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val productrevReviewTabCounterUseCase: ProductrevReviewTabCounterUseCase
) : BaseViewModel(dispatchers.io){

    private val _reviewTabs = MutableLiveData<Result<ProductrevReviewTabCount>>()
    val reviewTabs: LiveData<MutableList<ReviewInboxTabs>> = Transformations.map(_reviewTabs) {
        updateCounters(it)
    }

    fun getUserId() = userSessionInterface.userId ?: ""

    fun getTabCounter() {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                productrevReviewTabCounterUseCase.executeOnBackground()
            }
            _reviewTabs.postValue(Success(response.productrevReviewTabCount))
        }) {
            _reviewTabs.postValue(Fail(it))
        }
    }

    private fun updateCounters(tabCount: Result<ProductrevReviewTabCount>): MutableList<ReviewInboxTabs> {
        val result = mutableListOf<ReviewInboxTabs>()
        if(tabCount is Success && !isInboxUnified()) {
            with(tabCount.data.count) {
                result.add(ReviewInboxTabs.ReviewInboxPending(this.toString()))
            }
        } else {
            result.add(ReviewInboxTabs.ReviewInboxPending())
        }
        result.add(ReviewInboxTabs.ReviewInboxHistory)
        if(isShopOwner() && !isInboxUnified()) {
            result.add(ReviewInboxTabs.ReviewInboxSeller)
        }
        return result
    }

    private fun isShopOwner() = userSessionInterface.hasShop()
}