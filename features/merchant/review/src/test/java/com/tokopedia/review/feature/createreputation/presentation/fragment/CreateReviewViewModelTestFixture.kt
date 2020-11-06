package com.tokopedia.review.feature.createreputation.presentation.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.review.common.data.ProductrevReviewAttachment
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.coroutine.TestCoroutineDispatchers
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductIncentiveOvo
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductReputationForm
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevEditReviewUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevSubmitReviewUseCase
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers

abstract class CreateReviewViewModelTestFixture {

    @RelaxedMockK
    lateinit var getProductReputationForm: GetProductReputationForm

    @RelaxedMockK
    lateinit var getProductIncentiveOvo: GetProductIncentiveOvo

    @RelaxedMockK
    lateinit var getReviewDetailUseCase: ProductrevGetReviewDetailUseCase

    @RelaxedMockK
    lateinit var submitReviewUseCase: ProductrevSubmitReviewUseCase

    @RelaxedMockK
    lateinit var uploaderUseCase: UploaderUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var editReviewUseCase: ProductrevEditReviewUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: CreateReviewViewModel
    protected val images = listOf(
            ProductrevReviewAttachment("ImageUrl1", "ImageUrl1"),
            ProductrevReviewAttachment("ImageUrl2", "ImageUrl2"),
            ProductrevReviewAttachment("ImageUrl3", "ImageUrl3"),
            ProductrevReviewAttachment("ImageUrl4", "ImageUrl4"),
            ProductrevReviewAttachment("ImageUrl5", "ImageUrl5")
    )
    protected val feedbackID = ArgumentMatchers.anyInt()
    protected val reputationId = ArgumentMatchers.anyInt()
    protected val productId = ArgumentMatchers.anyInt()
    protected val shopId = ArgumentMatchers.anyInt()
    protected val reputationScore = ArgumentMatchers.anyInt()
    protected val rating = ArgumentMatchers.anyInt()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CreateReviewViewModel(TestCoroutineDispatchers, getProductReputationForm, getProductIncentiveOvo, getReviewDetailUseCase, submitReviewUseCase, uploaderUseCase, editReviewUseCase, userSession)
    }
}