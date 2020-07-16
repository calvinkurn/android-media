package com.tokopedia.reviewseller.reviewlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.reviewseller.coroutine.TestCoroutineDispatchers
import com.tokopedia.reviewseller.feature.reviewlist.domain.GetProductRatingOverallUseCase
import com.tokopedia.reviewseller.feature.reviewlist.domain.GetReviewProductListUseCase
import com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel.SellerReviewListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule

abstract class SellerReviewListViewModelTextFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getProductRatingOverallUse: GetProductRatingOverallUseCase

    @RelaxedMockK
    lateinit var getReviewProductListUseCase: GetReviewProductListUseCase

    protected lateinit var viewModel: SellerReviewListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SellerReviewListViewModel(TestCoroutineDispatchers,
                getProductRatingOverallUse, getReviewProductListUseCase)
    }

    protected fun LiveData<*>.verifySuccessEquals(expected: Success<*>) {
        val expectedResult = expected.data
        val actualResult = (value as Success<*>).data
        assertEquals(expectedResult, actualResult)
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        assertEquals(expectedResult, actualResult)
    }
}