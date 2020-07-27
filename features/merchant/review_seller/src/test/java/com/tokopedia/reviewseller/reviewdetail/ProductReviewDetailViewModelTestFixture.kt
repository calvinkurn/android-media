package com.tokopedia.reviewseller.reviewdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.reviewseller.coroutine.TestCoroutineDispatchers
import com.tokopedia.reviewseller.feature.reviewdetail.domain.GetProductFeedbackDetailListUseCase
import com.tokopedia.reviewseller.feature.reviewdetail.domain.GetProductReviewInitialUseCase
import com.tokopedia.reviewseller.feature.reviewdetail.view.viewmodel.ProductReviewDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
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

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ProductReviewDetailViewModel(TestCoroutineDispatchers, userSession,
                                        getProductReviewInitialUseCase, getProductFeedbackDetailListUseCase)
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        TestCase.assertEquals(expectedResult, actualResult)
    }
}