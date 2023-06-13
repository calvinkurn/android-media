package com.tokopedia.review.feature.createreputation.presentation.viewmodel.old

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.review.common.data.ProductrevReviewImageAttachment
import com.tokopedia.review.common.data.ProductrevReviewVideoAttachment
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevEditReviewUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers

abstract class CreateReviewViewModelTestFixture {

    @RelaxedMockK
    lateinit var getReviewDetailUseCase: ProductrevGetReviewDetailUseCase

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
        ProductrevReviewImageAttachment("ImageUrl1", "ImageUrl1"),
        ProductrevReviewImageAttachment("ImageUrl2", "ImageUrl2"),
        ProductrevReviewImageAttachment("ImageUrl3", "ImageUrl3"),
        ProductrevReviewImageAttachment("ImageUrl4", "ImageUrl4"),
        ProductrevReviewImageAttachment("ImageUrl5", "ImageUrl5")
    )
    protected val videos = listOf(
        ProductrevReviewVideoAttachment("1234567890", "https://tokopedia.com/video.mp4")
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
            getReviewDetailUseCase,
            uploaderUseCase,
            editReviewUseCase,
            userSession,
        )
    }
}