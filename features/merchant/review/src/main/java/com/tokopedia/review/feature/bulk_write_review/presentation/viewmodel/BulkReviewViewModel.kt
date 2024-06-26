package com.tokopedia.review.feature.bulk_write_review.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.bulk_write_review.di.qualifier.BulkReviewGson
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestParams
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestParam
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetBadRatingCategoryUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetFormUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewSubmitUseCase
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.analytic.BulkWriteReviewTracker
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewBadRatingCategoryMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewBadRatingCategoryUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewMediaPickerUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewMiniActionsUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewPageUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewProductInfoUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewRatingUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewStickyButtonMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewSubmissionParamsMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewTextAreaUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewVisitableMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemBadRatingCategoryUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemMediaUploadBatchNumberUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemMediaUploadJobsUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemMediaUploadResultsUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemMediaUrisUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemRatingUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemTestimonyUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.ReviewTestimonyUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryBottomSheetUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewCancelReviewSubmissionDialogUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewExpandedTextAreaBottomSheetUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewItemUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionsUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewPageUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewProductInfoUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRemoveReviewItemDialogUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewStickyButtonUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.util.ResourceProvider
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewMediaUploadResult
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewToasterUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.MediaUploadJobMap
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.MediaUploadResultMap
import com.tokopedia.review.feature.createreputation.util.CreateReviewMapper
import com.tokopedia.reviewcommon.extension.combine
import com.tokopedia.reviewcommon.extension.get
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.File
import javax.inject.Inject

class BulkReviewViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getFormUseCase: BulkReviewGetFormUseCase,
    private val getBadRatingCategoryUseCase: BulkReviewGetBadRatingCategoryUseCase,
    private val uploaderUseCase: UploaderUseCase,
    private val submitUseCase: BulkReviewSubmitUseCase,
    private val bulkReviewProductInfoUiStateMapper: BulkReviewProductInfoUiStateMapper,
    private val bulkReviewRatingUiStateMapper: BulkReviewRatingUiStateMapper,
    private val bulkReviewBadRatingCategoryUiStateMapper: BulkReviewBadRatingCategoryUiStateMapper,
    private val bulkReviewTextAreaUiStateMapper: BulkReviewTextAreaUiStateMapper,
    private val bulkReviewMediaPickerUiStateMapper: BulkReviewMediaPickerUiStateMapper,
    private val bulkReviewMiniActionsUiStateMapper: BulkReviewMiniActionsUiStateMapper,
    private val bulkReviewVisitableMapper: BulkReviewVisitableMapper,
    private val bulkReviewStickyButtonMapper: BulkReviewStickyButtonMapper,
    private val bulkReviewPageUiStateMapper: BulkReviewPageUiStateMapper,
    private val bulkReviewBadRatingCategoryMapper: BulkReviewBadRatingCategoryMapper,
    private val bulkReviewSubmissionParamsMapper: BulkReviewSubmissionParamsMapper,
    @BulkReviewGson private val gson: Gson,
    private val userSession: UserSessionInterface,
    private val bulkWriteReviewTracker: BulkWriteReviewTracker
) : ViewModel() {

    companion object {
        private const val STATE_FLOW_TIMEOUT_MILLIS = 5000L
        private const val DELAY_WAIT_FOR_UI_STATE_RESTORATION = 5000L
        private const val UPDATE_POEM_INTERVAL = 1000L
        private const val MIN_REVIEW_ITEM = 1
        private const val SAVE_STATE_KEY_GET_FORM_REQUEST_STATE = "getFormRequestState"
        private const val SAVE_STATE_KEY_GET_BAD_RATING_CATEGORY_REQUEST_STATE = "getBadRatingCategoryRequestState"
        private const val SAVE_STATE_KEY_SUBMIT_BULK_REVIEW_REQUEST_STATE = "submitBulkReviewRequestState"
        private const val SAVE_STATE_KEY_REMOVED_REVIEW_ITEMS_INBOX_ID = "removedReviewItemsInboxID"
        private const val SAVE_STATE_KEY_REVIEW_ITEMS_RATING = "reviewItemsRating"
        private const val SAVE_STATE_KEY_REVIEW_ITEMS_BAD_RATING_CATEGORY = "reviewItemsBadRatingCategory"
        private const val SAVE_STATE_KEY_REVIEW_ITEMS_TESTIMONY = "reviewItemsTestimony"
        private const val SAVE_STATE_KEY_REVIEW_ITEMS_MEDIA_URIS = "reviewItemsMediaUris"
        private const val SAVE_STATE_KEY_REVIEW_ITEMS_MEDIA_UPLOAD_RESULTS = "reviewItemsMediaUploadResults"
        private const val SAVE_STATE_KEY_REVIEW_ITEMS_MEDIA_UPLOAD_BATCH_NUMBER = "reviewItemsMediaUploadBatchNumber"
        private const val SAVE_STATE_KEY_ANONYMOUS = "anonymous"
        private const val SAVE_STATE_KEY_SHOULD_SUBMIT_REVIEW = "shouldSubmitReview"
        private const val SAVE_STATE_KEY_ACTIVE_MEDIA_PICKER_INBOX_ID = "activeMediaPickerInboxID"

        private const val TOASTER_ID_REMOVE_REVIEW = 0

        private const val REVIEW_ID_ALL = "-1"
    }

    private var defaultReviewItemRating = BulkReviewRatingUiStateMapper.DEFAULT_PRODUCT_RATING

    // region stateflow that need to be saved and restored
    private val getFormRequestState = MutableStateFlow<BulkReviewGetFormRequestState>(BulkReviewGetFormRequestState.Requesting())
    private val getBadRatingCategoryRequestState = MutableStateFlow<BulkReviewGetBadRatingCategoryRequestState>(BulkReviewGetBadRatingCategoryRequestState.Requesting())
    private val submitBulkReviewRequestState = MutableStateFlow<BulkReviewSubmitRequestState>(BulkReviewSubmitRequestState.Requesting())
    private val removedReviewItemsInboxID = MutableStateFlow(emptySet<String>())
    private val impressedReviewItemsInboxID = MutableStateFlow(emptySet<String>())
    private val reviewItemsRating = MutableStateFlow(emptyList<BulkReviewItemRatingUiModel>())
    private val reviewItemsBadRatingCategory = MutableStateFlow(emptyList<BulkReviewItemBadRatingCategoryUiModel>())
    private val reviewItemsTestimony = MutableStateFlow(emptyList<BulkReviewItemTestimonyUiModel>())
    private val reviewItemsMediaUris = MutableStateFlow(emptyList<BulkReviewItemMediaUrisUiModel>())
    private val reviewItemsMediaUploadResults = MutableStateFlow(emptyList<BulkReviewItemMediaUploadResultsUiModel>())
    private val reviewItemsMediaUploadBatchNumber = MutableStateFlow(emptyList<BulkReviewItemMediaUploadBatchNumberUiModel>())
    private val anonymous = MutableStateFlow(false)
    private val shouldSubmitReview = MutableStateFlow(false)
    private val shouldApplyBadRatingToAllReviewItems = MutableStateFlow(false)
    private var activeMediaPickerInboxID = ""

    // endregion stateflow that need to be saved and restored
    private val shouldCancelBulkReview = MutableStateFlow(false)
    private val reviewItemsMediaUploadJobs = MutableStateFlow(emptyList<BulkReviewItemMediaUploadJobsUiModel>())
    private val reviewItemImpressEventInboxID = MutableSharedFlow<String>(extraBufferCapacity = 50)
    private val bulkReviewToasterCtaKeyEvents = MutableSharedFlow<CreateReviewToasterUiModel<Any>>(extraBufferCapacity = 50)
    private val reviewItemsProductInfoUiState = getFormRequestState.mapLatest(
        bulkReviewProductInfoUiStateMapper::map
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsRatingUiState = combine(
        getFormRequestState,
        reviewItemsRating,
        ::mapReviewRatingUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsBadRatingCategoryUiState = combine(
        getFormRequestState,
        reviewItemsBadRatingCategory,
        bulkReviewBadRatingCategoryUiStateMapper::map
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsTextAreaUiState = combine(
        getFormRequestState,
        reviewItemsTestimony,
        reviewItemsBadRatingCategoryUiState,
        reviewItemsRatingUiState,
        bulkReviewTextAreaUiStateMapper::map
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsMediaPickerItems = combine(
        reviewItemsMediaUris,
        reviewItemsMediaUploadResults,
        reviewItemsMediaUploadJobs,
        reviewItemsMediaUploadBatchNumber,
        ::mapMediaItems
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val poemUpdateSignal = flow {
        while (true) {
            emit(Unit)
            delay(UPDATE_POEM_INTERVAL)
        }
    }
    private val reviewItemsMediaPickerPoem = combine(
        reviewItemsMediaPickerItems,
        poemUpdateSignal,
        ::mapPoem
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsMediaPickerUiState = combine(
        getFormRequestState,
        reviewItemsMediaPickerItems,
        reviewItemsMediaPickerPoem,
        reviewItemsMediaUploadBatchNumber,
        ::mapMediaPickerUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsMiniActionsUiState = combine(
        getFormRequestState,
        reviewItemsTextAreaUiState,
        reviewItemsMediaPickerUiState,
        bulkReviewMiniActionsUiStateMapper::map
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val bulkReviewVisitableList = combine(
        getFormRequestState,
        removedReviewItemsInboxID,
        impressedReviewItemsInboxID,
        reviewItemsProductInfoUiState,
        reviewItemsRatingUiState,
        reviewItemsBadRatingCategoryUiState,
        reviewItemsTextAreaUiState,
        reviewItemsMediaPickerUiState,
        reviewItemsMiniActionsUiState,
        ::mapBulkReviewVisitableList
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyList()
    )
    private val bulkReviewStickyButtonUiState = combine(
        shouldSubmitReview,
        submitBulkReviewRequestState,
        bulkReviewVisitableList,
        anonymous,
        bulkReviewStickyButtonMapper::map
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = BulkReviewStickyButtonUiState.Hidden
    )
    private val _removeReviewItemDialogUiState = MutableStateFlow<BulkReviewRemoveReviewItemDialogUiState>(BulkReviewRemoveReviewItemDialogUiState.Dismissed)
    val removeReviewItemDialogUiState: StateFlow<BulkReviewRemoveReviewItemDialogUiState>
        get() = _removeReviewItemDialogUiState
    private val _cancelReviewSubmissionDialogUiState = MutableStateFlow<BulkReviewCancelReviewSubmissionDialogUiState>(BulkReviewCancelReviewSubmissionDialogUiState.Dismissed)
    val cancelReviewSubmissionDialogUiState: StateFlow<BulkReviewCancelReviewSubmissionDialogUiState>
        get() = _cancelReviewSubmissionDialogUiState
    private val _badRatingCategoryBottomSheetUiState = MutableStateFlow<BulkReviewBadRatingCategoryBottomSheetUiState>(BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)
    val badRatingCategoryBottomSheetUiState: StateFlow<BulkReviewBadRatingCategoryBottomSheetUiState>
        get() = _badRatingCategoryBottomSheetUiState
    private val _expandedTextAreaBottomSheetUiState = MutableStateFlow<BulkReviewExpandedTextAreaBottomSheetUiState>(BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)
    val expandedTextAreaBottomSheetUiState: StateFlow<BulkReviewExpandedTextAreaBottomSheetUiState>
        get() = _expandedTextAreaBottomSheetUiState
    private val _bulkReviewPageToasterQueue = MutableSharedFlow<CreateReviewToasterUiModel<Any>>(extraBufferCapacity = 50)
    val bulkReviewPageToasterQueue: Flow<CreateReviewToasterUiModel<Any>>
        get() = _bulkReviewPageToasterQueue
    private val _expandedTextAreaToasterQueue = MutableSharedFlow<CreateReviewToasterUiModel<Any>>(extraBufferCapacity = 50)
    val expandedTextAreaToasterQueue: Flow<CreateReviewToasterUiModel<Any>>
        get() = _expandedTextAreaToasterQueue
    private val _badRatingCategoryBottomSheetToasterQueue = MutableSharedFlow<CreateReviewToasterUiModel<Any>>(extraBufferCapacity = 50)
    val badRatingCategoryBottomSheetToasterQueue: Flow<CreateReviewToasterUiModel<Any>>
        get() = _badRatingCategoryBottomSheetToasterQueue
    val bulkReviewPageUiState = combine(
        shouldCancelBulkReview,
        shouldSubmitReview,
        submitBulkReviewRequestState,
        bulkReviewVisitableList,
        bulkReviewStickyButtonUiState,
        getFormRequestState,
        getBadRatingCategoryRequestState,
        bulkReviewPageUiStateMapper::map
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = BulkReviewPageUiState.Loading
    )
    private val _reviewItemScrollRequest = MutableSharedFlow<BulkReviewItemUiModel>()
    val reviewItemScrollRequest: Flow<BulkReviewItemUiModel>
        get() = _reviewItemScrollRequest

    init {
        observeMediaUrisForUpload()
        observeSubmitReviewsResult()
        observeToasterCtaClickEvents()
        observeShouldApplyBadRatingToAllReviewItems()
        handleMediaPickerErrorToasterQueue()
        handleSubmitReviews()
        handleTrackers()
    }

    fun getData(invoice: String, utmSource: String) {
        getForms(invoice, utmSource)
        getBadRatingCategory()
    }

    fun onRemoveReviewItem(inboxID: String) {
        val reviewItem = getReviewItem(inboxID) ?: return
        sendReviewItemRemoveButtonClickEventTracker(reviewItem)
        if (haveMinimumReviewItem()) {
            if (reviewItem.hasDefaultState()) {
                removeReviewItem(inboxID, notifyUser = true)
            } else {
                showRemoveReviewItemDialog(inboxID)
            }
        } else {
            enqueueToasterErrorCannotRemoveAllReviewItems()
        }
    }

    fun onRatingChanged(inboxID: String, rating: Int) {
        val reviewItem = getReviewItem(inboxID) ?: return
        sendReviewItemRatingChangedEventTracker(reviewItem, rating)
        updateRating(inboxID, rating)
        shouldShowBadRatingCategoryBottomSheet(inboxID, rating)
    }

    fun onRatingSet(inboxID: String) {
        reviewItemsRating.update {
            it.toMutableList().apply {
                find { reviewItemRating ->
                    reviewItemRating.inboxID == inboxID
                }.let { previousUiModel ->
                    if (previousUiModel != null) {
                        remove(previousUiModel)
                        add(previousUiModel.copy(animate = false))
                    } else {
                        add(
                            BulkReviewItemRatingUiModel(
                                inboxID = inboxID,
                                rating = defaultReviewItemRating,
                                animate = false
                            )
                        )
                    }
                }
            }
        }
    }

    fun onDismissExpandedTextAreaBottomSheet(text: String) {
        applyExpandedTextAreaValue(text)
        dismissExpandedTextAreaBottomSheet(text)
    }

    fun onBadRatingCategorySelectionChanged(
        position: Int,
        badRatingCategoryID: String,
        reason: String,
        selected: Boolean
    ) {
        val uiState = badRatingCategoryBottomSheetUiState.value
        if (uiState is BulkReviewBadRatingCategoryBottomSheetUiState.Showing) {
            if (uiState.inboxID == REVIEW_ID_ALL) {
                onBadRatingCategorySelectionChangedForAllReviewItems(
                    position = position,
                    badRatingCategoryID = badRatingCategoryID,
                    reason = reason,
                    selected = selected
                )
            } else {
                onBadRatingCategorySelectionChangedForReviewItems(
                    position = position,
                    badRatingCategoryID = badRatingCategoryID,
                    reason = reason,
                    selected = selected
                )
            }
        }
    }

    private fun onBadRatingCategorySelectionChangedForReviewItems(
        position: Int,
        badRatingCategoryID: String,
        reason: String,
        selected: Boolean
    ) {
        val reviewItem = getBulkReviewBadRatingCategoryBottomSheetReferencedReviewItem() ?: return
        if (selected) sendReviewItemBadRatingCategorySelectedEventTracker(
            position = position,
            reason = reason,
            reviewItem = reviewItem
        )
        updateBadRatingCategorySelection(
            badRatingCategoryID = badRatingCategoryID,
            selected = selected
        )
        shouldShowExpandedTextAreaBottomSheetOnBadRatingChanged(
            badRatingCategoryID = badRatingCategoryID,
            selected = selected,
            inboxID = reviewItem.inboxID,
            testimony = reviewItem.getReviewItemTextAreaText(),
            rating = reviewItem.getReviewItemRating()
        )
    }

    private fun onBadRatingCategorySelectionChangedForAllReviewItems(
        position: Int,
        badRatingCategoryID: String,
        reason: String,
        selected: Boolean
    ) {
        if (selected) {
            bulkReviewVisitableList
                .value
                .filterIsInstance<BulkReviewItemUiModel>()
                .forEach { reviewItem ->
                    sendReviewItemBadRatingCategorySelectedEventTracker(position, reason, reviewItem)
                }
        }
        updateBadRatingCategorySelection(badRatingCategoryID, selected)
        shouldShowExpandedTextAreaBottomSheetOnBadRatingChanged(
            badRatingCategoryID = badRatingCategoryID,
            selected = selected,
            inboxID = REVIEW_ID_ALL,
            testimony = "",
            rating = defaultReviewItemRating
        )
    }

    fun onApplyBadRatingCategory() {
        applyBadRatingCategory()
        dismissBadRatingCategoryBottomSheet()
    }

    fun onChangeBadRatingCategory(inboxID: String) {
        showBadRatingCategoryBottomSheet(inboxID)
    }

    fun onReviewItemTextAreaGainFocus(inboxID: String) {
        reviewItemsTestimony.update {
            it.copyAndReplace(matcher = { reviewItemTestimony ->
                reviewItemTestimony.inboxID == inboxID
            }, copier = { reviewItemTestimony ->
                reviewItemTestimony.copy(
                    testimonyUiModel = reviewItemTestimony.testimonyUiModel.copy(
                        shouldShowTextArea = true,
                        focused = true
                    )
                )
            })
        }
    }

    fun onReviewItemTextAreaLostFocus(inboxID: String, text: String) {
        reviewItemsTestimony.update {
            it.copyAndReplace(
                matcher = { reviewItemTestimony ->
                    reviewItemTestimony.inboxID == inboxID
                },
                copier = { reviewItemTestimony ->
                    reviewItemTestimony.copy(
                        testimonyUiModel = reviewItemTestimony.testimonyUiModel.copy(
                            shouldShowTextArea = text.isNotBlank(),
                            focused = false
                        )
                    )
                }
            )
        }
    }

    fun onReviewItemTextAreaTextChanged(inboxID: String, text: String) {
        reviewItemsTestimony.update {
            it.copyAndReplace(
                matcher = { reviewItemTestimony ->
                    reviewItemTestimony.inboxID == inboxID
                },
                copier = { reviewItemTestimony ->
                    reviewItemTestimony.copy(
                        testimonyUiModel = reviewItemTestimony.testimonyUiModel.copy(
                            text = text,
                            shouldApplyText = false
                        )
                    )
                }
            )
        }
    }

    fun onConfirmRemoveReviewItem(inboxID: String, title: String, subtitle: String) {
        sendRemoveReviewItemDialogRemoveButtonClickEventTracker(title, subtitle)
        removeReviewItem(inboxID, notifyUser = true)
        dismissRemoveReviewItemDialog()
    }

    fun onCancelRemoveReviewItem(title: String, subtitle: String) {
        sendRemoveReviewItemDialogCancelButtonClickEventTracker(title, subtitle)
        dismissRemoveReviewItemDialog()
    }

    fun onClickTestimonyMiniAction(inboxID: String) {
        sendReviewItemAddTestimonyMiniActionClickEventTracker(inboxID)
        showReviewItemTextArea(inboxID)
    }

    fun onExpandTextArea(inboxID: String, text: String) {
        val reviewItem = getReviewItem(inboxID) ?: return
        updateReviewItemTestimony(inboxID, text)
        showExpandedTextAreaBottomSheet(
            inboxID = inboxID,
            title = ResourceProvider.getExpandedTextAreaTitle(reviewItem.getReviewItemRating()),
            hint = reviewItem.getReviewItemTextAreaHint(),
            text = text,
            allowEmpty = true
        )
    }

    fun onAnonymousCheckChanged(checked: Boolean) {
        anonymous.value = checked
    }

    fun onReceiveMediaPickerResult(originalPaths: List<String>) {
        val inboxID = getAndUpdateActiveMediaPickerInboxID(String.EMPTY)
        incrementReviewItemMediaUploadBatchNumber(inboxID)
        updateMediaUris(inboxID, originalPaths)
    }

    fun onRetryUploadClicked(inboxID: String) {
        incrementReviewItemMediaUploadBatchNumber(inboxID)
        updateReviewItemsMediaUrisForRetryUpload(inboxID)
    }

    fun onRemoveMedia(inboxID: String, media: CreateReviewMediaUiModel) {
        reviewItemsMediaUris.update {
            it.copyAndReplace(matcher = { reviewItemMediaUris ->
                reviewItemMediaUris.inboxID == inboxID
            }, copier = { reviewItemMediaUris ->
                    reviewItemMediaUris.copy(
                        uris = reviewItemMediaUris.uris.filter { uri -> uri != media.uri }
                    )
                })
        }
    }

    fun onSubmitReviews() {
        val itemWithFailMediaUpload = getFirstItemWithFailMediaUpload()
        val itemWithUploadingMedia = getFirstItemWithUploadingMedia()
        val itemWithEmptyOtherReasonTestimony = getFirstItemWithEmptyOtherReasonTestimony()
        if (itemWithFailMediaUpload != null) {
            enqueueToasterErrorUploadMedia(itemWithFailMediaUpload.getMediaUploadErrorCode())
        } else if (itemWithUploadingMedia != null) {
            enqueueToasterWaitForUploadMedia()
        } else if (itemWithEmptyOtherReasonTestimony != null) {
            scrollToReviewItemVisitable(itemWithEmptyOtherReasonTestimony)
        } else {
            shouldSubmitReview.value = true
        }
    }

    fun onConfirmCancelReviewSubmission() {
        shouldSubmitReview.value = false
        shouldCancelBulkReview.value = true
        dismissCancelReviewSubmissionDialog()
    }

    fun onCancelReviewSubmissionCancellation() {
        dismissCancelReviewSubmissionDialog()
    }

    fun onBackPressed(): Boolean {
        return if (shouldSubmitReview.value && submitBulkReviewRequestState.value is BulkReviewSubmitRequestState.Requesting) {
            showCancelReviewSubmissionDialog()
            true
        } else {
            sendPageDismissalEventTracker()
            false
        }
    }

    fun onSaveInstanceState(saveInstanceCacheManager: CacheManager) {
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_GET_FORM_REQUEST_STATE,
            gson.toJson(getFormRequestState.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_GET_BAD_RATING_CATEGORY_REQUEST_STATE,
            gson.toJson(getBadRatingCategoryRequestState.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_SUBMIT_BULK_REVIEW_REQUEST_STATE,
            gson.toJson(submitBulkReviewRequestState.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_REMOVED_REVIEW_ITEMS_INBOX_ID,
            gson.toJson(removedReviewItemsInboxID.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_REVIEW_ITEMS_RATING,
            gson.toJson(reviewItemsRating.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_REVIEW_ITEMS_BAD_RATING_CATEGORY,
            gson.toJson(reviewItemsBadRatingCategory.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_REVIEW_ITEMS_TESTIMONY,
            gson.toJson(reviewItemsTestimony.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_REVIEW_ITEMS_MEDIA_URIS,
            gson.toJson(reviewItemsMediaUris.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_REVIEW_ITEMS_MEDIA_UPLOAD_RESULTS,
            gson.toJson(reviewItemsMediaUploadResults.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_REVIEW_ITEMS_MEDIA_UPLOAD_BATCH_NUMBER,
            gson.toJson(reviewItemsMediaUploadBatchNumber.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_ANONYMOUS,
            gson.toJson(anonymous.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_SHOULD_SUBMIT_REVIEW,
            gson.toJson(shouldSubmitReview.value)
        )
        saveInstanceCacheManager.put(
            SAVE_STATE_KEY_ACTIVE_MEDIA_PICKER_INBOX_ID,
            gson.toJson(activeMediaPickerInboxID)
        )
    }

    fun onRestoreInstanceState(
        saveInstanceCacheManager: CacheManager,
        onFailedRestoreState: () -> Unit
    ) {
        viewModelScope.launch(coroutineDispatchers.io) {
            val savedGetFormRequestState = saveInstanceCacheManager.get<BulkReviewGetFormRequestState>(
                customId = SAVE_STATE_KEY_GET_FORM_REQUEST_STATE,
                type = BulkReviewGetFormRequestState::class.java,
                defaultValue = null,
                gson = gson
            )
            val savedGetBadRatingCategoryRequestState = saveInstanceCacheManager.get<BulkReviewGetBadRatingCategoryRequestState>(
                customId = SAVE_STATE_KEY_GET_BAD_RATING_CATEGORY_REQUEST_STATE,
                type = BulkReviewGetBadRatingCategoryRequestState::class.java,
                defaultValue = null,
                gson = gson
            )
            val savedSubmitBulkReviewRequestState = saveInstanceCacheManager.get<BulkReviewSubmitRequestState>(
                customId = SAVE_STATE_KEY_SUBMIT_BULK_REVIEW_REQUEST_STATE,
                type = BulkReviewSubmitRequestState::class.java,
                defaultValue = null,
                gson = gson
            )
            val savedRemovedReviewItemsInboxID = saveInstanceCacheManager.get<Set<String>>(
                customId = SAVE_STATE_KEY_REMOVED_REVIEW_ITEMS_INBOX_ID,
                type = object : TypeToken<HashSet<String>>() {}.type,
                defaultValue = null,
                gson = gson
            )
            val savedReviewItemsRating = saveInstanceCacheManager.get<List<BulkReviewItemRatingUiModel>>(
                customId = SAVE_STATE_KEY_REVIEW_ITEMS_RATING,
                type = object : TypeToken<ArrayList<BulkReviewItemRatingUiModel>>() {}.type,
                defaultValue = null,
                gson = gson
            )
            val savedReviewItemsBadRatingCategory = saveInstanceCacheManager.get<List<BulkReviewItemBadRatingCategoryUiModel>>(
                customId = SAVE_STATE_KEY_REVIEW_ITEMS_BAD_RATING_CATEGORY,
                type = object : TypeToken<ArrayList<BulkReviewItemBadRatingCategoryUiModel>>() {}.type,
                defaultValue = null,
                gson = gson
            )
            val savedReviewItemsTestimony = saveInstanceCacheManager.get<List<BulkReviewItemTestimonyUiModel>>(
                customId = SAVE_STATE_KEY_REVIEW_ITEMS_TESTIMONY,
                type = object : TypeToken<ArrayList<BulkReviewItemTestimonyUiModel>>() {}.type,
                defaultValue = null,
                gson = gson
            )
            val savedReviewItemsMediaUris = saveInstanceCacheManager.get<List<BulkReviewItemMediaUrisUiModel>>(
                customId = SAVE_STATE_KEY_REVIEW_ITEMS_MEDIA_URIS,
                type = object : TypeToken<ArrayList<BulkReviewItemMediaUrisUiModel>>() {}.type,
                defaultValue = null,
                gson = gson
            )
            val savedReviewItemsMediaUploadResults = saveInstanceCacheManager.get<List<BulkReviewItemMediaUploadResultsUiModel>>(
                customId = SAVE_STATE_KEY_REVIEW_ITEMS_MEDIA_UPLOAD_RESULTS,
                type = object : TypeToken<ArrayList<BulkReviewItemMediaUploadResultsUiModel>>() {}.type,
                defaultValue = null,
                gson = gson
            )
            val savedReviewItemsMediaUploadBatchNumber = saveInstanceCacheManager.get<List<BulkReviewItemMediaUploadBatchNumberUiModel>>(
                customId = SAVE_STATE_KEY_REVIEW_ITEMS_MEDIA_UPLOAD_BATCH_NUMBER,
                type = object : TypeToken<ArrayList<BulkReviewItemMediaUploadBatchNumberUiModel>>() {}.type,
                defaultValue = null,
                gson = gson
            )
            val savedAnonymous = saveInstanceCacheManager.get<Boolean>(
                customId = SAVE_STATE_KEY_ANONYMOUS,
                type = Boolean::class.java,
                defaultValue = null,
                gson = gson
            )
            val savedShouldSubmitReview = saveInstanceCacheManager.get<Boolean>(
                customId = SAVE_STATE_KEY_SHOULD_SUBMIT_REVIEW,
                type = Boolean::class.java,
                defaultValue = null,
                gson = gson
            )
            val savedActiveMediaPickerInboxID = saveInstanceCacheManager.get<String>(
                customId = SAVE_STATE_KEY_ACTIVE_MEDIA_PICKER_INBOX_ID,
                type = String::class.java,
                defaultValue = null,
                gson = gson
            )
            if (
                savedGetFormRequestState is BulkReviewGetFormRequestState &&
                savedGetBadRatingCategoryRequestState is BulkReviewGetBadRatingCategoryRequestState &&
                savedSubmitBulkReviewRequestState is BulkReviewSubmitRequestState &&
                savedRemovedReviewItemsInboxID != null &&
                savedReviewItemsRating != null &&
                savedReviewItemsBadRatingCategory != null &&
                savedReviewItemsTestimony != null &&
                savedReviewItemsMediaUris != null &&
                savedReviewItemsMediaUploadResults != null &&
                savedReviewItemsMediaUploadBatchNumber != null &&
                savedAnonymous != null &&
                savedShouldSubmitReview != null &&
                savedActiveMediaPickerInboxID != null
            ) {
                getFormRequestState.value = savedGetFormRequestState
                getBadRatingCategoryRequestState.value = savedGetBadRatingCategoryRequestState
                submitBulkReviewRequestState.value = savedSubmitBulkReviewRequestState
                removedReviewItemsInboxID.value = savedRemovedReviewItemsInboxID
                reviewItemsRating.value = savedReviewItemsRating
                reviewItemsBadRatingCategory.value = savedReviewItemsBadRatingCategory
                reviewItemsTestimony.value = savedReviewItemsTestimony
                reviewItemsMediaUris.value = savedReviewItemsMediaUris
                reviewItemsMediaUploadResults.value = savedReviewItemsMediaUploadResults
                reviewItemsMediaUploadBatchNumber.value = savedReviewItemsMediaUploadBatchNumber
                anonymous.value = savedAnonymous
                activeMediaPickerInboxID = savedActiveMediaPickerInboxID
                if (savedShouldSubmitReview) {
                    // wait 5 seconds or until restored raw data mapped into bulkReviewVisitableList
                    // before restoring shouldSubmitReview value, because we need bulkReviewVisitableList
                    // to compose the submission params
                    runCatching {
                        withTimeout(DELAY_WAIT_FOR_UI_STATE_RESTORATION) {
                            bulkReviewVisitableList.filter {
                                it.filterIsInstance<BulkReviewItemUiModel>().isNotEmpty()
                            }.first()
                            shouldSubmitReview.value = savedShouldSubmitReview
                        }
                    }
                } else {
                    // noop
                }
            } else {
                onFailedRestoreState()
            }
        }
    }

    fun scrollToFocusedReviewItemVisitable() {
        bulkReviewVisitableList
            .value
            .filterIsInstance<BulkReviewItemUiModel>()
            .find { it.uiState is BulkReviewItemUiState.Focused }
            ?.let(::scrollToReviewItemVisitable)
    }

    fun getReviewItemVisitablePosition(inboxID: String): Int {
        val pageUiState = bulkReviewPageUiState.value as? BulkReviewPageUiState.Showing ?: return -1
        return pageUiState.items.indexOfFirst { it is BulkReviewItemUiModel && it.inboxID == inboxID }
    }

    fun getAndUpdateActiveMediaPickerInboxID(inboxID: String): String {
        val previousValue = activeMediaPickerInboxID
        activeMediaPickerInboxID = inboxID
        return previousValue
    }

    fun getReviewItemMedia(inboxID: String): List<String> {
        return getReviewItem(inboxID)?.getReviewItemAttachmentPaths().orEmpty()
    }

    fun enqueueToasterDisabledAddMoreMedia() {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = ResourceProvider.getErrorMessageCannotAddMediaWhileUploading(),
                actionText = StringRes(Int.ZERO),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL,
                payload = Unit
            )
        )
    }

    fun isAnonymous(): Boolean {
        return anonymous.value
    }

    fun getUserId(): String {
        return userSession.userId
    }

    fun onReviewItemImpressed(inboxID: String) {
        impressedReviewItemsInboxID.update { it.toMutableSet().apply { add(inboxID) } }
        reviewItemImpressEventInboxID.tryEmit(inboxID)
    }

    fun onBadRatingCategoryImpressed(position: Int, reason: String) {
        sendReviewItemBadRatingCategoryImpressionEventTracker(position, reason)
    }

    fun onRemoveReviewItemDialogImpressed(inboxID: String, title: String, subtitle: String) {
        sendRemoveReviewItemDialogImpressionEventTracker(inboxID, title, subtitle)
    }

    fun onClickAddAttachmentMiniAction(inboxID: String) {
        sendReviewItemAddAttachmentMiniActionClickEventTracker(inboxID)
    }

    fun sendAllTrackers() {
        bulkWriteReviewTracker.sendAllTrackers()
    }

    fun onToasterCtaClicked(data: CreateReviewToasterUiModel<Any>) {
        bulkReviewToasterCtaKeyEvents.tryEmit(data)
    }

    fun setDefaultReviewItemRating(rating: Int) {
        defaultReviewItemRating = rating
        shouldApplyBadRatingToAllReviewItems.value = rating <= ReviewConstants.BAD_RATING_CATEGORY_THRESHOLD
    }

    fun onCancelBadRatingCategoryBottomSheet() {
        val uiState = _badRatingCategoryBottomSheetUiState.value
        if (uiState is BulkReviewBadRatingCategoryBottomSheetUiState.Showing) {
            if (uiState.badRatingCategories.none { it.selected }) {
                enqueueToasterErrorNoBadRatingCategoryReasonSelected()
            }
        }
    }

    private fun mapReviewRatingUiState(
        getFormRequestState: BulkReviewGetFormRequestState,
        reviewItemsRating: List<BulkReviewItemRatingUiModel>
    ): Map<String, BulkReviewRatingUiState> {
        return bulkReviewRatingUiStateMapper.map(
            getFormRequestState = getFormRequestState,
            reviewItemsRating = reviewItemsRating,
            defaultReviewItemRating = defaultReviewItemRating
        )
    }

    private fun mapMediaItems(
        reviewItemsMediaUris: List<BulkReviewItemMediaUrisUiModel>,
        reviewItemsMediaUploadResults: List<BulkReviewItemMediaUploadResultsUiModel>,
        reviewItemsMediaUploadJobs: List<BulkReviewItemMediaUploadJobsUiModel>,
        reviewItemsUploadBatchNumber: List<BulkReviewItemMediaUploadBatchNumberUiModel>
    ): Map<String, List<CreateReviewMediaUiModel>> {
        return reviewItemsMediaUris.associate { reviewItemMediaUris ->
            reviewItemMediaUris.inboxID to CreateReviewMapper.mapMediaItems(
                reviewItemMediaUris = reviewItemMediaUris.uris,
                reviewItemsMediaUploadResults = reviewItemsMediaUploadResults.find {
                    it.inboxID == reviewItemMediaUris.inboxID
                }?.mediaUploadResults.orEmpty(),
                reviewItemMediaUploadJobs = reviewItemsMediaUploadJobs.find {
                    it.inboxID == reviewItemMediaUris.inboxID
                }?.jobs.orEmpty(),
                existingMediaItems = reviewItemsMediaPickerItems.value[reviewItemMediaUris.inboxID].orEmpty(),
                uploadBatchNumber = reviewItemsUploadBatchNumber.find {
                    it.inboxID == reviewItemMediaUris.inboxID
                }?.batchNumber.orZero(),
                showLargeAddMediaItem = false
            )
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mapPoem(
        mediaItems: Map<String, List<CreateReviewMediaUiModel>>,
        updateSignal: Unit
    ): Map<String, Pair<String, StringRes>> {
        return mediaItems.keys.associateWith { inboxID ->
            CreateReviewMapper.mapPoem(
                currentValue = reviewItemsMediaPickerPoem.value[inboxID] ?: Pair("", StringRes(Int.ZERO)),
                mediaItems = mediaItems[inboxID].orEmpty(),
                uploadBatchNumber = reviewItemsMediaUploadBatchNumber.value.find {
                    it.inboxID == inboxID
                }?.batchNumber.orZero()
            )
        }
    }

    private fun mapMediaPickerUiState(
        getFormRequestState: BulkReviewGetFormRequestState,
        mediaItems: Map<String, List<CreateReviewMediaUiModel>>,
        poem: Map<String, Pair<String, StringRes>>,
        reviewItemsUploadBatchNumber: List<BulkReviewItemMediaUploadBatchNumberUiModel>
    ): Map<String, CreateReviewMediaPickerUiState> {
        return bulkReviewMediaPickerUiStateMapper.map(
            getFormRequestState = getFormRequestState,
            mediaItems = mediaItems,
            poem = poem,
            currentMediaPickerUiStates = reviewItemsMediaPickerUiState.value,
            reviewItemsUploadBatchNumber = reviewItemsUploadBatchNumber
        )
    }

    private fun mapBulkReviewVisitableList(
        getFormRequestState: BulkReviewGetFormRequestState,
        removedReviewItem: Set<String>,
        impressedReviewItems: Set<String>,
        bulkReviewProductInfoUiState: Map<String, BulkReviewProductInfoUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>,
        bulkReviewMiniActionsUiState: Map<String, BulkReviewMiniActionsUiState>
    ): List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>> {
        return when (getFormRequestState) {
            is BulkReviewGetFormRequestState.Complete.Success -> bulkReviewVisitableMapper.map(
                productRevGetBulkForm = getFormRequestState.result,
                removedReviewItem = removedReviewItem,
                impressedReviewItems = impressedReviewItems,
                bulkReviewProductInfoUiState = bulkReviewProductInfoUiState,
                bulkReviewRatingUiState = bulkReviewRatingUiState,
                bulkReviewBadRatingCategoryUiState = bulkReviewBadRatingCategoryUiState,
                bulkReviewTextAreaUiState = bulkReviewTextAreaUiState,
                bulkReviewMediaPickerUiState = bulkReviewMediaPickerUiState,
                bulkReviewMiniActionsUiState = bulkReviewMiniActionsUiState
            )
            else -> bulkReviewVisitableList.value
        }
    }

    private fun observeMediaUrisForUpload() {
        viewModelScope.launch(coroutineDispatchers.io) {
            reviewItemsMediaUris.collectLatest { reviewItemsMediaUris ->
                reviewItemsMediaUris.forEach { reviewItemMediaUris ->
                    tryUploadMedia(reviewItemMediaUris)
                }
            }
        }
    }

    private fun observeSubmitReviewsResult() {
        viewModelScope.launch(coroutineDispatchers.io) {
            submitBulkReviewRequestState.collectLatest { submitBulkReviewRequestState ->
                if (submitBulkReviewRequestState is BulkReviewSubmitRequestState.Complete.Success) {
                    if (!submitBulkReviewRequestState.result.success) {
                        val bulkReviewItems = bulkReviewVisitableList.value
                            .filterIsInstance<BulkReviewItemUiModel>()
                        val reviewItemsInboxID = bulkReviewItems.map { it.inboxID }
                        val failedReviewItemsInboxID = submitBulkReviewRequestState
                            .result
                            .failedInboxIDs
                        val successReviewItemsInboxID = reviewItemsInboxID
                            .filter { it !in failedReviewItemsInboxID }
                        if (successReviewItemsInboxID.isNotEmpty()) {
                            removeReviewItem(
                                inboxID = successReviewItemsInboxID.toTypedArray(),
                                notifyUser = false
                            )
                            enqueueToasterSuccessSubmitReviewsPartially()
                        } else {
                            enqueueToasterFailedSubmitReviews()
                        }
                    }
                    dismissCancelReviewSubmissionDialog()
                } else if (submitBulkReviewRequestState is BulkReviewSubmitRequestState.Complete.Error) {
                    enqueueToasterFailedSubmitReviews()
                    dismissCancelReviewSubmissionDialog()
                }
            }
        }
    }

    private fun observeToasterCtaClickEvents() {
        viewModelScope.launch {
            bulkReviewToasterCtaKeyEvents.collect {
                when (it.id) {
                    TOASTER_ID_REMOVE_REVIEW -> onUndoRemoveReviewItem(it.payload as? String)
                }
            }
        }
    }

    private fun observeShouldApplyBadRatingToAllReviewItems() {
        viewModelScope.launch {
            combine(
                shouldApplyBadRatingToAllReviewItems,
                bulkReviewPageUiState
            ) { shouldApplyBadRatingToAllReviewItems, bulkReviewPageUiState ->
                shouldApplyBadRatingToAllReviewItems && bulkReviewPageUiState is BulkReviewPageUiState.Showing
            }.distinctUntilChanged().collectLatest { applyBadRatingToAllReviewItems ->
                if (applyBadRatingToAllReviewItems) {
                    shouldApplyBadRatingToAllReviewItems.value = false
                    showBadRatingCategoryBottomSheet(REVIEW_ID_ALL)
                }
            }
        }
    }

    private fun handleMediaPickerErrorToasterQueue() {
        viewModelScope.launch(coroutineDispatchers.io) {
            reviewItemsMediaPickerUiState.collectLatest {
                it.values.forEach { uiState ->
                    if (uiState is CreateReviewMediaPickerUiState.FailedUpload && uiState.shouldQueueToaster) {
                        enqueueToasterErrorUploadMedia(uiState.errorCode)
                    }
                }
            }
        }
    }

    private fun handleSubmitReviews() {
        viewModelScope.launch(coroutineDispatchers.io) {
            shouldSubmitReview.collectLatest { shouldSubmitReview ->
                if (shouldSubmitReview) {
                    mapSubmitReviewParams(
                        bulkReviewVisitableList = bulkReviewVisitableList.value,
                        anonymous = anonymous.value
                    ).let { params ->
                        if (params.isNotEmpty()) {
                            submitUseCase(params).collectLatest { requestState ->
                                submitBulkReviewRequestState.value = requestState
                            }
                        } else {
                            enqueueToasterFailedSubmitReviews()
                        }
                    }
                    this@BulkReviewViewModel.shouldSubmitReview.value = false
                }
            }
        }
    }

    private fun handleTrackers() {
        handleReviewItemImpressionEventTracker()
        handleReviewItemSubmissionEventTracker()
        handleReviewItemPartialErrorSubmissionEventTracker()
        handleReviewItemFullyErrorSubmissionEventTracker()
    }

    private fun handleReviewItemImpressionEventTracker() {
        viewModelScope.launch(coroutineDispatchers.io) {
            reviewItemImpressEventInboxID
                .distinctUntilChanged()
                .mapNotNull(::getReviewItem)
                .collect(::sendReviewItemImpressionEventTracker)
        }
    }

    private fun handleReviewItemSubmissionEventTracker() {
        viewModelScope.launch(coroutineDispatchers.io) {
            submitBulkReviewRequestState
                .filterIsInstance<BulkReviewSubmitRequestState.Complete.Success>()
                .flatMapConcat(::getSuccessInboxIDs)
                .mapNotNull(::getReviewItem)
                .collect(::sendReviewItemSubmissionEventTracker)
        }
    }

    private fun getSuccessInboxIDs(
        submitBulkReviewRequestState: BulkReviewSubmitRequestState.Complete.Success
    ) = flow {
        submitBulkReviewRequestState
            .params
            .map { it.inboxID }
            .filter { it !in submitBulkReviewRequestState.result.failedInboxIDs }
            .forEach { emit(it) }
    }

    private fun handleReviewItemPartialErrorSubmissionEventTracker() {
        viewModelScope.launch(coroutineDispatchers.io) {
            submitBulkReviewRequestState
                .filterIsInstance<BulkReviewSubmitRequestState.Complete.Success>()
                .flatMapConcat(::getFailedInboxIDs)
                .mapNotNull(::getReviewItem)
                .collect(::sendReviewItemPartialErrorSubmissionEventTracker)
        }
    }

    private fun getFailedInboxIDs(
        submitBulkReviewRequestState: BulkReviewSubmitRequestState.Complete.Success
    ) = flow {
        submitBulkReviewRequestState.result.failedInboxIDs.forEach { emit(it) }
    }

    private fun handleReviewItemFullyErrorSubmissionEventTracker() {
        viewModelScope.launch(coroutineDispatchers.io) {
            submitBulkReviewRequestState
                .filterIsInstance<BulkReviewSubmitRequestState.Complete.Error>()
                .flatMapConcat { flow { getAllReviewItem().forEach { emit(it) } } }
                .collect(::sendReviewItemFullyErrorSubmissionEventTracker)
        }
    }

    private fun mapSubmitReviewParams(
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        anonymous: Boolean
    ): List<BulkReviewSubmitRequestParam> {
        return bulkReviewVisitableList
            .filterIsInstance<BulkReviewItemUiModel>()
            .mapNotNull { bulkReviewItemUiModel ->
                bulkReviewSubmissionParamsMapper.map(bulkReviewItemUiModel, anonymous)
            }
    }

    private suspend fun tryUploadMedia(reviewItemMediaUris: BulkReviewItemMediaUrisUiModel) {
        val inboxID = reviewItemMediaUris.inboxID
        cancelReviewItemInvalidUploadJobs(reviewItemMediaUris)
        if (reviewItemMediaUris.shouldResetFailedUploadStatus) {
            resetReviewItemFailedMediaUploadResults(inboxID)
        } else {
            uploadMedia(inboxID, reviewItemMediaUris)
        }
    }

    private suspend fun cancelReviewItemInvalidUploadJobs(
        reviewItemMediaUris: BulkReviewItemMediaUrisUiModel
    ) {
        findReviewItemMediaUploadJobs(reviewItemMediaUris.inboxID).forEach { (uri, job) ->
            if (uri !in reviewItemMediaUris.uris && job.isActive) job.cancelAndJoin()
        }
    }

    private fun resetReviewItemFailedMediaUploadResults(inboxID: String) {
        reviewItemsMediaUploadResults.update {
            it.copyAndReplace(matcher = { reviewItemMediaUploadResults ->
                reviewItemMediaUploadResults.inboxID == inboxID
            }, copier = { reviewItemMediaUploadResults ->
                    reviewItemMediaUploadResults.copy(
                        mediaUploadResults = reviewItemMediaUploadResults
                            .mediaUploadResults
                            .filterValues { uploadResult ->
                                uploadResult is CreateReviewMediaUploadResult.Success
                            }
                    )
                })
        }
        // update shouldResetFailedUploadStatus value to false
        reviewItemsMediaUris.update {
            it.copyAndReplace(matcher = { reviewItemMediaUris ->
                reviewItemMediaUris.inboxID == inboxID
            }, copier = { reviewItemMediaUris ->
                    reviewItemMediaUris.copy(shouldResetFailedUploadStatus = false)
                })
        }
    }

    private suspend fun uploadMedia(
        inboxID: String,
        reviewItemMediaUris: BulkReviewItemMediaUrisUiModel
    ) {
        reviewItemMediaUris.uris.forEach { uri ->
            val hasActiveUploadJob = findReviewItemMediaUploadJob(inboxID, uri)?.isActive == true
            val hasUploadResult = findReviewItemMediaUploadResult(inboxID, uri) != null
            val needToStartNewJob = !hasActiveUploadJob && !hasUploadResult
            if (needToStartNewJob) {
                findReviewItemMediaUploadJobs(inboxID).values.joinAll()
                updateMediaUploadJobs(inboxID, uri, startNewUploadMediaJob(inboxID, uri))
                findReviewItemMediaUploadJobs(inboxID).values.joinAll()
            }
        }
    }

    private fun findReviewItemMediaUploadJob(inboxID: String, uri: String): Job? {
        return findReviewItemMediaUploadJobs(inboxID)[uri]
    }

    private fun findReviewItemMediaUploadJobs(inboxID: String): MediaUploadJobMap {
        return reviewItemsMediaUploadJobs.value.find { it.inboxID == inboxID }?.jobs.orEmpty()
    }

    private fun findReviewItemMediaUploadResult(inboxID: String, uri: String): CreateReviewMediaUploadResult? {
        return findReviewItemMediaUploadResults(inboxID)?.get(uri)
    }

    private fun findReviewItemMediaUploadResults(inboxID: String): MediaUploadResultMap? {
        return reviewItemsMediaUploadResults.value.find { it.inboxID == inboxID }?.mediaUploadResults
    }

    private fun startNewUploadMediaJob(inboxID: String, uri: String): Job {
        return viewModelScope.launchCatchError(context = coroutineDispatchers.io, block = {
            val filePath = File(uri)
            val params = uploaderUseCase.createParams(
                sourceId = getUploadSourceId(uri),
                filePath = filePath,
                withTranscode = false
            )
            val uploadMediaResult = uploaderUseCase(params).let {
                when (it) {
                    is UploadResult.Success -> CreateReviewMediaUploadResult.Success(it.uploadId, it.videoUrl)
                    is UploadResult.Error -> CreateReviewMediaUploadResult.Error(it.message)
                }
            }
            updateMediaUploadResults(inboxID, uri, uploadMediaResult)
        }, onError = {
                updateMediaUploadResults(inboxID, uri, CreateReviewMediaUploadResult.Error(it.message.orEmpty()))
            }).also { job -> job.invokeOnCompletion { updateMediaUploadJobs(inboxID, uri, null) } }
    }

    private fun updateMediaUploadJobs(inboxID: String, uri: String, newUploadJob: Job?) {
        reviewItemsMediaUploadJobs.update {
            it.toMutableList().apply {
                find { reviewItemMediaUploadJobs -> reviewItemMediaUploadJobs.inboxID == inboxID }.let { previousReviewItemMediaUploadJobs ->
                    if (previousReviewItemMediaUploadJobs == null && newUploadJob != null) {
                        add(
                            BulkReviewItemMediaUploadJobsUiModel(
                                inboxID = inboxID,
                                jobs = mapOf(uri to newUploadJob)
                            )
                        )
                    } else if (previousReviewItemMediaUploadJobs != null) {
                        remove(previousReviewItemMediaUploadJobs)
                        if (newUploadJob == null) {
                            add(
                                previousReviewItemMediaUploadJobs.copy(
                                    jobs = previousReviewItemMediaUploadJobs.jobs.filter { jobMap ->
                                        jobMap.key != uri
                                    }
                                )
                            )
                        } else {
                            add(
                                previousReviewItemMediaUploadJobs.copy(
                                    jobs = previousReviewItemMediaUploadJobs.jobs.toMutableMap().apply {
                                        put(uri, newUploadJob)
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getUploadSourceId(uri: String): String {
        return if (isVideoFormat(uri)) ReviewConstants.CREATE_REVIEW_VIDEO_SOURCE_ID else ReviewConstants.CREATE_REVIEW_IMAGE_SOURCE_ID
    }

    private fun updateMediaUploadResults(
        inboxID: String,
        uri: String,
        uploadMediaResult: CreateReviewMediaUploadResult
    ) {
        reviewItemsMediaUploadResults.update {
            it.toMutableList().apply {
                find { reviewItemMediaUploadResults ->
                    reviewItemMediaUploadResults.inboxID == inboxID
                }.let { previousReviewItemMediaUploadResults ->
                    if (previousReviewItemMediaUploadResults == null) {
                        add(
                            BulkReviewItemMediaUploadResultsUiModel(
                                inboxID = inboxID,
                                mediaUploadResults = mapOf(uri to uploadMediaResult)
                            )
                        )
                    } else {
                        remove(previousReviewItemMediaUploadResults)
                        add(
                            previousReviewItemMediaUploadResults.copy(
                                mediaUploadResults = previousReviewItemMediaUploadResults
                                    .mediaUploadResults
                                    .toMutableMap()
                                    .apply {
                                        put(uri, uploadMediaResult)
                                    }
                            )
                        )
                    }
                }
            }
        }
    }

    private fun enqueueToasterErrorUploadMedia(errorCode: String) {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = ResourceProvider.getMessageFailedUploadMedia(errorCode),
                actionText = StringRes(Int.ZERO),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR,
                payload = Unit
            )
        )
    }

    private fun enqueueToasterWaitForUploadMedia() {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = ResourceProvider.getMessageWaitForUploadMedia(),
                actionText = StringRes(Int.ZERO),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL,
                payload = Unit
            )
        )
    }

    private fun enqueueToasterErrorEmptyBadRatingCategoryOtherReason() {
        _expandedTextAreaToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = ResourceProvider.getMessageBadRatingReasonCannotEmpty(),
                actionText = ResourceProvider.getCtaOke(),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR,
                payload = Unit
            )
        )
    }

    private fun enqueueToasterErrorCannotRemoveAllReviewItems() {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = ResourceProvider.getMessageCannotRemoveMoreReviewItem(MIN_REVIEW_ITEM),
                actionText = ResourceProvider.getCtaOke(),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR,
                payload = Unit
            )
        )
    }

    private fun enqueueToasterSuccessRemoveReviewItem(inboxID: String) {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = ResourceProvider.getMessageReviewItemRemoved(),
                actionText = ResourceProvider.getCtaCancel(),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL,
                id = TOASTER_ID_REMOVE_REVIEW,
                payload = inboxID
            )
        )
    }

    private fun enqueueToasterFailedSubmitReviews() {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = ResourceProvider.getMessageReviewItemSubmissionFullyError(),
                actionText = ResourceProvider.getCtaOke(),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR,
                payload = Unit
            )
        )
    }

    private fun enqueueToasterSuccessSubmitReviewsPartially() {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = ResourceProvider.getMessageReviewItemPartiallySubmitted(),
                actionText = ResourceProvider.getCtaOke(),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR,
                payload = Unit
            )
        )
    }

    private fun enqueueToasterErrorNoBadRatingCategoryReasonSelected() {
        _badRatingCategoryBottomSheetToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = ResourceProvider.getMessageBadRatingReasonReasonMustBeSelected(),
                actionText = ResourceProvider.getCtaOke(),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR,
                payload = Unit
            )
        )
    }

    private fun showBadRatingCategoryBottomSheet(inboxID: String) {
        _badRatingCategoryBottomSheetUiState.update {
            if (inboxID == REVIEW_ID_ALL) {
                showBadRatingCategoryBottomSheetForAllReviewItems()
            } else {
                showBadRatingCategoryBottomSheetForReviewItem(inboxID, it)
            }
        }
    }

    private fun showBadRatingCategoryBottomSheetForAllReviewItems(): BulkReviewBadRatingCategoryBottomSheetUiState.Showing {
        val initialBadRatingCategories = bulkReviewBadRatingCategoryMapper.map(
            badRatingCategoryRequestState = getBadRatingCategoryRequestState.value
        )
        return BulkReviewBadRatingCategoryBottomSheetUiState.Showing(
            inboxID = REVIEW_ID_ALL,
            badRatingCategories = initialBadRatingCategories
        )
    }

    private fun showBadRatingCategoryBottomSheetForReviewItem(
        inboxID: String,
        currentState: BulkReviewBadRatingCategoryBottomSheetUiState
    ): BulkReviewBadRatingCategoryBottomSheetUiState {
        val initialBadRatingCategories = bulkReviewBadRatingCategoryMapper.map(
            badRatingCategoryRequestState = getBadRatingCategoryRequestState.value
        )
        val reviewItemBadRatingCategories = reviewItemsBadRatingCategory.value.find { reviewItemBadRatingCategory ->
            reviewItemBadRatingCategory.inboxID == inboxID
        }?.badRatingCategory
        return if (!reviewItemBadRatingCategories.isNullOrEmpty() || initialBadRatingCategories.isNotEmpty()) {
            BulkReviewBadRatingCategoryBottomSheetUiState.Showing(
                inboxID = inboxID,
                badRatingCategories = reviewItemBadRatingCategories ?: initialBadRatingCategories
            )
        } else {
            currentState
        }
    }

    private fun dismissBadRatingCategoryBottomSheet() {
        _badRatingCategoryBottomSheetUiState.update {
            BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed
        }
    }

    private fun clearReviewItemBadRatingCategory(inboxID: String) {
        reviewItemsBadRatingCategory.update {
            it.filter { reviewItemBadRatingCategory ->
                reviewItemBadRatingCategory.inboxID != inboxID
            }
        }
    }

    private fun haveMinimumReviewItem(): Boolean {
        return bulkReviewVisitableList.value.count { it is BulkReviewItemUiModel } > MIN_REVIEW_ITEM
    }

    private fun showRemoveReviewItemDialog(inboxID: String) {
        _removeReviewItemDialogUiState.value = BulkReviewRemoveReviewItemDialogUiState.Showing(
            inboxID = inboxID
        )
    }

    private fun dismissRemoveReviewItemDialog() {
        _removeReviewItemDialogUiState.value = BulkReviewRemoveReviewItemDialogUiState.Dismissed
    }

    private fun showCancelReviewSubmissionDialog() {
        _cancelReviewSubmissionDialogUiState.value = BulkReviewCancelReviewSubmissionDialogUiState.Showing
    }

    private fun dismissCancelReviewSubmissionDialog() {
        _cancelReviewSubmissionDialogUiState.value = BulkReviewCancelReviewSubmissionDialogUiState.Dismissed
    }

    private fun removeReviewItem(vararg inboxID: String, notifyUser: Boolean) {
        removedReviewItemsInboxID.update { it.plus(inboxID) }
        if (notifyUser) inboxID.firstOrNull()?.let { enqueueToasterSuccessRemoveReviewItem(it) }
    }

    private fun onUndoRemoveReviewItem(inboxID: String?) {
        if (inboxID.isNullOrBlank()) return
        removedReviewItemsInboxID.update { it.toMutableSet().apply { remove(inboxID) } }
    }

    private fun updateRating(inboxID: String, rating: Int) {
        reviewItemsRating.update {
            it.toMutableList().apply {
                find { reviewItemRating ->
                    reviewItemRating.inboxID == inboxID
                }.let { previousUiModel ->
                    if (previousUiModel != null) {
                        remove(previousUiModel)
                        add(previousUiModel.copy(rating = rating))
                    } else {
                        add(BulkReviewItemRatingUiModel(inboxID = inboxID, rating = rating, animate = true))
                    }
                }
            }
        }
    }

    private fun shouldShowBadRatingCategoryBottomSheet(
        inboxID: String,
        currentRating: Int
    ) {
        if (currentRating <= ReviewConstants.BAD_RATING_CATEGORY_THRESHOLD) {
            showBadRatingCategoryBottomSheet(inboxID = inboxID)
        } else {
            clearReviewItemBadRatingCategory(inboxID)
        }
    }

    private fun dismissExpandedTextAreaBottomSheet(text: String) {
        _expandedTextAreaBottomSheetUiState.update {
            if (
                it is BulkReviewExpandedTextAreaBottomSheetUiState.Showing &&
                (it.allowEmpty || text.isNotBlank())
            ) {
                BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed
            } else {
                it
            }
        }
    }

    private fun updateBadRatingCategorySelection(
        badRatingCategoryID: String,
        selected: Boolean
    ) {
        _badRatingCategoryBottomSheetUiState.update { currentUiState ->
            if (currentUiState is BulkReviewBadRatingCategoryBottomSheetUiState.Showing) {
                currentUiState.copy(
                    badRatingCategories = currentUiState.badRatingCategories.map { badRatingCategory ->
                        if (badRatingCategory.id == badRatingCategoryID) {
                            badRatingCategory.copy(selected = selected)
                        } else {
                            badRatingCategory
                        }
                    }
                )
            } else {
                currentUiState
            }
        }
    }

    private fun shouldShowExpandedTextAreaBottomSheetOnBadRatingChanged(
        badRatingCategoryID: String,
        selected: Boolean,
        inboxID: String,
        testimony: String,
        rating: Int
    ) {
        if (badRatingCategoryID == ReviewConstants.BAD_RATING_OTHER_ID && selected) {
            showExpandedTextAreaBottomSheet(
                inboxID = inboxID,
                title = ResourceProvider.getExpandedTextAreaTitle(rating),
                hint = ResourceProvider.getBadRatingCategoryExpandedTextAreaHint(),
                text = testimony,
                allowEmpty = false
            )
            onApplyBadRatingCategory()
            dismissBadRatingCategoryBottomSheet()
        }
    }

    private fun applyBadRatingCategory() {
        val uiState = _badRatingCategoryBottomSheetUiState.value
        if (uiState is BulkReviewBadRatingCategoryBottomSheetUiState.Showing) {
            reviewItemsBadRatingCategory.update {
                if (uiState.inboxID == REVIEW_ID_ALL) {
                    applyBadRatingCategoryToReviewAllItem(uiState)
                } else {
                    applyBadRatingCategoryToReviewItem(it, uiState)
                }
            }
        }
    }

    private fun applyBadRatingCategoryToReviewItem(
        bulkReviewItemBadRatingCategoryUiModels: List<BulkReviewItemBadRatingCategoryUiModel>,
        badRatingCategoryBottomSheetUiState: BulkReviewBadRatingCategoryBottomSheetUiState.Showing
    ): List<BulkReviewItemBadRatingCategoryUiModel> {
        return bulkReviewItemBadRatingCategoryUiModels.toMutableList().apply {
            find { reviewItemBadRatingCategory ->
                reviewItemBadRatingCategory.inboxID == badRatingCategoryBottomSheetUiState.inboxID
            }.let { oldReviewItemBadRatingCategory ->
                if (oldReviewItemBadRatingCategory == null) {
                    add(
                        BulkReviewItemBadRatingCategoryUiModel(
                            inboxID = badRatingCategoryBottomSheetUiState.inboxID,
                            badRatingCategory = badRatingCategoryBottomSheetUiState.badRatingCategories
                        )
                    )
                } else {
                    remove(oldReviewItemBadRatingCategory)
                    add(
                        oldReviewItemBadRatingCategory.copy(
                            badRatingCategory = badRatingCategoryBottomSheetUiState.badRatingCategories
                        )
                    )
                }
            }
        }.toList()
    }

    private fun applyBadRatingCategoryToReviewAllItem(
        badRatingCategoryBottomSheetUiState: BulkReviewBadRatingCategoryBottomSheetUiState.Showing
    ): List<BulkReviewItemBadRatingCategoryUiModel> {
        return bulkReviewVisitableList
            .value
            .filterIsInstance<BulkReviewItemUiModel>()
            .map { reviewItem ->
            BulkReviewItemBadRatingCategoryUiModel(
                inboxID = reviewItem.inboxID,
                badRatingCategory = badRatingCategoryBottomSheetUiState.badRatingCategories
            )
        }
    }

    private fun applyExpandedTextAreaValue(text: String) {
        val uiState = _expandedTextAreaBottomSheetUiState.value
        if (uiState is BulkReviewExpandedTextAreaBottomSheetUiState.Showing) {
            if (uiState.allowEmpty || text.isNotBlank()) {
                if (uiState.inboxID == REVIEW_ID_ALL) {
                    applyExpandedTextAreaValueToReviewAllItem(text)
                } else {
                    applyExpandedTextAreaValueToReviewItem(uiState, text)
                }
            } else {
                enqueueToasterErrorEmptyBadRatingCategoryOtherReason()
            }
        }
    }

    private fun applyExpandedTextAreaValueToReviewItem(
        uiState: BulkReviewExpandedTextAreaBottomSheetUiState.Showing,
        text: String
    ) {
        reviewItemsTestimony.update {
            it.filterAndPut(
                matcher = { reviewItemTestimony ->
                    reviewItemTestimony.inboxID != uiState.inboxID
                },
                newItem = BulkReviewItemTestimonyUiModel(
                    inboxID = uiState.inboxID,
                    testimonyUiModel = ReviewTestimonyUiModel(
                        text = text,
                        shouldShowTextArea = text.isNotBlank(),
                        focused = false,
                        shouldApplyText = true
                    )
                )
            )
        }
    }

    private fun applyExpandedTextAreaValueToReviewAllItem(text: String) {
        reviewItemsTestimony.update {
            bulkReviewVisitableList
                .value
                .filterIsInstance<BulkReviewItemUiModel>()
                .map { reviewItem ->
                    BulkReviewItemTestimonyUiModel(
                        inboxID = reviewItem.inboxID,
                        testimonyUiModel = ReviewTestimonyUiModel(
                            text = text,
                            shouldShowTextArea = text.isNotBlank(),
                            focused = false,
                            shouldApplyText = true
                        )
                    )
                }
        }
    }

    private fun showReviewItemTextArea(inboxID: String) {
        reviewItemsTestimony.update {
            it.filterAndPut(
                matcher = { reviewItemTestimony ->
                    reviewItemTestimony.inboxID != inboxID
                },
                newItem = BulkReviewItemTestimonyUiModel(
                    inboxID = inboxID,
                    testimonyUiModel = ReviewTestimonyUiModel(
                        text = String.EMPTY,
                        shouldShowTextArea = true,
                        focused = true,
                        shouldApplyText = true
                    )
                )
            )
        }
    }

    private fun updateReviewItemTestimony(inboxID: String, text: String) {
        reviewItemsTestimony.update {
            it.copyAndReplace(matcher = { reviewItemTestimony ->
                reviewItemTestimony.inboxID == inboxID
            }, copier = { reviewItemTestimony ->
                reviewItemTestimony.copy(
                    testimonyUiModel = reviewItemTestimony.testimonyUiModel.copy(
                        text = text,
                        shouldApplyText = false
                    )
                )
            })
        }
    }

    private fun showExpandedTextAreaBottomSheet(
        inboxID: String,
        title: StringRes,
        hint: StringRes,
        text: String,
        allowEmpty: Boolean
    ) {
        _expandedTextAreaBottomSheetUiState.update {
            BulkReviewExpandedTextAreaBottomSheetUiState.Showing(
                inboxID = inboxID,
                title = title,
                hint = hint,
                text = text,
                allowEmpty = allowEmpty
            )
        }
    }

    private fun updateMediaUris(inboxID: String, originalPaths: List<String>) {
        reviewItemsMediaUris.update {
            it.filterAndPut(
                matcher = { reviewItemMediaUris ->
                    reviewItemMediaUris.inboxID != inboxID
                },
                newItem = BulkReviewItemMediaUrisUiModel(
                    inboxID = inboxID,
                    uris = originalPaths,
                    shouldResetFailedUploadStatus = true
                )
            )
        }
    }

    private fun incrementReviewItemMediaUploadBatchNumber(inboxID: String) {
        reviewItemsMediaUploadBatchNumber.update {
            it.toMutableList().apply {
                find { reviewItemUploadBatchNumber ->
                    reviewItemUploadBatchNumber.inboxID == inboxID
                }.let { previousReviewItemUploadBatchNumber ->
                    if (previousReviewItemUploadBatchNumber == null) {
                        add(
                            BulkReviewItemMediaUploadBatchNumberUiModel(
                                inboxID = inboxID,
                                batchNumber = Int.ONE
                            )
                        )
                    } else {
                        remove(previousReviewItemUploadBatchNumber)
                        add(
                            previousReviewItemUploadBatchNumber.copy(
                                batchNumber = previousReviewItemUploadBatchNumber.batchNumber.inc()
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateReviewItemsMediaUrisForRetryUpload(inboxID: String) {
        reviewItemsMediaUris.update {
            it.copyAndReplace(matcher = { reviewItemMediaUris ->
                reviewItemMediaUris.inboxID == inboxID
            }, copier = { reviewItemMediaUris ->
                    reviewItemMediaUris.copy(shouldResetFailedUploadStatus = true)
                })
        }
    }

    private fun getForms(invoice: String, utmSource: String) {
        viewModelScope.launch(coroutineDispatchers.io) {
            getFormUseCase(
                BulkReviewGetFormRequestParams(invoice, utmSource)
            ).collectLatest { requestState ->
                getFormRequestState.value = requestState
            }
        }
    }

    private fun getBadRatingCategory() {
        viewModelScope.launch(coroutineDispatchers.io) {
            getBadRatingCategoryUseCase(Unit).collectLatest { requestState ->
                getBadRatingCategoryRequestState.value = requestState
            }
        }
    }

    private fun getAllReviewItem(): List<BulkReviewItemUiModel> {
        return bulkReviewVisitableList.value.filterIsInstance<BulkReviewItemUiModel>()
    }

    private fun getReviewItem(inboxID: String): BulkReviewItemUiModel? {
        return bulkReviewVisitableList.value.find {
            it is BulkReviewItemUiModel && it.inboxID == inboxID
        } as? BulkReviewItemUiModel
    }

    private fun getBulkReviewBadRatingCategoryBottomSheetReferencedReviewItem(): BulkReviewItemUiModel? {
        val badRatingCategoryBottomSheetUiState = badRatingCategoryBottomSheetUiState.value
        return if (badRatingCategoryBottomSheetUiState is BulkReviewBadRatingCategoryBottomSheetUiState.Showing) {
            getReviewItem(badRatingCategoryBottomSheetUiState.inboxID)
        } else {
            null
        }
    }

    private fun getFirstItemWithFailMediaUpload(): BulkReviewItemUiModel? {
        return bulkReviewVisitableList
            .value
            .filterIsInstance<BulkReviewItemUiModel>()
            .firstOrNull {
                it.inboxID !in removedReviewItemsInboxID.value && it.isMediaUploadFailed()
            }
    }

    private fun getFirstItemWithUploadingMedia(): BulkReviewItemUiModel? {
        return bulkReviewVisitableList
            .value
            .filterIsInstance<BulkReviewItemUiModel>()
            .firstOrNull {
                it.inboxID !in removedReviewItemsInboxID.value && it.isUploadingMedia()
            }
    }

    private fun getFirstItemWithEmptyOtherReasonTestimony(): BulkReviewItemUiModel? {
        return bulkReviewVisitableList
            .value
            .filterIsInstance<BulkReviewItemUiModel>()
            .firstOrNull {
                it.inboxID !in removedReviewItemsInboxID.value && it.hasEmptyOtherReasonTestimony()
            }
    }

    private fun <T> List<T>.copyAndReplace(
        matcher: (T) -> Boolean,
        copier: (T) -> T
    ): List<T> {
        return toMutableList().apply {
            find { item ->
                matcher(item)
            }?.let { matchingItem ->
                remove(matchingItem)
                add(copier(matchingItem))
            }
        }
    }

    private fun <T> List<T>.filterAndPut(
        matcher: (T) -> Boolean,
        newItem: T
    ): List<T> {
        return filter { item -> matcher(item) }.toMutableList().apply { add(newItem) }
    }

    private fun sendReviewItemImpressionEventTracker(reviewItem: BulkReviewItemUiModel) {
        bulkWriteReviewTracker.trackReviewItemImpression(
            position = reviewItem.position,
            orderId = reviewItem.orderID,
            reputationId = reviewItem.reputationID,
            productId = reviewItem.getReviewItemProductId(),
            userId = getUserId()
        )
    }

    private fun sendReviewItemSubmissionEventTracker(reviewItem: BulkReviewItemUiModel) {
        bulkWriteReviewTracker.trackReviewItemSubmission(
            position = reviewItem.position,
            orderId = reviewItem.orderID,
            reputationId = reviewItem.reputationID,
            productId = reviewItem.getReviewItemProductId(),
            userId = getUserId(),
            rating = reviewItem.getReviewItemRating(),
            isReviewEmpty = reviewItem.isReviewItemHasEmptyReview(),
            reviewLength = reviewItem.getReviewItemReviewLength(),
            imageAttachmentCount = reviewItem.getReviewItemImageAttachmentCount(),
            videoAttachmentCount = reviewItem.getReviewItemVideoAttachmentCount(),
            isAnonymous = isAnonymous()
        )
    }

    private fun sendPageDismissalEventTracker() {
        getAllReviewItem().forEach { reviewItem ->
            bulkWriteReviewTracker.trackPageDismissal(
                position = reviewItem.position,
                orderId = reviewItem.orderID,
                reputationId = reviewItem.reputationID,
                productId = reviewItem.getReviewItemProductId(),
                userId = getUserId(),
                rating = reviewItem.getReviewItemRating(),
                isReviewEmpty = reviewItem.isReviewItemHasEmptyReview(),
                reviewLength = reviewItem.getReviewItemReviewLength(),
                imageAttachmentCount = reviewItem.getReviewItemImageAttachmentCount(),
                videoAttachmentCount = reviewItem.getReviewItemVideoAttachmentCount(),
                isAnonymous = isAnonymous()
            )
        }
    }

    private fun sendReviewItemBadRatingCategoryImpressionEventTracker(position: Int, reason: String) {
        getBulkReviewBadRatingCategoryBottomSheetReferencedReviewItem()?.let { reviewItem ->
            bulkWriteReviewTracker.trackReviewItemBadRatingCategoryImpression(
                position = position,
                orderId = reviewItem.orderID,
                reputationId = reviewItem.reputationID,
                productId = reviewItem.getReviewItemProductId(),
                userId = getUserId(),
                rating = reviewItem.getReviewItemRating(),
                reason = reason
            )
        }
    }

    private fun sendReviewItemBadRatingCategorySelectedEventTracker(
        position: Int,
        reason: String,
        reviewItem: BulkReviewItemUiModel
    ) {
        bulkWriteReviewTracker.trackReviewItemBadRatingCategorySelected(
            position = position,
            orderId = reviewItem.orderID,
            reputationId = reviewItem.reputationID,
            productId = reviewItem.getReviewItemProductId(),
            userId = getUserId(),
            rating = reviewItem.getReviewItemRating(),
            reason = reason
        )
    }

    private fun sendReviewItemPartialErrorSubmissionEventTracker(reviewItem: BulkReviewItemUiModel) {
        bulkWriteReviewTracker.trackReviewItemSubmissionError(
            position = reviewItem.position,
            orderId = reviewItem.orderID,
            reputationId = reviewItem.reputationID,
            productId = reviewItem.getReviewItemProductId(),
            userId = getUserId(),
            errorMessage = ResourceProvider.getMessageReviewItemPartiallySubmitted(),
            rating = reviewItem.getReviewItemRating(),
            isReviewEmpty = reviewItem.isReviewItemHasEmptyReview(),
            reviewLength = reviewItem.getReviewItemReviewLength(),
            imageAttachmentCount = reviewItem.getReviewItemImageAttachmentCount(),
            videoAttachmentCount = reviewItem.getReviewItemVideoAttachmentCount(),
            isAnonymous = isAnonymous()
        )
    }

    private fun sendReviewItemFullyErrorSubmissionEventTracker(reviewItem: BulkReviewItemUiModel) {
        bulkWriteReviewTracker.trackReviewItemSubmissionError(
            position = reviewItem.position,
            orderId = reviewItem.orderID,
            reputationId = reviewItem.reputationID,
            productId = reviewItem.getReviewItemProductId(),
            userId = getUserId(),
            errorMessage = ResourceProvider.getMessageReviewItemSubmissionFullyError(),
            rating = reviewItem.getReviewItemRating(),
            isReviewEmpty = reviewItem.isReviewItemHasEmptyReview(),
            reviewLength = reviewItem.getReviewItemReviewLength(),
            imageAttachmentCount = reviewItem.getReviewItemImageAttachmentCount(),
            videoAttachmentCount = reviewItem.getReviewItemVideoAttachmentCount(),
            isAnonymous = isAnonymous()
        )
    }

    private fun sendReviewItemRemoveButtonClickEventTracker(reviewItem: BulkReviewItemUiModel) {
        bulkWriteReviewTracker.trackReviewItemRemoveButtonClick(
            position = reviewItem.position,
            orderId = reviewItem.orderID,
            reputationId = reviewItem.reputationID,
            productId = reviewItem.getReviewItemProductId(),
            userId = getUserId()
        )
    }

    private fun sendRemoveReviewItemDialogImpressionEventTracker(
        inboxID: String,
        title: String,
        subtitle: String
    ) {
        getReviewItem(inboxID)?.let { reviewItem ->
            bulkWriteReviewTracker.trackRemoveReviewItemDialogImpression(
                position = reviewItem.position,
                orderId = reviewItem.orderID,
                reputationId = reviewItem.reputationID,
                productId = reviewItem.getReviewItemProductId(),
                userId = getUserId(),
                title = title,
                subtitle = subtitle
            )
        }
    }

    private fun sendRemoveReviewItemDialogCancelButtonClickEventTracker(title: String, subtitle: String) {
        bulkWriteReviewTracker.trackRemoveReviewItemDialogCancelButtonClick(
            userId = getUserId(),
            title = title,
            subtitle = subtitle
        )
    }

    private fun sendRemoveReviewItemDialogRemoveButtonClickEventTracker(title: String, subtitle: String) {
        bulkWriteReviewTracker.trackRemoveReviewItemDialogRemoveButtonClick(
            userId = getUserId(),
            title = title,
            subtitle = subtitle
        )
    }

    private fun sendReviewItemAddTestimonyMiniActionClickEventTracker(inboxID: String) {
        getReviewItem(inboxID)?.let { reviewItem ->
            bulkWriteReviewTracker.trackReviewItemAddTestimonyMiniActionClick(
                position = reviewItem.position,
                orderId = reviewItem.orderID,
                reputationId = reviewItem.reputationID,
                productId = reviewItem.getReviewItemProductId(),
                userId = getUserId()
            )
        }
    }

    private fun sendReviewItemAddAttachmentMiniActionClickEventTracker(inboxID: String) {
        getReviewItem(inboxID)?.let { reviewItem ->
            bulkWriteReviewTracker.trackReviewItemAddAttachmentMiniActionClick(
                position = reviewItem.position,
                orderId = reviewItem.orderID,
                reputationId = reviewItem.reputationID,
                productId = reviewItem.getReviewItemProductId(),
                userId = getUserId()
            )
        }
    }

    private fun sendReviewItemRatingChangedEventTracker(reviewItem: BulkReviewItemUiModel, rating: Int) {
        bulkWriteReviewTracker.trackReviewItemRatingChanged(
            position = reviewItem.position,
            orderId = reviewItem.orderID,
            reputationId = reviewItem.reputationID,
            productId = reviewItem.getReviewItemProductId(),
            userId = getUserId(),
            rating = rating,
            timestamp = System.currentTimeMillis()
        )
    }

    private fun scrollToReviewItemVisitable(reviewItem: BulkReviewItemUiModel) {
        viewModelScope.launch { _reviewItemScrollRequest.emit(reviewItem) }
    }
}
