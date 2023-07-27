package com.tokopedia.review.feature.createreputation.presentation.viewmodel

import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.GetBadRatingCategoryUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductReputationForm
import com.tokopedia.review.feature.createreputation.domain.usecase.GetReviewTemplatesUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevEditReviewUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevGetPostSubmitBottomSheetUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevSubmitReviewUseCase
import com.tokopedia.review.feature.createreputation.model.BadRatingCategoriesResponse
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponseWrapper
import com.tokopedia.review.feature.createreputation.model.ProductrevGetReviewTemplateResponseWrapper
import com.tokopedia.review.feature.createreputation.model.ProductrevSubmitReviewResponseWrapper
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewToasterUiModel
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.usecase.GetProductIncentiveOvo
import com.tokopedia.review.utils.createSuccessResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule

abstract class CreateReviewViewModelTestFixture {

    companion object {
        const val BAD_RATING_CATEGORY_ID_PRODUCT_PROBLEM = "1"
        const val BAD_RATING_CATEGORY_ID_OTHER = "6"

        const val SAMPLE_FEEDBACK_ID = "666589017"
        const val SAMPLE_REPUTATION_ID = "910390101"
        const val SAMPLE_PRODUCT_ID = "626102761"
        const val SAMPLE_ORDER_ID = "1195866520"
        const val SAMPLE_UTM_SOURCE = "lorem ipsum"
        const val SAMPLE_USER_ID = "1111111110"
        const val SAMPLE_USER_NAME = "Spongebob"
        const val SAMPLE_SHOP_ID = "6996572"
        const val SAMPLE_UPLOAD_ID = "abcd-1234-efgh-5678-ijkl"
        const val SAMPLE_VIDEO_URL = "https://www.tokopedia.com/patrick-belly-dancing.mp4"
        const val ERROR_CODE = "OO-XE8L"

        const val SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_VALID_TO_REVIEW_WITH_NON_EMPTY_KEYWORDS = "json/get_reputation_form_use_case_result_success_valid_to_review_with_non_empty_keywords.json"
        const val SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_VALID_TO_REVIEW_WITH_EMPTY_KEYWORDS = "json/get_reputation_form_use_case_result_success_valid_to_review_with_empty_keywords.json"
        const val SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_VALID_TO_REVIEW_WITH_EMPTY_PLACEHOLDER = "json/get_reputation_form_use_case_result_success_valid_to_review_with_empty_placeholder.json"
        const val SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_INVALID_TO_REVIEW = "json/get_reputation_form_use_case_result_success_invalid_to_review.json"
        const val SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_PRODUCT_DELETED = "json/get_reputation_form_use_case_result_success_product_deleted.json"
        const val SAMPLE_GET_REVIEW_TEMPLATE_RESULT_SUCCESS_EMPTY = "json/get_review_template_use_case_result_success_empty.json"
        const val SAMPLE_GET_REVIEW_TEMPLATE_RESULT_SUCCESS_NON_EMPTY = "json/get_review_template_use_case_result_success_non_empty.json"
        const val SAMPLE_GET_PRODUCT_INCENTIVE_OVO_RESULT_SUCCESS_NO_INCENTIVE = "json/get_product_incentive_ovo_result_success_no_incentive.json"
        const val SAMPLE_GET_PRODUCT_INCENTIVE_OVO_RESULT_SUCCESS_INCENTIVE = "json/get_product_incentive_ovo_result_success_incentive.json"
        const val SAMPLE_GET_PRODUCT_INCENTIVE_OVO_RESULT_SUCCESS_CHALLENGE = "json/get_product_incentive_ovo_result_success_challenge.json"
        const val SAMPLE_GET_BAD_RATING_CATEGORY_RESULT_SUCCESS_NON_EMPTY = "json/get_bad_rating_category_use_case_result_success_non_empty.json"
        const val SAMPLE_SUBMIT_REVIEW_RESULT_SUCCESS = "json/submit_review_use_case_result_success.json"
        const val SAMPLE_GET_POST_SUBMIT_BOTTOM_SHEET_RESULT_SUCCESS_SHOW_BOTTOM_SHEET = "json/get_post_submit_bottom_sheet_use_case_result_success_show_bottom_sheet.json"
        const val SAMPLE_GET_POST_SUBMIT_BOTTOM_SHEET_RESULT_SUCCESS_SHOW_TOASTER = "json/get_post_submit_bottom_sheet_use_case_result_success_show_toaster.json"
    }

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
    lateinit var editReviewUseCase: ProductrevEditReviewUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getReviewTemplatesUseCase: GetReviewTemplatesUseCase

