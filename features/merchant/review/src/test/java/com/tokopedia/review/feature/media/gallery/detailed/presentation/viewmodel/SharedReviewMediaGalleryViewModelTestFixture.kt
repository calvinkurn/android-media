package com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.review.feature.media.gallery.detailed.domain.usecase.GetDetailedReviewMediaUseCase
import com.tokopedia.review.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.utils.createSuccessResponse
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductRevGetDetailedReviewMediaResponse
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

open class SharedReviewMediaGalleryViewModelTestFixture {

    protected lateinit var viewModel: SharedReviewMediaGalleryViewModel

    @get:Rule
    val rule = UnconfinedTestRule()

    @RelaxedMockK
    lateinit var getDetailedReviewMediaUseCase: GetDetailedReviewMediaUseCase

    @RelaxedMockK
    lateinit var toggleLikeReviewUseCase: ToggleLikeReviewUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    protected val getDetailedReviewMediaResult1stPage = createSuccessResponse<ProductRevGetDetailedReviewMediaResponse>(
        "json/get_detailed_review_media_use_case_result_with_image_and_video.json"
    ).getSuccessData<ProductRevGetDetailedReviewMediaResponse>()

    protected val getDetailedReviewMediaResult2ndPage = createSuccessResponse<ProductRevGetDetailedReviewMediaResponse>(
        "json/get_detailed_review_media_use_case_result_with_image_load_more.json"
    ).getSuccessData<ProductRevGetDetailedReviewMediaResponse>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SharedReviewMediaGalleryViewModel(
            rule.dispatchers, getDetailedReviewMediaUseCase, toggleLikeReviewUseCase, userSession
        )
    }
}
