package com.tokopedia.review.feature.reading.presentation.viewmodel

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.common.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductReviewListUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetShopRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetShopReviewListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ReadReviewViewModelTestFixture {

    @RelaxedMockK
    lateinit var getProductRatingAndTopicsUseCase: GetProductRatingAndTopicsUseCase

    @RelaxedMockK
    lateinit var getProductReviewListUseCase: GetProductReviewListUseCase

    @RelaxedMockK
    lateinit var toggleLikeReviewUseCase: ToggleLikeReviewUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getShopRatingAndTopicsUseCase: GetShopRatingAndTopicsUseCase

    @RelaxedMockK
    lateinit var getShopReviewListUseCase: GetShopReviewListUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReadReviewViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReadReviewViewModel(getProductRatingAndTopicsUseCase, getShopRatingAndTopicsUseCase, getProductReviewListUseCase, getShopReviewListUseCase, toggleLikeReviewUseCase, userSessionInterface, CoroutineTestDispatchersProvider)
        viewModel.productReviews.observeForever { }
        viewModel.shopReviews.observeForever { }
        viewModel.ratingAndTopic.observeForever { }
        viewModel.shopRatingAndTopic.observeForever { }
        mockkStatic(Resources::class)
        val resources = mockk<Resources>()
        val displayMetrics = mockk<DisplayMetrics>()
        every { Resources.getSystem() } returns resources
        every { Resources.getSystem().displayMetrics } returns displayMetrics
    }

    @After
    fun finish() {
        unmockkStatic(Resources::class)
    }
}