    @RelaxedMockK
    lateinit var getBadRatingCategoryUseCase: GetBadRatingCategoryUseCase

    @RelaxedMockK
    lateinit var getPostSubmitBottomSheetUseCase: ProductrevGetPostSubmitBottomSheetUseCase

    @RelaxedMockK
    lateinit var cacheManager: CacheManager

    @get:Rule
    val rule = CoroutineTestRule()

    protected lateinit var viewModel: CreateReviewViewModel

    protected val getReputationFormUseCaseResultSuccessValidWithNonEmptyKeywords = createSuccessResponse<ProductRevGetForm>(
        SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_VALID_TO_REVIEW_WITH_NON_EMPTY_KEYWORDS
    ).getSuccessData<ProductRevGetForm>()

    protected val getReputationFormUseCaseResultSuccessValidWithEmptyKeywords = createSuccessResponse<ProductRevGetForm>(
        SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_VALID_TO_REVIEW_WITH_EMPTY_KEYWORDS
    ).getSuccessData<ProductRevGetForm>()

    protected val getReputationFormUseCaseResultSuccessValidWithEmptyPlaceholder = createSuccessResponse<ProductRevGetForm>(
        SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_VALID_TO_REVIEW_WITH_EMPTY_PLACEHOLDER
    ).getSuccessData<ProductRevGetForm>()

    protected val getReputationFormUseCaseResultSuccessInvalid = createSuccessResponse<ProductRevGetForm>(
        SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_INVALID_TO_REVIEW
    ).getSuccessData<ProductRevGetForm>()

    protected val getReputationFormUseCaseResultSuccessProductDeleted = createSuccessResponse<ProductRevGetForm>(
        SAMPLE_GET_REPUTATION_FORM_USE_CASE_RESULT_SUCCESS_PRODUCT_DELETED
    ).getSuccessData<ProductRevGetForm>()

    protected val getReviewTemplateUseCaseResultSuccessEmpty = createSuccessResponse<ProductrevGetReviewTemplateResponseWrapper>(
        SAMPLE_GET_REVIEW_TEMPLATE_RESULT_SUCCESS_EMPTY
    ).getSuccessData<ProductrevGetReviewTemplateResponseWrapper>()

    protected val getReviewTemplateUseCaseResultSuccessNonEmpty = createSuccessResponse<ProductrevGetReviewTemplateResponseWrapper>(
        SAMPLE_GET_REVIEW_TEMPLATE_RESULT_SUCCESS_NON_EMPTY
    ).getSuccessData<ProductrevGetReviewTemplateResponseWrapper>()

    protected val getProductIncentiveOvoUseCaseResultSuccessNoIncentive = createSuccessResponse<ProductRevIncentiveOvoDomain>(
        SAMPLE_GET_PRODUCT_INCENTIVE_OVO_RESULT_SUCCESS_NO_INCENTIVE
    ).getSuccessData<ProductRevIncentiveOvoDomain>()


    protected val getProductIncentiveOvoUseCaseResultSuccessIncentive = createSuccessResponse<ProductRevIncentiveOvoDomain>(
        SAMPLE_GET_PRODUCT_INCENTIVE_OVO_RESULT_SUCCESS_INCENTIVE
    ).getSuccessData<ProductRevIncentiveOvoDomain>()

    protected val getProductIncentiveOvoUseCaseResultSuccessChallenge = createSuccessResponse<ProductRevIncentiveOvoDomain>(
        SAMPLE_GET_PRODUCT_INCENTIVE_OVO_RESULT_SUCCESS_CHALLENGE
    ).getSuccessData<ProductRevIncentiveOvoDomain>()

