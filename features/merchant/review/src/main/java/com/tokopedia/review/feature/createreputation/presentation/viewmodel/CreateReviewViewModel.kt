package com.tokopedia.review.feature.createreputation.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.review.R
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.common.extension.combine
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.domain.RequestState
import com.tokopedia.review.feature.createreputation.domain.usecase.GetBadRatingCategoryUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductReputationForm
import com.tokopedia.review.feature.createreputation.domain.usecase.GetReviewTemplatesUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevEditReviewUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevGetPostSubmitBottomSheetUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevSubmitReviewUseCase
import com.tokopedia.review.feature.createreputation.model.BadRatingCategory
import com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponse
import com.tokopedia.review.feature.createreputation.model.ProductrevSubmitReviewResponseWrapper
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.old.CreateReviewBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewMediaUploadResult
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewProgressBarState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewStringRes
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewTextAreaTextUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.PostSubmitUiState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewBadRatingCategoryUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewTemplateItemUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewAnonymousUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewBadRatingCategoriesUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewBadRatingCategoryUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewIncentiveBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewProductCardUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewProgressBarUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewRatingUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewSubmitButtonUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTemplateItemUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTemplateUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaTitleUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTickerUiState
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.old.CreateReviewViewModel
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.TncBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.model.IncentiveOvoBottomSheetUiModel
import com.tokopedia.review.feature.ovoincentive.usecase.GetProductIncentiveOvo
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

typealias ReviewFormRequestState = RequestState<ProductRevGetForm, Nothing>
typealias ReviewFormRequestSuccessState = RequestState.Success<ProductRevGetForm, Nothing>
typealias IncentiveOvoRequestState = RequestState<ProductRevIncentiveOvoDomain?, Nothing>
typealias IncentiveOvoRequestSuccessState = RequestState.Success<ProductRevIncentiveOvoDomain?, Nothing>
typealias ReviewTemplateRequestState = RequestState<List<CreateReviewTemplate>, Nothing>
typealias ReviewTemplateRequestSuccessState = RequestState.Success<List<CreateReviewTemplate>, Nothing>
typealias BadRatingCategoriesRequestState = RequestState<List<BadRatingCategory>, Nothing>
typealias BadRatingCategoriesRequestSuccessState = RequestState.Success<List<BadRatingCategory>, Nothing>
typealias SubmitReviewRequestState = RequestState<ProductrevSubmitReviewResponseWrapper, Nothing>
typealias PostSubmitReviewRequestState = RequestState<ProductrevGetPostSubmitBottomSheetResponse?, ProductrevGetPostSubmitBottomSheetUseCase.PostSubmitReviewRequestParams>
typealias MediaUploadResultMap = Map<String, CreateReviewMediaUploadResult>
typealias MediaUploadJobMap = Map<String, Job>

