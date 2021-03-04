package com.tokopedia.review.feature.reviewlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.review.feature.reviewlist.domain.GetProductRatingOverallUseCase
import com.tokopedia.review.feature.reviewlist.domain.GetReviewProductListUseCase
import com.tokopedia.review.feature.reviewlist.view.viewmodel.SellerReviewListViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
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
        viewModel = SellerReviewListViewModel(CoroutineTestDispatchersProvider,
                getProductRatingOverallUse, getReviewProductListUseCase)
        mockkObject(GetProductRatingOverallUseCase)
    }
}