    protected val getBadRatingCategoryUseCaseResultSuccessNonEmpty = createSuccessResponse<BadRatingCategoriesResponse>(
        SAMPLE_GET_BAD_RATING_CATEGORY_RESULT_SUCCESS_NON_EMPTY
    ).getSuccessData<BadRatingCategoriesResponse>()

    protected val submitReviewResultSuccess = createSuccessResponse<ProductrevSubmitReviewResponseWrapper>(
        SAMPLE_SUBMIT_REVIEW_RESULT_SUCCESS
    ).getSuccessData<ProductrevSubmitReviewResponseWrapper>()

    protected val getPostSubmitBottomSheetResultSuccessShowBottomSheet = createSuccessResponse<ProductrevGetPostSubmitBottomSheetResponseWrapper>(
        SAMPLE_GET_POST_SUBMIT_BOTTOM_SHEET_RESULT_SUCCESS_SHOW_BOTTOM_SHEET
    ).getSuccessData<ProductrevGetPostSubmitBottomSheetResponseWrapper>()

    protected val getPostSubmitBottomSheetResultSuccessShowToaster = createSuccessResponse<ProductrevGetPostSubmitBottomSheetResponseWrapper>(
        SAMPLE_GET_POST_SUBMIT_BOTTOM_SHEET_RESULT_SUCCESS_SHOW_TOASTER
    ).getSuccessData<ProductrevGetPostSubmitBottomSheetResponseWrapper>()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockIsVideoFormat()
        mockUserSession()
        mockkStatic(RemoteConfigInstance::class)
        mockStringAb("review_inspiration", "experiment_variant", "experiment_variant")
        setShouldRunReviewTopicsPeekAnimation()
        mockkObject(ErrorHandler)
    }

    private fun mockIsVideoFormat() {
        mockkStatic(::isVideoFormat)
        every { isVideoFormat(any()) } answers {
            !(args.first() as String).endsWith(".jpg")
        }
    }

    private fun mockUserSession() {
        every { userSessionInterface.userId } returns SAMPLE_USER_ID
        every { userSessionInterface.name } returns SAMPLE_USER_NAME
    }

    protected fun setInitialData(
        rating: Int = 5,
        reputationID: String = SAMPLE_REPUTATION_ID,
        productID: String = SAMPLE_PRODUCT_ID,
        utmSource: String = SAMPLE_UTM_SOURCE
    ) {
        viewModel.setRating(rating)
        viewModel.setReputationId(reputationID)
        viewModel.setProductId(productID)
        viewModel.setUtmSource(utmSource)
    }

    protected fun mockSuccessGetReputationForm(
        response: ProductRevGetForm = getReputationFormUseCaseResultSuccessValidWithNonEmptyKeywords
    ) {
        coEvery { getProductReputationForm.getReputationForm(any()) } returns response
    }

    protected fun mockErrorGetReputationForm() {
        coEvery { getProductReputationForm.getReputationForm(any()) } throws Exception()
    }

    protected fun mockErrorGetReviewTemplate() {
        coEvery { getReviewTemplatesUseCase.executeOnBackground() } throws Exception()
    }

    protected fun mockSuccessGetReviewTemplate(
        response: ProductrevGetReviewTemplateResponseWrapper = getReviewTemplateUseCaseResultSuccessEmpty
    ) {
        coEvery { getReviewTemplatesUseCase.executeOnBackground() } returns response
    }

    protected fun mockSuccessGetProductIncentiveOvo(
        response: ProductRevIncentiveOvoDomain? = getProductIncentiveOvoUseCaseResultSuccessIncentive
    ) {
        coEvery { getProductIncentiveOvo.getIncentiveOvo(any(), any()) } returns response
    }

    protected fun mockErrorGetBadRatingCategory() {
        coEvery { getBadRatingCategoryUseCase.executeOnBackground() } throws Exception()
    }

    protected fun mockSuccessGetBadRatingCategory(
        response: BadRatingCategoriesResponse = getBadRatingCategoryUseCaseResultSuccessNonEmpty
    ) {
        coEvery { getBadRatingCategoryUseCase.executeOnBackground() } returns response
    }

    protected fun mockSuccessUploadMedia(
        uploadID: String = SAMPLE_UPLOAD_ID,
        videoUrl: String = SAMPLE_VIDEO_URL
    ) {
        coEvery {
            uploaderUseCase(any())
        } returns UploadResult.Success(
            uploadId = uploadID,
            videoUrl = videoUrl
        )
    }

    protected fun mockErrorUploadMedia(withException: Boolean = true, delayTime: Long = 0L) {
        coEvery { uploaderUseCase(any()) } coAnswers {
            delay(delayTime)
            if (withException) throw Exception() else UploadResult.Error("")
        }
    }

    protected fun mockSuccessSubmitReview(
        response: ProductrevSubmitReviewResponseWrapper = submitReviewResultSuccess
    ) {
        coEvery { submitReviewUseCase.executeOnBackground() } returns response
    }

    protected fun mockErrorSubmitReview() {
        coEvery { submitReviewUseCase.executeOnBackground() } throws Exception()
    }

    protected fun mockSuccessPostSubmitBottomSheet(
        response: ProductrevGetPostSubmitBottomSheetResponseWrapper = getPostSubmitBottomSheetResultSuccessShowBottomSheet
    ) {
        coEvery { getPostSubmitBottomSheetUseCase.executeOnBackground() } returns response
    }

    protected fun mockErrorPostSubmitBottomSheet() {
        coEvery { getPostSubmitBottomSheetUseCase.executeOnBackground() } throws Exception()
    }

    protected fun mockStringAb(key: String, defaultValue: String, value: String) {
        every { RemoteConfigInstance.getInstance() } returns mockk(relaxed = true) {
            val mockAbTestPlatform = mockk<AbTestPlatform>(relaxed = true) {
                every { getString(key, defaultValue) } returns value
            }
            every { abTestPlatform } returns mockAbTestPlatform
        }
    }

    protected fun mockErrorHandler() {
        every {
            ErrorHandler.getErrorMessagePair(any(), any(), any())
        } returns Pair("", ERROR_CODE)
    }

    protected fun setShouldRunReviewTopicsPeekAnimation() {
        every {
            cacheManager.get(
                customId = "cacheKeyIsReviewTopicsPeekAnimationAlreadyRun",
                type = Boolean::class.java,
                defaultValue = false
            )
        } returns false
    }

    protected fun runCollectingStateFlows(
        setupMock: () -> Unit = {
            mockSuccessGetReputationForm()
            mockSuccessGetBadRatingCategory()
            mockSuccessGetReviewTemplate()
            mockSuccessGetProductIncentiveOvo()
            mockSuccessUploadMedia()
            mockSuccessSubmitReview()
            mockSuccessPostSubmitBottomSheet()
        },
        block: (List<CreateReviewToasterUiModel<Any>>) -> Unit
    ) {
        val toasterQueue = mutableListOf<CreateReviewToasterUiModel<Any>>()
        val collectorJobs = mutableListOf<Job>()
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        setupMock()
        viewModel = CreateReviewViewModel(
            CoroutineTestDispatchersProvider,
            getProductReputationForm,
            getProductIncentiveOvo,
            getReviewDetailUseCase,
            submitReviewUseCase,
            uploaderUseCase,
            editReviewUseCase,
            userSessionInterface,
            getReviewTemplatesUseCase,
            getBadRatingCategoryUseCase,
            getPostSubmitBottomSheetUseCase,
            cacheManager
        )
        collectorJobs.add(scope.launch { viewModel.createReviewBottomSheetUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.productCardUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.ratingUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.tickerUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.textAreaTitleUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.badRatingCategoriesUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.topicsUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.textAreaUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.templateUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.mediaPickerUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.anonymousUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.progressBarUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.submitButtonUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.incentiveBottomSheetUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.textAreaBottomSheetUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.postSubmitBottomSheetUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.anonymousInfoBottomSheetUiState.collect {} })
        collectorJobs.add(scope.launch { viewModel.toasterQueue.toList(toasterQueue) })
        block(toasterQueue)
        collectorJobs.onEach { it.cancel() }
    }
}
