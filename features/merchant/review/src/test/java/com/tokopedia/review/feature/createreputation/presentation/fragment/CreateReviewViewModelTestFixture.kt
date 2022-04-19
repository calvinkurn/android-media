package com.tokopedia.review.feature.createreputation.presentation.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.review.common.data.ProductrevReviewAttachment
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.GetBadRatingCategoryUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductReputationForm
import com.tokopedia.review.feature.createreputation.domain.usecase.GetReviewTemplatesUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevEditReviewUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevGetPostSubmitBottomSheetUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevSubmitReviewUseCase
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.ovoincentive.usecase.GetProductIncentiveOvo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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

    @RelaxedMockK
    lateinit var getReviewTemplatesUseCase: GetReviewTemplatesUseCase

    @RelaxedMockK
    lateinit var getBadRatingCategoryUseCase: GetBadRatingCategoryUseCase

    @RelaxedMockK
    lateinit var getPostSubmitBottomSheetUseCase: ProductrevGetPostSubmitBottomSheetUseCase

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
    protected val feedbackID = ArgumentMatchers.anyString()
    protected val reputationId = ArgumentMatchers.anyString()
    protected val productId = ArgumentMatchers.anyString()
    protected val shopId = ArgumentMatchers.anyString()
    protected val reputationScore = ArgumentMatchers.anyInt()
    protected val rating = ArgumentMatchers.anyInt()
    protected val review = ArgumentMatchers.anyString()
    protected val isAnonymous = ArgumentMatchers.anyBoolean()
    protected val utmSource = ArgumentMatchers.anyString()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CreateReviewViewModel(
            CoroutineTestDispatchersProvider,
            getProductReputationForm,
            getProductIncentiveOvo,
            getReviewDetailUseCase,
            submitReviewUseCase,
            uploaderUseCase,
            editReviewUseCase,
            userSession,
            getReviewTemplatesUseCase,
            getBadRatingCategoryUseCase,
            getPostSubmitBottomSheetUseCase
        )
    }
}