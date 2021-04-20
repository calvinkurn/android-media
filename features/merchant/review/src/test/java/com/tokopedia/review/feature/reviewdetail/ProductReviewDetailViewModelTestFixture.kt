package com.tokopedia.review.feature.reviewdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.review.feature.reviewdetail.domain.GetProductFeedbackDetailListUseCase
import com.tokopedia.review.feature.reviewdetail.domain.GetProductReviewInitialUseCase
import com.tokopedia.review.feature.reviewdetail.view.viewmodel.ProductReviewDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

abstract class ProductReviewDetailViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getProductReviewInitialUseCase: GetProductReviewInitialUseCase

    @RelaxedMockK
    lateinit var getProductFeedbackDetailListUseCase: GetProductFeedbackDetailListUseCase

    protected lateinit var viewModel: ProductReviewDetailViewModel
    protected lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ProductReviewDetailViewModel(CoroutineTestDispatchersProvider, userSession,
                                        getProductReviewInitialUseCase, getProductFeedbackDetailListUseCase)
        lifecycle = LifecycleRegistry(mockk()).apply {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        TestCase.assertEquals(expectedResult, actualResult)
    }
}