@ExperimentalCoroutinesApi
@Suppress("UNUSED_PARAMETER")
class CreateReviewViewModel @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatchers,
    private val getProductReputationForm: GetProductReputationForm,
    private val getProductIncentiveOvo: GetProductIncentiveOvo,
    private val getReviewDetailUseCase: ProductrevGetReviewDetailUseCase,
    private val submitReviewUseCase: ProductrevSubmitReviewUseCase,
    private val uploaderUseCase: UploaderUseCase,
    private val editReviewUseCase: ProductrevEditReviewUseCase,
    private val userSessionInterface: UserSessionInterface,
    private val getReviewTemplatesUseCase: GetReviewTemplatesUseCase,
    private val getBadRatingCategoryUseCase: GetBadRatingCategoryUseCase,
    private val getPostSubmitBottomSheetUseCase: ProductrevGetPostSubmitBottomSheetUseCase,
) : BaseViewModel(coroutineDispatcherProvider.main) {

    companion object {
        private const val STATE_FLOW_TIMEOUT_MILLIS = 5000L
        private const val MAX_MEDIA_COUNT = 5
        private const val GOOD_RATING_THRESHOLD = 2

        private const val SAVED_STATE_RATING = "savedStateRating"
        private const val SAVED_STATE_REVIEW_TEXT = "savedStateReviewText"
        private const val SAVED_STATE_ANONYMOUS = "savedStateAnonymous"
        private const val SAVED_STATE_MEDIA_URIS = "savedStateMediaUris"
        private const val SAVED_STATE_MEDIA_UPLOAD_RESULTS = "savedStateUploadResults"
        private const val SAVED_STATE_EDIT_MODE = "savedStateEditMode"
        private const val SAVED_STATE_PRODUCT_ID = "savedStateProductId"
        private const val SAVED_STATE_SHOP_ID = "savedStateShopId"
        private const val SAVED_STATE_ORDER_ID = "savedStateOrderId"
        private const val SAVED_STATE_FEEDBACK_ID = "savedStateFeedbackId"
        private const val SAVED_STATE_REPUTATION_ID = "savedStateReputationId"
        private const val SAVED_STATE_UTM_SOURCE = "savedStateUtmSource"
        private const val SAVED_STATE_SENDING_REVIEW = "savedStateSendingReview"
        private const val SAVED_STATE_REVIEW_FORM = "savedStateReviewForm"
        private const val SAVED_STATE_INCENTIVE_OVO = "savedStateIncentiveOvo"
        private const val SAVED_STATE_REVIEW_TEMPLATE = "savedStateReviewTemplate"
        private const val SAVED_STATE_BAD_RATING_CATEGORIES = "savedStateBadRatingCategories"
        private const val SAVED_STATE_SUBMIT_REVIEW_RESULT = "savedStateSubmitReviewResult"
        private const val SAVED_STATE_POST_SUBMIT_REVIEW_RESULT = "savedStatePostSubmitReviewResult"
        private const val SAVED_STATE_SHOULD_SHOW_INCENTIVE_BOTTOM_SHEET = "savedStateShowIncentiveBottomSheet"
        private const val SAVED_STATE_SHOULD_SHOW_TEXT_AREA_BOTTOM_SHEET = "savedStateShowTextAreaBottomSheet"
    }

    // region state that need to be saved and restored
    private val rating = MutableStateFlow(Int.ZERO)
    private val reviewText = MutableStateFlow(CreateReviewTextAreaTextUiModel())
    private val anonymous = MutableStateFlow(false)
    private val mediaUris = MutableStateFlow<List<String>>(emptyList())
    private val reputationScore = MutableStateFlow(Int.ZERO)
    private val mediaUploadResults = MutableStateFlow<MediaUploadResultMap>(mapOf())
    private val editMode = MutableStateFlow(false)
    private val productId = MutableStateFlow("")
    private val shopId = MutableStateFlow("")
    private val orderId = MutableStateFlow("")
    private val feedbackId = MutableStateFlow("")
    private val reputationId = MutableStateFlow("")
    private val utmSource = MutableStateFlow("")
    private val sendingReview = MutableStateFlow(false)
    private val reviewForm = MutableStateFlow<ReviewFormRequestState>(RequestState.Idle)
    private val incentiveOvo = MutableStateFlow<IncentiveOvoRequestState>(RequestState.Idle)
    private val reviewTemplate = MutableStateFlow<ReviewTemplateRequestState>(RequestState.Idle)
    private val badRatingCategories = MutableStateFlow<BadRatingCategoriesRequestState>(RequestState.Idle)
    private val submitReviewResult = MutableStateFlow<SubmitReviewRequestState>(RequestState.Idle)
    private val postSubmitReviewResult = MutableStateFlow<PostSubmitReviewRequestState>(RequestState.Idle)
    private val shouldShowIncentiveBottomSheet = MutableStateFlow(false)
    private val shouldShowTextAreaBottomSheet = MutableStateFlow(false)
    // endregion state that need to be saved and restored

    // region state that must not be saved nor restored
    private val mediaUploadJobs = MutableStateFlow<MediaUploadJobMap>(mapOf())
    private val textAreaHasFocus = MutableStateFlow(false)
    private val reviewTemplatesAnimating = MutableStateFlow(false)
    private val shouldResetFailedUploadStatus = MutableStateFlow(false)
    // endregion state that must not be saved nor restored

    // region state whose it's value is determined by other states

    // region misc state
    private val canRenderForm = combine(
        reviewForm.filterIsInstance(), incentiveOvo, reviewTemplate.filterIsInstance(),
        badRatingCategories.filterIsInstance(), ::mapCanRenderForm
    ).toStateFlow(false)
    private val isGoodRating = rating.mapLatest(::mapIsGoodRating).toStateFlow(false)
    private val hasIncentive = incentiveOvo.filterIsInstance<IncentiveOvoRequestSuccessState>()
        .mapLatest(::mapHasIncentive).toStateFlow(false)
    private val hasOngoingChallenge = incentiveOvo
        .filterIsInstance<IncentiveOvoRequestSuccessState>().mapLatest(::mapHasOngoingChallenge)
        .toStateFlow(false)
    private val isAnyBadRatingCategorySelected = badRatingCategories.mapLatest(
        ::mapIsAnyBadRatingCategorySelected
    ).toStateFlow(false)
    private val isOnlyBadRatingOtherCategorySelected = badRatingCategories.mapLatest(
        ::mapIsOnlyBadRatingOtherCategorySelected
    ).toStateFlow(false)
    private val isBadRatingOtherCategorySelected = badRatingCategories.mapLatest(
        ::mapIsBadRatingOtherCategorySelected
    ).toStateFlow(false)
    // endregion misc state

    // region product card state
    val productCardUiState = combine(
        canRenderForm, reviewForm.filterIsInstance(), ::mapProductCardUiState
    ).toStateFlow(CreateReviewProductCardUiState.Loading)
    // endregion product card state

    // region rating widget state
    val ratingUiState = combine(
        canRenderForm, rating, orderId, productId, editMode, feedbackId, ::mapRatingUiState
    ).toStateFlow(CreateReviewRatingUiState.Loading)
    // endregion rating widget state

    // region ticker widget state
    val tickerUiState = combine(
        canRenderForm, incentiveOvo.filterIsInstance(), reputationId, orderId, productId,
        hasIncentive, hasOngoingChallenge, ::mapTickerUiState
    ).toStateFlow(CreateReviewTickerUiState.Hidden)
    // endregion ticker widget state

    // region text area title state
    val textAreaTitleUiState = combine(
        canRenderForm, rating, ::mapTextAreaTitleUiState
    ).toStateFlow(CreateReviewTextAreaTitleUiState.Loading)
    // endregion text area title state

    // region bad rating categories state
    val badRatingCategoriesUiState = combine(
        canRenderForm, isGoodRating, orderId, productId, badRatingCategories.filterIsInstance(),
        ::mapBadRatingCategoriesUiState
    ).toStateFlow(CreateReviewBadRatingCategoriesUiState.Loading)
    // endregion bad rating categories state

    // region text area state
    private val textAreaHint = combine(
        rating, isOnlyBadRatingOtherCategorySelected, badRatingCategoriesUiState, ::mapTextAreaHint
    ).toStateFlow(CreateReviewStringRes())
    private val textAreaHelper = combine(
        reviewText, hasIncentive, hasOngoingChallenge, textAreaHasFocus, ::mapTextAreaHelper
    ).toStateFlow(CreateReviewStringRes())
    val textAreaUiState = combine(
        canRenderForm, reviewText, textAreaHint, textAreaHelper, textAreaHasFocus,
        ::mapTextAreaUiState
    ).toStateFlow(CreateReviewTextAreaUiState.Loading)
    // endregion text area state

    // region template state
    val templateUiState = combine(
        canRenderForm, isGoodRating, reviewTemplatesAnimating, reviewTemplate.filterIsInstance(),
        ::mapTemplateUiState
    ).toStateFlow(CreateReviewTemplateUiState.Loading)
    // endregion template state

    // region media picker state
    private val mediaItems = combine(
        mediaUris, mediaUploadResults, mediaUploadJobs, ::mapMediaItems
    ).toStateFlow(emptyList())
    val mediaPickerUiState = combine(
        canRenderForm, mediaItems, ::mapMediaPickerUiState
    ).toStateFlow(CreateReviewMediaPickerUiState.Loading)
    // endregion media picker state

    // region anonymous widget state
    val anonymousUiState = combine(
        canRenderForm, anonymous, orderId, productId, editMode, feedbackId, ::mapAnonymousUiState
    ).toStateFlow(CreateReviewAnonymousUiState.Loading)
    // endregion anonymous widget state

    // region progress bar state
    private val progressBarState = combine(
        isGoodRating, mediaPickerUiState, reviewText, isAnyBadRatingCategorySelected,
        badRatingCategoriesUiState, ::mapProgressBarState
    ).toStateFlow(CreateReviewProgressBarState())
    val progressBarUiState = combine(
        canRenderForm, progressBarState, ::mapProgressBarUiState
    ).toStateFlow(CreateReviewProgressBarUiState.Loading)
    // endregion progress bar state

    // region submit button state
    val submitButtonUiState = combine(
        canRenderForm, isGoodRating, reviewText, isBadRatingOtherCategorySelected,
        isAnyBadRatingCategorySelected, badRatingCategoriesUiState, sendingReview, ::mapSubmitButtonUiState
    ).toStateFlow(CreateReviewSubmitButtonUiState.Loading)
    // endregion submit button state

    // region incentive bottom sheet state
    val incentiveBottomSheetUiState = combine(
        canRenderForm, shouldShowIncentiveBottomSheet, incentiveOvo.filterIsInstance(),
        reputationId, productId, orderId, ::mapIncentiveBottomSheetUiState
    ).toStateFlow(CreateReviewIncentiveBottomSheetUiState.Hidden)
    // endregion incentive bottom sheet state

    // region text area bottom sheet state
    val textAreaBottomSheetUiState = combine(
        canRenderForm, shouldShowTextAreaBottomSheet, ::mapTextAreaBottomSheetUiState
    ).toStateFlow(CreateReviewTextAreaBottomSheetUiState.Hidden)
    // endregion text area bottom sheet state

    // region post submit bottom sheet state
    val postSubmitBottomSheetUiState = postSubmitReviewResult.mapLatest(::mapPostSubmitUiState)
        .toStateFlow(PostSubmitUiState.Hidden)
    // endregion post submit bottom sheet state

    // endregion state whose it's value is determined by other states

    init {
        observeMediaUrisForUpload()
        observeMediaTemplatesToAppend()
        observeShouldSubmitReview()
        observeShouldLoadPostSubmitReviewData()
        observeShouldLoadForm()
        observeShouldLoadIncentiveOvo()
        observeShouldLoadReviewTemplates()
        observeShouldLoadBadRatingCategories()
    }

    // region extensions
    private inline fun <reified T> Flow<T>.toStateFlow(initialValue: T) = stateIn(
        scope = this@CreateReviewViewModel,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = initialValue
    )
    // endregion extensions

    // region state mapper
    @Suppress("UNUSED_PARAMETER")
    private fun mapCanRenderForm(
        reviewForm: ReviewFormRequestSuccessState,
        incentiveOvo: IncentiveOvoRequestState,
        reviewTemplates: ReviewTemplateRequestSuccessState,
        badRatingCategories: BadRatingCategoriesRequestSuccessState
    ): Boolean {
        val isReviewFormValid = reviewForm.result.productrevGetForm.validToReview &&
                reviewForm.result.productrevGetForm.productData.productStatus != Int.ZERO
        val isIncentiveOvoLoaded = incentiveOvo is RequestState.CompleteRequestState
        return isReviewFormValid && isIncentiveOvoLoaded
    }

    private fun mapIsGoodRating(rating: Int): Boolean {
        return rating > GOOD_RATING_THRESHOLD
    }

    private fun mapMediaItems(
        mediaUris: List<String>,
        mediaUploadResults: MediaUploadResultMap,
        mediaUploadJobs: MediaUploadJobMap
    ): List<CreateReviewMediaUiModel> {
        return mutableListOf<CreateReviewMediaUiModel>().apply {
            includeAddMediaUiModel(mediaUris)
            includeMediaItems(mediaUris, mediaUploadResults, mediaUploadJobs)
        }
    }

    private fun mapIsAnyBadRatingCategorySelected(
        badRatingCategories: BadRatingCategoriesRequestState
    ): Boolean {
        return if (badRatingCategories is BadRatingCategoriesRequestSuccessState) {
            badRatingCategories.result.any { category -> category.selected }
        } else false
    }

    private fun mapIsOnlyBadRatingOtherCategorySelected(
        badRatingCategories: BadRatingCategoriesRequestState
    ): Boolean {
        return if (badRatingCategories is BadRatingCategoriesRequestSuccessState) {
            badRatingCategories.result.find { category ->
                category.id == CreateReviewBottomSheet.BAD_RATING_OTHER_ID
            }?.selected == true && badRatingCategories.result.count { category ->
                category.selected
            } == 1
        } else false
    }

    private fun mapIsBadRatingOtherCategorySelected(
        badRatingCategories: BadRatingCategoriesRequestState
    ): Boolean {
        return if (badRatingCategories is BadRatingCategoriesRequestSuccessState) {
            badRatingCategories.result.find { category ->
                category.id == CreateReviewBottomSheet.BAD_RATING_OTHER_ID
            }?.selected == true
        } else false
    }

    private fun mapHasIncentive(
        incentiveOvo: IncentiveOvoRequestSuccessState
    ): Boolean {
        return incentiveOvo.result?.productrevIncentiveOvo?.amount.isMoreThanZero()
    }

    private fun mapHasOngoingChallenge(
        incentiveOvo: IncentiveOvoRequestSuccessState
    ): Boolean {
        return incentiveOvo.result?.productrevIncentiveOvo?.amount.isZero()
    }

    private fun mapTextAreaHint(
        rating: Int,
        isOnlyBadRatingOtherCategorySelected: Boolean,
        badRatingCategoriesUiState: CreateReviewBadRatingCategoriesUiState
    ): CreateReviewStringRes {
        val badRatingCategoriesShowing = badRatingCategoriesUiState is CreateReviewBadRatingCategoriesUiState.Showing
        return if (rating in CreateReviewFragment.RATING_1..CreateReviewFragment.RATING_2) {
            if (badRatingCategoriesShowing) {
                if (isOnlyBadRatingOtherCategorySelected) {
                    CreateReviewStringRes(R.string.review_form_bad_helper_must_fill)
                } else {
                    CreateReviewStringRes(R.string.review_form_bad_helper)
                }
            } else {
                // for rating between 1 to 2 we expect bad rating categories to be showing but
                // it isn't showing yet, so we need to wait for it to be showing
                textAreaHint.value
            }
        } else if (rating == CreateReviewFragment.RATING_3) {
            CreateReviewStringRes(R.string.review_form_neutral_helper)
        } else {
            CreateReviewStringRes(R.string.review_form_good_helper)
        }
    }
    private fun mapTextAreaHelper(
        reviewTextAreaTextUiModel: CreateReviewTextAreaTextUiModel,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean,
        textAreaHasFocus: Boolean
    ): CreateReviewStringRes {
        val stringResId = if (textAreaHasFocus) {
            when {
                reviewTextAreaTextUiModel.text.length in 1 until CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD -> {
                    R.string.review_create_bottom_sheet_text_area_partial_incentive
                }
                reviewTextAreaTextUiModel.text.length >= CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD -> {
                    R.string.review_create_bottom_sheet_text_area_eligible_for_incentive
                }
                else -> {
                    R.string.review_create_bottom_sheet_text_area_empty_incentive
                }
            }
        } else {
            Int.ZERO
        }
        return CreateReviewStringRes(stringResId)
    }

    private fun mapProgressBarState(
        isGoodRating: Boolean,
        mediaPickerUiState: CreateReviewMediaPickerUiState,
        reviewTextAreaTextUiModel: CreateReviewTextAreaTextUiModel,
        isAnyBadRatingCategorySelected: Boolean,
        badRatingCategoriesUiState: CreateReviewBadRatingCategoriesUiState
    ): CreateReviewProgressBarState {
        val mediaNotEmpty = mediaPickerUiState is CreateReviewMediaPickerUiState.Showing && mediaPickerUiState.mediaItems.size > 1
        val textAreaFilled = reviewTextAreaTextUiModel.text.isNotBlank()
        val badRatingCategoriesShowed = badRatingCategoriesUiState is CreateReviewBadRatingCategoriesUiState.Showing
        return CreateReviewProgressBarState(
            isGoodRating = isGoodRating,
            isPhotosFilled = mediaNotEmpty,
            isTextAreaFilled = textAreaFilled,
            isBadRatingReasonSelected = badRatingCategoriesShowed && isAnyBadRatingCategorySelected
        )
    }

    private fun mapProductCardUiState(
        canRenderForm: Boolean,
        reviewForm: ReviewFormRequestSuccessState
    ): CreateReviewProductCardUiState {
        return if (canRenderForm) {
            CreateReviewProductCardUiState.Showing(reviewForm.result.productrevGetForm.productData)
        } else CreateReviewProductCardUiState.Loading
    }

    private fun mapRatingUiState(
        canRenderForm: Boolean,
        rating: Int,
        orderId: String,
        productId: String,
        editMode: Boolean,
        feedbackId: String
    ): CreateReviewRatingUiState {
        return if (canRenderForm) {
            CreateReviewRatingUiState.Showing(
                rating = rating,
                trackerData = CreateReviewRatingUiState.Showing.TrackerData(
                    orderId, productId, editMode, feedbackId
                )
            )
        } else CreateReviewRatingUiState.Loading
    }

    private fun mapTickerUiState(
        canRenderForm: Boolean,
        incentiveOvo: IncentiveOvoRequestSuccessState,
        reputationId: String,
        orderId: String,
        productId: String,
        hasIncentive: Boolean,
        hasOngoingChallenge: Boolean
    ): CreateReviewTickerUiState {
        val ticker = incentiveOvo.result?.productrevIncentiveOvo?.ticker
        return if (canRenderForm && ticker != null) {
            val trackerData = CreateReviewTickerUiState.Showing.TrackerData(
                incentiveOvo.result,
                reputationId,
                orderId,
                productId,
                userSessionInterface.userId,
                hasIncentive,
                hasOngoingChallenge
            )
            CreateReviewTickerUiState.Showing(ticker, trackerData)
        } else CreateReviewTickerUiState.Hidden
    }

    private fun mapTextAreaTitleUiState(
        canRenderForm: Boolean,
        rating: Int
    ): CreateReviewTextAreaTitleUiState {
        return if (canRenderForm) {
            val stringResId = when (rating) {
                CreateReviewFragment.RATING_1 -> R.string.review_create_worst_title
                CreateReviewFragment.RATING_2 -> R.string.review_form_bad_title
                CreateReviewFragment.RATING_3 -> R.string.review_form_neutral_title
                else -> R.string.review_create_best_title
            }
            CreateReviewTextAreaTitleUiState.Showing(CreateReviewStringRes(stringResId))
        } else CreateReviewTextAreaTitleUiState.Loading
    }

    private fun mapBadRatingCategoriesUiState(
        canRenderForm: Boolean,
        isGoodRating: Boolean,
        orderId: String,
        productId: String,
        badRatingCategories: BadRatingCategoriesRequestSuccessState
    ): CreateReviewBadRatingCategoriesUiState {
        val trackerData = CreateReviewBadRatingCategoriesUiState.Showing.TrackerData(orderId, productId, userSessionInterface.userId)
        return if (canRenderForm && !isGoodRating && badRatingCategories.result.isNotEmpty()) {
            CreateReviewBadRatingCategoriesUiState.Showing(
                badRatingCategories.result.map {
                   CreateReviewBadRatingCategoryUiModel(CreateReviewBadRatingCategoryUiState.Showing(it))
                },
                trackerData
            )
        } else CreateReviewBadRatingCategoriesUiState.Hidden(
            badRatingCategories.result.map {
               CreateReviewBadRatingCategoryUiModel(CreateReviewBadRatingCategoryUiState.Hidden(it))
            },
            trackerData
        )
    }

    private fun mapTextAreaUiState(
        canRenderForm: Boolean,
        textAreaTextAreaTextUiModel: CreateReviewTextAreaTextUiModel,
        textAreaHint: CreateReviewStringRes,
        textAreaHelper: CreateReviewStringRes,
        textAreaHasFocus: Boolean
    ): CreateReviewTextAreaUiState {
        return if (canRenderForm) {
            CreateReviewTextAreaUiState.Showing(
                textAreaTextAreaTextUiModel,
                textAreaHint,
                textAreaHelper,
                textAreaHasFocus
            )
        } else CreateReviewTextAreaUiState.Loading
    }

    private fun mapTemplateUiState(
        canRenderForm: Boolean,
        isGoodRating: Boolean,
        reviewTemplatesAnimating: Boolean,
        reviewTemplates: ReviewTemplateRequestSuccessState
    ): CreateReviewTemplateUiState {
        val currentTemplates = templateUiState.value.templates
        val templates = if (canRenderForm) {
            getReviewTemplatesToShow(reviewTemplates, isGoodRating)
        } else {
            return CreateReviewTemplateUiState.Loading
        }
        return when {
            reviewTemplatesAnimating -> templateUiState.value
            templates.isEmpty() -> CreateReviewTemplateUiState.Hidden(emptyList())
            currentTemplates != templates -> CreateReviewTemplateUiState.Changing(templates)
            else -> CreateReviewTemplateUiState.Showing(templates)
        }
    }

    private fun mapMediaPickerUiState(
        canRenderForm: Boolean,
        mediaItems: List<CreateReviewMediaUiModel>
    ): CreateReviewMediaPickerUiState {
        return if (canRenderForm) CreateReviewMediaPickerUiState.Showing(mediaItems)
        else CreateReviewMediaPickerUiState.Loading
    }

    private fun mapAnonymousUiState(
        canRenderForm: Boolean,
        anonymous: Boolean,
        orderId: String,
        productId: String,
        editMode: Boolean,
        feedbackId: String
    ): CreateReviewAnonymousUiState {
        return if (canRenderForm) {
            CreateReviewAnonymousUiState.Showing(
                anonymous,
                CreateReviewAnonymousUiState.Showing.TrackerData(
                    orderId, productId, editMode, feedbackId
                )
            )
        } else CreateReviewAnonymousUiState.Loading
    }

    private fun mapProgressBarUiState(
        canRenderForm: Boolean,
        progressBarState: CreateReviewProgressBarState
    ): CreateReviewProgressBarUiState {
        return when {
            canRenderForm -> CreateReviewProgressBarUiState.Showing(progressBarState)
            else -> CreateReviewProgressBarUiState.Loading
        }
    }

    private fun mapSubmitButtonUiState(
        canRenderForm: Boolean,
        isGoodRating: Boolean,
        reviewTextAreaTextUiModel: CreateReviewTextAreaTextUiModel,
        badRatingOtherCategorySelected: Boolean,
        anyBadRatingOtherCategorySelected: Boolean,
        badRatingCategoriesUiState: CreateReviewBadRatingCategoriesUiState,
        sendingReview: Boolean
    ): CreateReviewSubmitButtonUiState {
        val badRatingCategoriesShowing = badRatingCategoriesUiState is CreateReviewBadRatingCategoriesUiState.Showing
        return if (canRenderForm) {
            if (sendingReview) {
                CreateReviewSubmitButtonUiState.Sending
            } else if (isGoodRating) {
                CreateReviewSubmitButtonUiState.Enabled
            } else {
                if (badRatingCategoriesShowing && badRatingOtherCategorySelected) {
                    if (reviewTextAreaTextUiModel.text.isNotBlank()) {
                        CreateReviewSubmitButtonUiState.Enabled
                    } else {
                        CreateReviewSubmitButtonUiState.Disabled
                    }
                } else {
                    if (badRatingCategoriesShowing && anyBadRatingOtherCategorySelected) {
                        CreateReviewSubmitButtonUiState.Enabled
                    } else {
                        CreateReviewSubmitButtonUiState.Disabled
                    }
                }
            }
        } else CreateReviewSubmitButtonUiState.Loading
    }

    private fun mapIncentiveBottomSheetUiState(
        canRenderForm: Boolean,
        showIncentiveBottomSheet: Boolean,
        incentiveOvo: IncentiveOvoRequestSuccessState,
        reputationId: String,
        productId: String,
        orderId: String
    ): CreateReviewIncentiveBottomSheetUiState {
        return if (canRenderForm && showIncentiveBottomSheet) {
            incentiveOvo.result?.let {
                val data = IncentiveOvoBottomSheetUiModel(
                    productRevIncentiveOvoDomain = it,
                    trackerData = TncBottomSheetTrackerData(
                        reputationId, productId, orderId, userSessionInterface.userId
                    )
                )
                CreateReviewIncentiveBottomSheetUiState.Showing(data)
            } ?: CreateReviewIncentiveBottomSheetUiState.Hidden
        } else CreateReviewIncentiveBottomSheetUiState.Hidden
    }

    private fun mapTextAreaBottomSheetUiState(
        canRenderForm: Boolean,
        shouldShowTextAreaBottomSheet: Boolean
    ): CreateReviewTextAreaBottomSheetUiState {
        return if (canRenderForm && shouldShowTextAreaBottomSheet) {
            CreateReviewTextAreaBottomSheetUiState.Showing
        } else CreateReviewTextAreaBottomSheetUiState.Hidden
    }

    private fun mapPostSubmitUiState(
        postSubmitReviewResult: PostSubmitReviewRequestState
    ): PostSubmitUiState {
        return if (postSubmitReviewResult is RequestState.Success) {
            if (postSubmitReviewResult.result?.isShowBottomSheet() == true) {
                PostSubmitUiState.ShowThankYouBottomSheet(
                    data = postSubmitReviewResult.result,
                    hasPendingIncentive = postSubmitReviewResult.params?.isInboxEmpty.orTrue()
                )
            } else {
                PostSubmitUiState.ShowThankYouToaster(postSubmitReviewResult.result)
            }
        } else {
            PostSubmitUiState.Hidden
        }
    }

    private suspend fun mapPostSubmitReviewRequestParams(
        feedbackId: String,
        reviewText: CreateReviewTextAreaTextUiModel,
        mediaPickerUiState: CreateReviewMediaPickerUiState,
        incentiveOvo: IncentiveOvoRequestState,
        submitReviewResult: SubmitReviewRequestState
    ): ProductrevGetPostSubmitBottomSheetUseCase.PostSubmitReviewRequestParams? {
        return if (
            submitReviewResult is RequestState.Success &&
            submitReviewResult.result.productrevSuccessIndicator?.success == true &&
            mediaPickerUiState is CreateReviewMediaPickerUiState.Showing &&
            incentiveOvo is RequestState.Success
        ) {
            val hasPendingIncentive = hasPendingIncentive()
            ProductrevGetPostSubmitBottomSheetUseCase.PostSubmitReviewRequestParams(
                feedbackId = feedbackId,
                reviewText = reviewText.text,
                imagesTotal = mediaPickerUiState.mediaItems.mapNotNull { media ->
                    media.uploadId.takeIf { uploadId -> uploadId.isNotBlank() }
                }.count(),
                isInboxEmpty = hasPendingIncentive.not(),
                incentiveAmount = incentiveOvo.result?.productrevIncentiveOvo?.amount.orZero()
            )
        } else null
    }

    private fun mapSubmitReviewRequestParams(
        sendingReview: Boolean,
        reputationId: String,
        productId: String,
        shopId: String,
        reputationScore: Int,
        rating: Int,
        reviewText: CreateReviewTextAreaTextUiModel,
        anonymous: Boolean,
        mediaPickerUiState: CreateReviewMediaPickerUiState,
        utmSource: String,
        badRatingCategoriesUiState: CreateReviewBadRatingCategoriesUiState
    ): ProductrevSubmitReviewUseCase.SubmitReviewRequestParams? {
        return if (sendingReview && mediaPickerUiState is CreateReviewMediaPickerUiState.Showing) {
            ProductrevSubmitReviewUseCase.SubmitReviewRequestParams(
                reputationId = reputationId,
                productId = productId,
                shopId = shopId,
                reputationScore = reputationScore,
                rating = rating,
                reviewText = reviewText.text,
                isAnonymous = anonymous,
                attachmentIds = mediaPickerUiState.mediaItems.mapNotNull { media ->
                    media.uploadId.takeIf { uploadId -> uploadId.isNotBlank() }
                },
                utmSource = utmSource,
                badRatingCategoryIds = badRatingCategoriesUiState.badRatingCategories.mapNotNull { category ->
                    category.uiState.takeIf {
                        it is CreateReviewBadRatingCategoryUiState.Showing
                    }?.badRatingCategory?.let { if (it.selected) it.id else null }
                }
            )
        } else null
    }

    private fun MutableList<CreateReviewMediaUiModel>.includeAddMediaUiModel(mediaUris: List<String>) {
        if (mediaUris.isEmpty()) {
            add(CreateReviewMediaUiModel.AddLarge)
        } else if (mediaUris.size < MAX_MEDIA_COUNT) {
            add(CreateReviewMediaUiModel.AddSmall)
        }
    }

    private fun MutableList<CreateReviewMediaUiModel>.includeMediaItems(
        mediaUris: List<String>,
        mediaUploadResults: MediaUploadResultMap,
        mediaUploadJobs: MediaUploadJobMap
    ) {
        mediaUris.forEach {
            val uploadResult = mediaUploadResults[it]
            val uploadJob = mediaUploadJobs[it]
            if (uploadJob?.isActive != true) {
                when (uploadResult) {
                    is CreateReviewMediaUploadResult.Success -> {
                        add(CreateReviewMediaUiModel.Image(
                            uri = it,
                            uploadId = uploadResult.uploadId,
                            state = CreateReviewMediaUiModel.State.UPLOADED
                        ))
                    }
                    is CreateReviewMediaUploadResult.Error -> {
                        add(CreateReviewMediaUiModel.Image(
                            uri = it,
                            state = CreateReviewMediaUiModel.State.UPLOAD_FAILED
                        ))
                    }
                    else -> {
                        add(CreateReviewMediaUiModel.Image(
                            uri = it,
                            state = CreateReviewMediaUiModel.State.UPLOADING
                        ))
                    }
                }
            } else {
                add(CreateReviewMediaUiModel.Image(
                    uri = it,
                    state = CreateReviewMediaUiModel.State.UPLOADING
                ))
            }
        }
    }

    private fun getReviewTemplatesToShow(
        reviewTemplates: ReviewTemplateRequestSuccessState,
        goodRating: Boolean
    ): List<CreateReviewTemplateItemUiModel> {
        return if (goodRating) {
            getUnselectedReviewTemplates(reviewTemplates)
        } else {
            emptyList()
        }
    }

    private fun getUnselectedReviewTemplates(
        reviewTemplates: ReviewTemplateRequestSuccessState
    ): List<CreateReviewTemplateItemUiModel> {
        return reviewTemplates.result.takeIf { it.isNotEmpty() }?.let { templates ->
            templates.mapNotNull { template ->
                if (!template.selected) {
                    CreateReviewTemplateItemUiModel(CreateReviewTemplateItemUiState.Showing(template))
                } else null
            }
        }.orEmpty()
    }
    // endregion state mapper

    // region state observer
    private fun observeMediaUrisForUpload() {
        launch {
            combine(
                shouldResetFailedUploadStatus,
                mediaUris
            ) { shouldResetFailedUploadStatus, uris ->
                shouldResetFailedUploadStatus to uris
            }.collectLatest(::tryUploadMedia)
        }
    }

    private fun observeMediaTemplatesToAppend() {
        launch {
            reviewTemplate.filterIsInstance<ReviewTemplateRequestSuccessState>()
                .collect(::appendSelectedTemplatesToReviewText)
        }
    }

    private fun observeShouldSubmitReview() {
        launch {
            combine(
                sendingReview, reputationId, productId, shopId, reputationScore, rating, reviewText,
                anonymous, mediaPickerUiState, utmSource, badRatingCategoriesUiState,
                ::mapSubmitReviewRequestParams
            ).collectLatest(::trySubmitReview)
        }
    }

    private fun observeShouldLoadPostSubmitReviewData() {
        launch {
            combine(
                feedbackId, reviewText, mediaPickerUiState, incentiveOvo, submitReviewResult,
                ::mapPostSubmitReviewRequestParams
            ).collectLatest { it?.let { getPostSubmitReviewData(it) } }
        }
    }

    private fun observeShouldLoadForm() {
        launch {
            combine(
                reputationId, productId, reviewForm, ::mapShouldLoadForm
            ).collectLatest { if (it) getProductReputation() }
        }
    }

    private fun observeShouldLoadIncentiveOvo() {
        launch {
            combine(
                reputationId, productId, incentiveOvo, ::mapShouldLoadIncentiveOvo
            ).collectLatest { if (it) getProductIncentiveOvo() }
        }
    }

    private fun observeShouldLoadReviewTemplates() {
        launch {
            combine(productId, reviewTemplate, ::mapShouldLoadReviewTemplates)
                .collectLatest { if (it) getReviewTemplates() }
        }
    }

    private fun observeShouldLoadBadRatingCategories() {
        launch {
            badRatingCategories.mapLatest{ badRatingCategories ->
                badRatingCategories is RequestState.Idle
            }.collectLatest { if (it) getBadRatingCategories() }
        }
    }
    // endregion state observer

    private suspend fun cancelInvalidUploadJobs(uris: List<String>) {
        mediaUploadJobs.value.forEach { (uri, job) ->
            if (uri !in uris && job.isActive) job.cancelAndJoin()
        }
    }

    private fun startNewUploadMediaJob(uri: String): Job {
        return launchCatchError(block = {
            val filePath = File(uri)
            val params = uploaderUseCase.createParams(
                sourceId = CreateReviewViewModel.CREATE_REVIEW_SOURCE_ID,
                filePath = filePath
            )
            val uploadMediaResult = uploaderUseCase(params).let {
                when (it) {
                    is UploadResult.Success -> CreateReviewMediaUploadResult.Success(it.uploadId, it.videoUrl)
                    is UploadResult.Error -> CreateReviewMediaUploadResult.Error(it.message)
                }
            }
            updateMediaUploadResults(uri, uploadMediaResult)
        }, onError = {
            updateMediaUploadResults(uri, CreateReviewMediaUploadResult.Error(it.message.orEmpty()))
        })
    }

    private fun updateMediaUploadResults(
        uri: String,
        uploadMediaResult: CreateReviewMediaUploadResult
    ) {
        mediaUploadResults.value = mutableMapOf<String, CreateReviewMediaUploadResult>().apply {
            putAll(mediaUploadResults.value)
            put(uri, uploadMediaResult)
        }
    }

    private fun updateMediaUploadJobs(uri: String, newUploadJob: Job) {
        mediaUploadJobs.value = mutableMapOf<String, Job>().apply {
            putAll(mediaUploadJobs.value)
            put(uri, newUploadJob)
        }
    }

    private fun getProductReputation() {
        launchCatchError(block = {
            withContext(coroutineDispatcherProvider.io) {
                reviewForm.value = RequestState.Requesting()
                getProductReputationForm.getReputationForm(
                    GetProductReputationForm.createRequestParam(reputationId.value, productId.value)
                )
            }.run {
                shopId.value = productrevGetForm.shopData.shopIDStr
                productId.value = productrevGetForm.productData.productIDStr
                reviewForm.value = ReviewFormRequestSuccessState(this)
            }
        }) {
            reviewForm.value = RequestState.Error(it)
        }
    }

    private fun getProductIncentiveOvo() {
        launchCatchError(block = {
            withContext(coroutineDispatcherProvider.io) {
                incentiveOvo.value = RequestState.Requesting()
                getProductIncentiveOvo.getIncentiveOvo(productId.value, reputationId.value)
            }.run {
                incentiveOvo.value = IncentiveOvoRequestSuccessState(this)
            }
        }) {
            incentiveOvo.value = RequestState.Error(it)
        }
    }

    private fun getReviewTemplates() {
        launchCatchError(block = {
            withContext(coroutineDispatcherProvider.io) {
                reviewTemplate.value = RequestState.Requesting()
                getReviewTemplatesUseCase.setParams(productId.value, 1)
                getReviewTemplatesUseCase.executeOnBackground()
            }.run {
                reviewTemplate.value = ReviewTemplateRequestSuccessState(
                    productrevGetPersonalizedReviewTemplate.templates.map {
                        CreateReviewTemplate(it, false)
                    }
                )
            }
        }) {
            reviewTemplate.value = RequestState.Error(it)
        }
    }

    private fun getBadRatingCategories() {
        launchCatchError(block = {
            withContext(coroutineDispatcherProvider.io) {
                badRatingCategories.value = RequestState.Requesting()
                getBadRatingCategoryUseCase.executeOnBackground()
            }.run {
                badRatingCategories.value = BadRatingCategoriesRequestSuccessState(
                    productrevGetBadRatingCategory.list
                )
            }
        }) {
            badRatingCategories.value = RequestState.Error(it)
        }
    }

    private suspend fun getPostSubmitReviewData(
        requestParams: ProductrevGetPostSubmitBottomSheetUseCase.PostSubmitReviewRequestParams
    ) {
        try {
            postSubmitReviewResult.value = RequestState.Requesting(requestParams)
            val data = withContext(coroutineDispatcherProvider.io) {
                getPostSubmitBottomSheetUseCase.setParams(requestParams)
                getPostSubmitBottomSheetUseCase.executeOnBackground().productrevGetPostSubmitBottomSheetResponse
            }
            postSubmitReviewResult.value = RequestState.Success(data, requestParams)
        } catch (t: Throwable) {
            postSubmitReviewResult.value = RequestState.Error(t, requestParams)
        }
    }

    private fun mapShouldLoadForm(
        reputationId: String,
        productId: String,
        reviewForm: ReviewFormRequestState
    ): Boolean {
        return reviewForm is RequestState.Idle && reputationId.isNotBlank() && productId.isNotBlank()
    }

    private fun mapShouldLoadIncentiveOvo(
        reputationId: String,
        productId: String,
        incentiveOvo: IncentiveOvoRequestState
    ): Boolean {
        return incentiveOvo is RequestState.Idle && reputationId.isNotBlank() && productId.isNotBlank()
    }

    private fun mapShouldLoadReviewTemplates(
        productId: String,
        reviewTemplates: ReviewTemplateRequestState
    ): Boolean {
        return reviewTemplates is RequestState.Idle && productId.isNotBlank()
    }

    private suspend fun hasPendingIncentive(): Boolean {
        return try {
            withContext(coroutineDispatcherProvider.io) {
                getProductIncentiveOvo.getIncentiveOvo(
                    productId = "",
                    reputationId = ""
                )?.productrevIncentiveOvo != null
            }
        } catch (_: Exception) {
            false
        }
    }

    private fun mergeImagePickerResultWithOriginalImages(
        imagePickerResult: MutableList<String>,
        imagesFedIntoPicker: MutableList<String>
    ): List<String> {
        return imagePickerResult.mapIndexed { index, result ->
            if (result.endsWith(ReviewConstants.TEMP_IMAGE_EXTENSION)) {
                imagesFedIntoPicker[index]
            } else {
                result
            }
        }
    }

    private fun appendSelectedTemplatesToReviewText(
        reviewTemplate: ReviewTemplateRequestSuccessState
    ) {
        reviewText.update { reviewText ->
            var source = reviewText.source
            val appendedTemplates = ArrayList(reviewText.appendedTemplates)
            val text = StringBuilder(reviewText.text).apply {
                reviewTemplate.result.forEach { template ->
                    if (template.selected && template !in appendedTemplates) {
                        source = CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEMPLATE
                        appendedTemplates.add(template)
                        append(" ${template.text}")
                    }
                }
            }.toString().trim()
            reviewText.copy(
                text = text,
                source = source,
                appendedTemplates = appendedTemplates
            )
        }
    }

    private suspend fun tryUploadMedia(data: Pair<Boolean, List<String>>) {
        val (shouldResetFailedUploadStatus, uris) = data
        cancelInvalidUploadJobs(uris)
        if (shouldResetFailedUploadStatus) {
            mediaUploadResults.value = mediaUploadResults.value.filterValues { uploadResult ->
                uploadResult is CreateReviewMediaUploadResult.Success
            }
            this@CreateReviewViewModel.shouldResetFailedUploadStatus.value = false
        } else {
            uris.forEach { uri ->
                val hasActiveUploadJob = mediaUploadJobs.value[uri]?.isActive == true
                val hasUploadResult = mediaUploadResults.value[uri] != null
                val needToStartNewJob = !hasActiveUploadJob && !hasUploadResult
                if (needToStartNewJob) {
                    updateMediaUploadJobs(uri, startNewUploadMediaJob(uri))
                    mediaUploadJobs.value.values.joinAll()
                }
            }
        }
    }

    private suspend fun trySubmitReview(
        requestParams: ProductrevSubmitReviewUseCase.SubmitReviewRequestParams?
    ) {
        try {
            requestParams?.let {
                submitReviewResult.value = RequestState.Requesting()
                submitReviewUseCase.setParams(it)
                val result = submitReviewUseCase.executeOnBackground()
                feedbackId.value = result.productrevSuccessIndicator?.feedbackID.orEmpty()
                submitReviewResult.value = RequestState.Success(result)
            }
        } catch (t: Throwable) {
            sendingReview.value = false
            submitReviewResult.value = RequestState.Error(t)
        }
    }

    fun submitReview() {
        sendingReview.value = true
    }

    // region MutableStateFlow updater
    fun removeMedia(media: CreateReviewMediaUiModel) {
        mediaUris.update { currentValue -> currentValue.toMutableList().apply { remove(media.uri) } }
    }

    fun setRating(newRating: Int) {
        rating.value = newRating
    }

    fun setProductId(newProductId: String) {
        productId.value = newProductId
    }

    fun setReputationId(newReputationId: String) {
        reputationId.value = newReputationId
    }

    fun setUtmSource(newUtmSource: String) {
        utmSource.value = newUtmSource
    }

    fun setReviewText(newReviewText: String, source: CreateReviewTextAreaTextUiModel.Source) {
        reviewText.update { it.copy(text = newReviewText, source = source) }
    }

    fun setAnonymous(newAnonymous: Boolean) {
        anonymous.value = newAnonymous
    }

    fun updateBadRatingSelectedStatus(id: String, selected: Boolean) {
        badRatingCategories.update { currentValue ->
            if (currentValue is BadRatingCategoriesRequestSuccessState) {
                RequestState.Success(
                    currentValue.result.map {
                        if (it.id == id) it.copy(selected = selected) else it
                    }
                )
            } else currentValue
        }
    }

    fun updateTextAreaHasFocus(hasFocus: Boolean) {
        textAreaHasFocus.value = hasFocus
    }

    fun selectTemplate(template: CreateReviewTemplate) {
        reviewTemplate.update { currentValue ->
            if (currentValue is ReviewTemplateRequestSuccessState) {
                val templates = currentValue.result.map {
                    if (it == template) it.copy(selected = true) else it
                }
                RequestState.Success(templates)
            } else currentValue
        }
    }

    fun updateMediaPicker(
        selectedImage: MutableList<String>,
        imagesFedIntoPicker: MutableList<String>
    ) {
        retryUploadMedia()
        mediaUris.value = mergeImagePickerResultWithOriginalImages(selectedImage, imagesFedIntoPicker)
    }

    fun setReviewTemplatesAnimating(animating: Boolean) {
        reviewTemplatesAnimating.value = animating
    }

    fun retryUploadMedia() {
        shouldResetFailedUploadStatus.value = true
    }

    fun showIncentiveBottomSheet() {
        shouldShowIncentiveBottomSheet.value = true
    }

    fun dismissIncentiveBottomSheet() {
        shouldShowIncentiveBottomSheet.value = false
    }

    fun showTextAreaBottomSheet() {
        reviewText.update { it.copy(source = CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA) }
        shouldShowTextAreaBottomSheet.value = true
    }

    fun dismissTextAreaBottomSheet() {
        shouldShowTextAreaBottomSheet.value = false
    }
    // endregion MutableStateFlow updater

    // region Flow value getter
    fun getProductId(): String {
        return productId.value
    }

    fun getOrderId(): String {
        return orderId.value
    }

    fun getReputationId(): String {
        return reputationId.value
    }

    fun getUtmSource(): String {
        return utmSource.value
    }

    fun hasIncentive(): Boolean {
        return hasIncentive.value
    }

    fun hasOngoingChallenge(): Boolean {
        return hasOngoingChallenge.value
    }

    fun isGoodRating(): Boolean {
        return isGoodRating.value
    }

    fun isReviewTextEmpty(): Boolean {
        return reviewText.value.text.isNotBlank()
    }

    fun isMediaEmpty(): Boolean {
        return mediaPickerUiState.value.let { mediaPickerUiState ->
            val isLoading = mediaPickerUiState is CreateReviewMediaPickerUiState.Loading
            val containMedia = if (mediaPickerUiState is CreateReviewMediaPickerUiState.Showing) {
                mediaPickerUiState.mediaItems.count { media ->
                    media is CreateReviewMediaUiModel.Image || media is CreateReviewMediaUiModel.Video
                }.isZero()
            } else false
            isLoading || !containMedia
        }
    }

    fun getUserId(): String {
        return userSessionInterface.userId
    }

    fun getSelectedImagesUrl(): ArrayList<String> {
        return ArrayList(mediaUris.value)
    }

    fun getRating(): Int {
        return rating.value
    }

    fun getReviewMessageLength(): Int {
        return reviewText.value.text.length
    }

    fun getNumberOfPictures(): Int {
        return mediaUris.value.size
    }

    fun isAnonymous(): Boolean {
        return anonymous.value
    }

    fun isTemplateAvailable(): Boolean {
        return reviewTemplate.value.let { reviewTemplate ->
            reviewTemplate is ReviewTemplateRequestSuccessState && reviewTemplate.result.isNotEmpty()
        }
    }

    fun getSelectedTemplateCount(): Int {
        return reviewTemplate.value.let { reviewTemplate ->
            if (reviewTemplate is ReviewTemplateRequestSuccessState){
                reviewTemplate.result.count { it.selected }.orZero()
            } else Int.ZERO
        }
    }

    fun isReviewComplete(): Boolean {
        return progressBarState.value.isComplete()
    }

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun getFeedbackId(): String {
        return feedbackId.value
    }
    // endregion Flow value getter

    // region save UI state handle
    @Suppress("UNCHECKED_CAST")
    private fun <T: Any> Bundle.getSavedState(key: String, defaultValue: T): T {
        return get(key) as? T ?: defaultValue
    }

    fun saveUiState(outState: Bundle) {
        with(outState) {
            putInt(SAVED_STATE_RATING, rating.value)
            putSerializable(SAVED_STATE_REVIEW_TEXT, reviewText.value)
            putBoolean(SAVED_STATE_ANONYMOUS, anonymous.value)
            putStringArrayList(SAVED_STATE_MEDIA_URIS, ArrayList(mediaUris.value))
            putSerializable(SAVED_STATE_MEDIA_UPLOAD_RESULTS, HashMap(mediaUploadResults.value))
            putBoolean(SAVED_STATE_EDIT_MODE, editMode.value)
            putString(SAVED_STATE_PRODUCT_ID, productId.value)
            putString(SAVED_STATE_SHOP_ID, shopId.value)
            putString(SAVED_STATE_ORDER_ID, orderId.value)
            putString(SAVED_STATE_FEEDBACK_ID, feedbackId.value)
            putString(SAVED_STATE_REPUTATION_ID, reputationId.value)
            putString(SAVED_STATE_UTM_SOURCE, utmSource.value)
            putBoolean(SAVED_STATE_SHOULD_SHOW_INCENTIVE_BOTTOM_SHEET, shouldShowIncentiveBottomSheet.value)
            putBoolean(SAVED_STATE_SHOULD_SHOW_TEXT_AREA_BOTTOM_SHEET, shouldShowTextAreaBottomSheet.value)
            putBoolean(SAVED_STATE_SENDING_REVIEW, sendingReview.value)
            putSerializable(SAVED_STATE_REVIEW_FORM, reviewForm.value)
            putSerializable(SAVED_STATE_INCENTIVE_OVO, incentiveOvo.value)
            putSerializable(SAVED_STATE_REVIEW_TEMPLATE, reviewTemplate.value)
            putSerializable(SAVED_STATE_BAD_RATING_CATEGORIES, badRatingCategories.value)
            putSerializable(SAVED_STATE_SUBMIT_REVIEW_RESULT, submitReviewResult.value)
            putSerializable(SAVED_STATE_POST_SUBMIT_REVIEW_RESULT, postSubmitReviewResult.value)
        }
    }

    fun restoreUiState(savedInstanceState: Bundle) {
        with(savedInstanceState) {
            reviewForm.value = getSavedState(SAVED_STATE_REVIEW_FORM, reviewForm.value)
            incentiveOvo.value = getSavedState(SAVED_STATE_INCENTIVE_OVO, incentiveOvo.value)
            reviewTemplate.value = getSavedState(SAVED_STATE_REVIEW_TEMPLATE, reviewTemplate.value)
            badRatingCategories.value = getSavedState(SAVED_STATE_BAD_RATING_CATEGORIES, badRatingCategories.value)
            submitReviewResult.value = getSavedState(SAVED_STATE_SUBMIT_REVIEW_RESULT, submitReviewResult.value)
            postSubmitReviewResult.value = getSavedState(SAVED_STATE_POST_SUBMIT_REVIEW_RESULT, postSubmitReviewResult.value)
            rating.value = getSavedState(SAVED_STATE_RATING, rating.value)
            anonymous.value = getSavedState(SAVED_STATE_ANONYMOUS, anonymous.value)
            mediaUris.value = getSavedState(SAVED_STATE_MEDIA_URIS, mediaUris.value)
            mediaUploadResults.value = getSavedState(SAVED_STATE_MEDIA_UPLOAD_RESULTS, mediaUploadResults.value)
            editMode.value = getSavedState(SAVED_STATE_EDIT_MODE, editMode.value)
            orderId.value = getSavedState(SAVED_STATE_ORDER_ID, orderId.value)
            feedbackId.value = getSavedState(SAVED_STATE_FEEDBACK_ID, feedbackId.value)
            utmSource.value = getSavedState(SAVED_STATE_UTM_SOURCE, utmSource.value)
            shouldShowIncentiveBottomSheet.value = getSavedState(SAVED_STATE_SHOULD_SHOW_INCENTIVE_BOTTOM_SHEET, shouldShowIncentiveBottomSheet.value)
            shouldShowTextAreaBottomSheet.value = getSavedState(SAVED_STATE_SHOULD_SHOW_TEXT_AREA_BOTTOM_SHEET, shouldShowTextAreaBottomSheet.value)
            sendingReview.value = getSavedState(SAVED_STATE_SENDING_REVIEW, sendingReview.value)
            reviewText.value = getSavedState(SAVED_STATE_REVIEW_TEXT, reviewText.value).copy(source = CreateReviewTextAreaTextUiModel.Source.SAVED_INSTANCE_STATE)
            shopId.value = getSavedState(SAVED_STATE_SHOP_ID, shopId.value)
            productId.value = getSavedState(SAVED_STATE_PRODUCT_ID, productId.value)
            reputationId.value = getSavedState(SAVED_STATE_REPUTATION_ID, reputationId.value)
        }
    }
    // endregion save UI state handle
}
