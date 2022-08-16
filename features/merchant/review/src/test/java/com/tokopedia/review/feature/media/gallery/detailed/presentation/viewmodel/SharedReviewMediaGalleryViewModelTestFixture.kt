package com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.review.feature.media.gallery.detailed.domain.usecase.GetDetailedReviewMediaUseCase
import com.tokopedia.review.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.utils.createSuccessResponse
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductRevGetDetailedReviewMediaResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before

open class SharedReviewMediaGalleryViewModelTestFixture {
    private val coroutineDispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: SharedReviewMediaGalleryViewModel

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
            coroutineDispatchers, getDetailedReviewMediaUseCase, toggleLikeReviewUseCase, userSession
        )
    }
}