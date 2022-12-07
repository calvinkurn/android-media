package com.tokopedia.review.feature.bulk_write_review.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.bulk_write_review.domain.model.RequestState
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetBadRatingCategoryUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetFormUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewSubmitUseCase
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewBadRatingCategoryMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewBadRatingCategoryUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewMediaPickerUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewMiniActionsUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewPageUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewProductInfoUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewRatingUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewStickyButtonMapper
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
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewExpandedTextAreaBottomSheetUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewItemUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionsUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewPageUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewProductInfoUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRemoveReviewItemDialogUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewStickyButtonUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewMediaUploadResult
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewToasterUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.review.feature.createreputation.util.CreateReviewMapper
import com.tokopedia.reviewcommon.extension.combine
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class BulkReviewViewModel @Inject constructor(
    private val getFormUseCase: BulkReviewGetFormUseCase,
    private val getBadRatingCategoryUseCase: BulkReviewGetBadRatingCategoryUseCase,
    private val uploaderUseCase: UploaderUseCase,
    private val submitUseCase: BulkReviewSubmitUseCase,
    private val userSession: UserSessionInterface,
    private val bulkReviewProductInfoUiStateMapper: BulkReviewProductInfoUiStateMapper,
    private val bulkReviewRatingUiStateMapper: BulkReviewRatingUiStateMapper,
    private val bulkReviewBadRatingCategoryUiStateMapper: BulkReviewBadRatingCategoryUiStateMapper,
    private val bulkReviewTextAreaUiStateMapper: BulkReviewTextAreaUiStateMapper,
    private val bulkReviewMediaPickerUiStateMapper: BulkReviewMediaPickerUiStateMapper,
    private val bulkReviewMiniActionsUiStateMapper: BulkReviewMiniActionsUiStateMapper,
    private val bulkReviewVisitableMapper: BulkReviewVisitableMapper,
    private val bulkReviewStickyButtonMapper: BulkReviewStickyButtonMapper,
    private val bulkReviewPageUiStateMapper: BulkReviewPageUiStateMapper,
    private val bulkReviewBadRatingCategoryMapper: BulkReviewBadRatingCategoryMapper
) : ViewModel() {

    companion object {
        const val BAD_RATING_CATEGORY_THRESHOLD = 2
        private const val STATE_FLOW_TIMEOUT_MILLIS = 5000L
        private const val UPDATE_POEM_INTERVAL = 1000L
        private const val MIN_REVIEW_ITEM = 1
    }

    private val getFormRequestState = MutableStateFlow<BulkReviewGetFormRequestState>(RequestState.Requesting)
    private val getBadRatingCategoryRequestState = MutableStateFlow<BulkReviewGetBadRatingCategoryRequestState>(RequestState.Requesting)
    private val submitBulkReviewRequestState = MutableStateFlow<BulkReviewSubmitRequestState>(RequestState.Requesting)
    private val removedReviewItemsInboxID = MutableStateFlow(emptyList<String>())
    private val reviewItemsRating = MutableStateFlow(emptyList<BulkReviewItemRatingUiModel>())
    private val reviewItemsBadRatingCategory = MutableStateFlow(emptyList<BulkReviewItemBadRatingCategoryUiModel>())
    private val reviewItemsTestimony = MutableStateFlow(emptyList<BulkReviewItemTestimonyUiModel>())
    private val reviewItemsMediaUris = MutableStateFlow(emptyList<BulkReviewItemMediaUrisUiModel>())
    private val reviewItemsMediaUploadJobs = MutableStateFlow(emptyList<BulkReviewItemMediaUploadJobsUiModel>())
    private val reviewItemsMediaUploadResults = MutableStateFlow(emptyList<BulkReviewItemMediaUploadResultsUiModel>())
    private val reviewItemsUploadBatchNumber = MutableStateFlow(emptyList<BulkReviewItemMediaUploadBatchNumberUiModel>())
    private val anonymous = MutableStateFlow(false)
    private val shouldSubmitReview = MutableStateFlow(false)
    private var activeMediaPickerInboxID = ""
    private val reviewItemsProductInfoUiState = getFormRequestState.mapLatest(
        ::mapProductInfoUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsRatingUiState = combine(
        getFormRequestState,
        reviewItemsRating,
        ::mapRatingUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsBadRatingCategoryUiState = combine(
        getFormRequestState,
        reviewItemsBadRatingCategory,
        ::mapBadRatingCategoryUiState
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
        ::mapTextAreaUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsMediaItems = combine(
        reviewItemsMediaUris,
        reviewItemsMediaUploadResults,
        reviewItemsMediaUploadJobs,
        reviewItemsUploadBatchNumber,
        ::mapMediaItems
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val poemUpdateSignal = flow {
        while (true) {
            delay(UPDATE_POEM_INTERVAL)
            emit(Unit)
        }
    }
    private val reviewItemsPoem = combine(
        reviewItemsMediaItems,
        poemUpdateSignal,
        ::mapPoem
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val reviewItemsMediaPickerUiState = combine(
        getFormRequestState,
        reviewItemsMediaItems,
        reviewItemsPoem,
        reviewItemsUploadBatchNumber,
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
        ::mapMiniActionsUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = emptyMap()
    )
    private val bulkReviewVisitableList = combine(
        getFormRequestState,
        removedReviewItemsInboxID,
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
        bulkReviewVisitableList,
        anonymous,
        ::mapBulkReviewStickyButtonUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = BulkReviewStickyButtonUiState.Hidden
    )
    private val _removeReviewItemDialogUiState = MutableStateFlow<BulkReviewRemoveReviewItemDialogUiState>(BulkReviewRemoveReviewItemDialogUiState.Dismissed)
    val removeReviewItemDialogUiState: StateFlow<BulkReviewRemoveReviewItemDialogUiState>
        get() = _removeReviewItemDialogUiState
    private val _badRatingCategoryBottomSheetUiState = MutableStateFlow<BulkReviewBadRatingCategoryBottomSheetUiState>(BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)
    val badRatingCategoryBottomSheetUiState: StateFlow<BulkReviewBadRatingCategoryBottomSheetUiState>
        get() = _badRatingCategoryBottomSheetUiState
    private val _expandedTextAreaBottomSheetUiState = MutableStateFlow<BulkReviewExpandedTextAreaBottomSheetUiState>(BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)
    val expandedTextAreaBottomSheetUiState: StateFlow<BulkReviewExpandedTextAreaBottomSheetUiState>
        get() = _expandedTextAreaBottomSheetUiState
    private val _bulkReviewPageToasterQueue = MutableSharedFlow<CreateReviewToasterUiModel>(extraBufferCapacity = 50)
    val bulkReviewPageToasterQueue: Flow<CreateReviewToasterUiModel>
        get() = _bulkReviewPageToasterQueue
    private val _expandedTextAreaToasterQueue = MutableSharedFlow<CreateReviewToasterUiModel>(extraBufferCapacity = 50)
    val expandedTextAreaToasterQueue: Flow<CreateReviewToasterUiModel>
        get() = _expandedTextAreaToasterQueue
    val bulkReviewPageUiState = combine(
        shouldSubmitReview,
        submitBulkReviewRequestState,
        bulkReviewVisitableList.filter { it.isNotEmpty() },
        bulkReviewStickyButtonUiState,
        getFormRequestState,
        getBadRatingCategoryRequestState,
        bulkReviewPageUiStateMapper::map
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_TIMEOUT_MILLIS),
        initialValue = BulkReviewPageUiState.Loading
    )

    init {
        observeMediaUrisForUpload()
        handleMediaPickerErrorToasterQueue()
        handleSubmitReviews()
    }

    fun getForms() {
        viewModelScope.launch {
            getFormUseCase(userSession.userId).collectLatest {
                getFormRequestState.value = it
            }
        }
    }

    fun getBadRatingCategory() {
        viewModelScope.launch {
            getBadRatingCategoryUseCase(Unit).collectLatest {
                getBadRatingCategoryRequestState.value = it
            }
        }
    }

    fun onRemoveReviewItem(inboxID: String) {
        if (bulkReviewVisitableList.value.count { it is BulkReviewItemUiModel } > MIN_REVIEW_ITEM) {
            (bulkReviewVisitableList.value.find {
                it is BulkReviewItemUiModel && it.inboxID == inboxID
            } as? BulkReviewItemUiModel)?.hasDefaultState()?.let { hasDefaultState ->
                if (hasDefaultState) {
                    removeReviewItem(inboxID)
                } else {
                    _removeReviewItemDialogUiState.value = BulkReviewRemoveReviewItemDialogUiState.Showing(
                        inboxID = inboxID
                    )
                }
            }
        } else {
            enqueueToasterErrorCannotRemoveAllReviewItems()
        }
    }

    fun dismissRemoveReviewItemDialog() {
        _removeReviewItemDialogUiState.value = BulkReviewRemoveReviewItemDialogUiState.Dismissed
    }

    fun removeReviewItem(inboxID: String) {
        removedReviewItemsInboxID.update { it.plus(inboxID) }
        enqueueToasterSuccessRemoveReviewItem()
    }

    fun onRatingChanged(inboxID: String, rating: Int) {
        val previousRating = reviewItemsRating.getAndUpdate {
            it.toMutableList().apply {
                find { reviewItemRating ->
                    reviewItemRating.inboxID == inboxID
                }.let { previousUiModel ->
                    if (previousUiModel != null) {
                        remove(previousUiModel)
                        add(previousUiModel.copy(rating = rating))
                    } else {
                        add(BulkReviewItemRatingUiModel(inboxID = inboxID, rating = rating))
                    }
                }
            }
        }.find {
            it.inboxID == inboxID
        }?.rating ?: BulkReviewRatingUiStateMapper.DEFAULT_PRODUCT_RATING
        if (rating <= BAD_RATING_CATEGORY_THRESHOLD) {
            if (previousRating > BAD_RATING_CATEGORY_THRESHOLD) {
                showBadRatingCategoryBottomSheet(inboxID = inboxID)
            } else {
                // ignore, user need to click `Ubah` to show the bad rating categories bottomsheet
            }
        } else {
            clearReviewItemBadRatingCategory(inboxID)
        }
    }

    fun dismissBadRatingCategoryBottomSheet() {
        _badRatingCategoryBottomSheetUiState.update {
            BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed
        }
    }

    fun dismissExpandedTextAreaBottomSheet(text: String) {
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

    fun onBadRatingCategorySelectionChanged(badRatingCategoryID: String, selected: Boolean) {
        _badRatingCategoryBottomSheetUiState.update { currentUiState ->
            if (currentUiState is BulkReviewBadRatingCategoryBottomSheetUiState.Showing) {
                currentUiState.copy(
                    badRatingCategories = currentUiState.badRatingCategories.map { badRatingCategory ->
                        if (badRatingCategory.id == badRatingCategoryID) {
                            badRatingCategory.copy(selected = selected)
                        } else badRatingCategory
                    }
                )
            } else {
                currentUiState
            }
        }
        if (badRatingCategoryID == ReviewConstants.BAD_RATING_OTHER_ID == selected) {
            (_badRatingCategoryBottomSheetUiState.value as? BulkReviewBadRatingCategoryBottomSheetUiState.Showing)?.inboxID?.let { inboxID ->
                showExpandedTextAreaBottomSheet(
                    inboxID = inboxID,
                    title = StringRes(R.string.bulk_review_bad_rating_category_bottom_sheet_title),
                    hint = StringRes(R.string.hint_bulk_review_bad_rating_category_expanded_text_area),
                    text = getReviewItemTestimony(inboxID),
                    allowEmpty = false
                )
            }
            applyBadRatingCategory()
            dismissBadRatingCategoryBottomSheet()
        }
    }

    fun getReviewItemTestimony(inboxID: String): String {
        return (reviewItemsTextAreaUiState.value[inboxID] as? BulkReviewTextAreaUiState.Showing)?.text.orEmpty()
    }

    fun getReviewItemHint(inboxID: String): StringRes {
        return (reviewItemsTextAreaUiState.value[inboxID] as? BulkReviewTextAreaUiState.Showing)?.hint ?: StringRes(Int.ZERO)
    }

    fun applyBadRatingCategory() {
        val uiState = _badRatingCategoryBottomSheetUiState.value
        if (uiState is BulkReviewBadRatingCategoryBottomSheetUiState.Showing) {
            reviewItemsBadRatingCategory.update {
                it.toMutableList().apply {
                    find { reviewItemBadRatingCategory ->
                        reviewItemBadRatingCategory.inboxID == uiState.inboxID
                    }.let { oldReviewItemBadRatingCategory ->
                        if (oldReviewItemBadRatingCategory == null) {
                            add(
                                BulkReviewItemBadRatingCategoryUiModel(
                                    inboxID = uiState.inboxID,
                                    badRatingCategory = uiState.badRatingCategories
                                )
                            )
                        } else {
                            remove(oldReviewItemBadRatingCategory)
                            add(
                                oldReviewItemBadRatingCategory.copy(
                                    badRatingCategory = uiState.badRatingCategories
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun applyExpandedTextAreaValue(text: String) {
        val uiState = _expandedTextAreaBottomSheetUiState.value
        if (uiState is BulkReviewExpandedTextAreaBottomSheetUiState.Showing) {
            if (uiState.allowEmpty || text.isNotBlank()) {
                reviewItemsTestimony.update {
                    it.filter { reviewItemTestimony ->
                        reviewItemTestimony.inboxID != uiState.inboxID
                    }.toMutableList().apply {
                        add(
                            BulkReviewItemTestimonyUiModel(
                                inboxID = uiState.inboxID,
                                testimonyUiModel = ReviewTestimonyUiModel(
                                    text = text,
                                    showTextArea = text.isNotBlank(),
                                    focused = false
                                )
                            )
                        )
                    }
                }
            } else {
                enqueueToasterErrorEmptyBadRatingCategoryOtherReason()
            }
        }
    }

    fun onChangeBadRatingCategory(inboxID: String) {
        showBadRatingCategoryBottomSheet(inboxID)
    }

    fun showReviewItemTextArea(inboxID: String) {
        reviewItemsTestimony.update {
            it.filter { reviewItemTestimony ->
                reviewItemTestimony.inboxID != inboxID
            }.toMutableList().apply {
                add(
                    BulkReviewItemTestimonyUiModel(
                        inboxID = inboxID,
                        testimonyUiModel = ReviewTestimonyUiModel(
                            text = String.EMPTY,
                            showTextArea = true,
                            focused = true
                        )
                    )
                )
            }
        }
    }

    fun onReviewItemTextAreaGainFocus(inboxID: String) {
        reviewItemsTestimony.update {
            it.toMutableList().apply {
                find { reviewItemTestimony ->
                    reviewItemTestimony.inboxID == inboxID
                }?.let { previousReviewItemTestimony ->
                    remove(previousReviewItemTestimony)
                    add(
                        previousReviewItemTestimony.copy(
                            testimonyUiModel = previousReviewItemTestimony.testimonyUiModel.copy(
                                focused = true
                            )
                        )
                    )
                }
            }
        }
    }

    fun onReviewItemTextAreaLostFocus(inboxID: String, text: String) {
        reviewItemsTestimony.update {
            it.filterNot { reviewItemTestimony ->
                reviewItemTestimony.inboxID == inboxID
            }.toMutableList().apply {
                add(
                    BulkReviewItemTestimonyUiModel(
                        inboxID = inboxID,
                        testimonyUiModel = ReviewTestimonyUiModel(
                            text = text,
                            showTextArea = text.isNotBlank(),
                            focused = false
                        )
                    )
                )
            }
        }
    }

    fun updateReviewItemTestimony(inboxID: String, text: String) {
        reviewItemsTestimony.update {
            it.toMutableList().apply {
                find { reviewItemTestimony ->
                    reviewItemTestimony.inboxID == inboxID
                }?.let { previousReviewItemTestimony ->
                    remove(previousReviewItemTestimony)
                    add(
                        previousReviewItemTestimony.copy(
                            testimonyUiModel = previousReviewItemTestimony.testimonyUiModel.copy(
                                text = text
                            )
                        )
                    )
                }
            }
        }
    }

    fun showExpandedTextAreaBottomSheet(
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

    fun findFocusedVisitable(): Pair<Int, BulkReviewVisitable<BulkReviewAdapterTypeFactory>>? {
        return bulkReviewVisitableList.value.find {
            it is BulkReviewItemUiModel && it.uiState is BulkReviewItemUiState.Focused
        }?.let { bulkReviewVisitableList.value.indexOf(it) to it }
    }

    fun updateMediaUris(inboxID: String, originalPaths: List<String>) {
        reviewItemsMediaUris.update {
            it.filter { reviewItemMediaUris ->
                reviewItemMediaUris.inboxID != inboxID
            }.toMutableList().apply {
                add(
                    BulkReviewItemMediaUrisUiModel(
                        inboxID = inboxID,
                        uris = originalPaths,
                        shouldResetFailedUploadStatus = true
                    )
                )
            }
        }
    }

    fun retryUploadMedia(inboxID: String) {
        reviewItemsUploadBatchNumber.update {
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
        reviewItemsMediaUris.update {
            it.toMutableList().apply {
                find { reviewItemMediaUris ->
                    reviewItemMediaUris.inboxID == inboxID
                }?.let { previousReviewItemMediaUris ->
                    remove(previousReviewItemMediaUris)
                    add(previousReviewItemMediaUris.copy(shouldResetFailedUploadStatus = true))
                }
            }
        }
    }

    fun getAndUpdateActiveMediaPickerInboxID(inboxID: String): String {
        val previousValue = activeMediaPickerInboxID
        activeMediaPickerInboxID = inboxID
        return previousValue
    }

    fun enqueueToasterDisabledAddMoreMedia() {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = StringRes(R.string.review_form_cannot_add_more_media_while_uploading),
                actionText = StringRes(Int.ZERO),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL
            )
        )
    }

    fun removeMedia(inboxID: String, media: CreateReviewMediaUiModel) {
        reviewItemsMediaUris.update {
            it.toMutableList().apply {
                find { reviewItemMediaUris ->
                    reviewItemMediaUris.inboxID == inboxID
                }?.let { previousReviewItemMediaUris ->
                    remove(previousReviewItemMediaUris)
                    add(
                        previousReviewItemMediaUris.copy(
                            uris = previousReviewItemMediaUris.uris.filter { uri ->
                                uri != media.uri
                            }
                        )
                    )
                }
            }
        }
    }

    fun onAnonymousCheckChanged(checked: Boolean) {
        anonymous.value = checked
    }

    fun submitReview() {
        reviewItemsMediaPickerUiState.value.let { currentMediaPickerUiState ->
            val firstFailMediaPickerUiState = currentMediaPickerUiState.values.firstOrNull {
                it is CreateReviewMediaPickerUiState.FailedUpload
            } as? CreateReviewMediaPickerUiState.FailedUpload
            val firstUploadingMediaPickerUiState = currentMediaPickerUiState.values.firstOrNull {
                it is CreateReviewMediaPickerUiState.Uploading
            } as? CreateReviewMediaPickerUiState.Uploading
            if (firstFailMediaPickerUiState != null) {
                enqueueToasterErrorUploadMedia(firstFailMediaPickerUiState.errorCode)
            } else if (firstUploadingMediaPickerUiState != null) {
                enqueueToasterWaitForUploadMedia()
            } else {
                shouldSubmitReview.value = true
            }
        }
    }

    private fun mapProductInfoUiState(
        getFormRequestState: BulkReviewGetFormRequestState
    ): Map<String, BulkReviewProductInfoUiState> {
        return bulkReviewProductInfoUiStateMapper.map(getFormRequestState)
    }

    private fun mapRatingUiState(
        getFormRequestState: BulkReviewGetFormRequestState,
        reviewItemsRating: List<BulkReviewItemRatingUiModel>
    ): Map<String, BulkReviewRatingUiState> {
        return bulkReviewRatingUiStateMapper.map(
            getFormRequestState = getFormRequestState,
            reviewItemsRating = reviewItemsRating
        )
    }

    private fun mapBadRatingCategoryUiState(
        getFormRequestState: BulkReviewGetFormRequestState,
        reviewItemsBadRatingCategory: List<BulkReviewItemBadRatingCategoryUiModel>
    ): Map<String, BulkReviewBadRatingCategoryUiState> {
        return bulkReviewBadRatingCategoryUiStateMapper.map(
            getFormRequestState = getFormRequestState,
            reviewItemsBadRatingCategory = reviewItemsBadRatingCategory
        )
    }

    private fun mapTextAreaUiState(
        getFormRequestState: BulkReviewGetFormRequestState,
        reviewItemsTestimony: List<BulkReviewItemTestimonyUiModel>,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>,
    ): Map<String, BulkReviewTextAreaUiState> {
        return bulkReviewTextAreaUiStateMapper.map(
            getFormRequestState = getFormRequestState,
            reviewItemsTestimony = reviewItemsTestimony,
            bulkReviewBadRatingCategoryUiState = bulkReviewBadRatingCategoryUiState,
            bulkReviewRatingUiState = bulkReviewRatingUiState
        )
    }

    private fun mapMiniActionsUiState(
        getFormRequestState: BulkReviewGetFormRequestState,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>
    ): Map<String, BulkReviewMiniActionsUiState> {
        return bulkReviewMiniActionsUiStateMapper.map(
            getFormRequestState = getFormRequestState,
            bulkReviewTextAreaUiState = bulkReviewTextAreaUiState,
            bulkReviewMediaPickerUiState = bulkReviewMediaPickerUiState
        )
    }

    private fun mapBulkReviewVisitableList(
        getFormRequestState: BulkReviewGetFormRequestState,
        removedReviewItem: List<String>,
        bulkReviewProductInfoUiState: Map<String, BulkReviewProductInfoUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>,
        bulkReviewMiniActionsUiState: Map<String, BulkReviewMiniActionsUiState>,
    ): List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>> {
        return when (getFormRequestState) {
            is RequestState.Complete.Success -> bulkReviewVisitableMapper.map(
                productRevGetBulkForm = getFormRequestState.result,
                removedReviewItem = removedReviewItem,
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

    private fun mapBulkReviewStickyButtonUiState(
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        anonymous: Boolean
    ): BulkReviewStickyButtonUiState {
        return bulkReviewStickyButtonMapper.map(
            bulkReviewVisitableList = bulkReviewVisitableList,
            anonymous = anonymous
        )
    }

    private fun observeMediaUrisForUpload() {
        viewModelScope.launch {
            reviewItemsMediaUris.collectLatest { reviewItemsMediaUris ->
                reviewItemsMediaUris.forEach { reviewItemMediaUris ->
                    tryUploadMedia(reviewItemMediaUris)
                }
            }
        }
    }

    private fun handleMediaPickerErrorToasterQueue() {
        viewModelScope.launch {
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
        viewModelScope.launch {
            combine(
                shouldSubmitReview,
                bulkReviewVisitableList,
                ::mapSubmitReviewParams
            ).filterNotNull().collectLatest {
                submitUseCase(it).collect { submitBulkReviewRequestState.value = it }
                shouldSubmitReview.value = false
            }
        }
    }

    private fun mapSubmitReviewParams(
        shouldSubmitReview: Boolean,
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>
    ): String? {
        return if (shouldSubmitReview) "" else null
    }

    private suspend fun tryUploadMedia(reviewItemMediaUris: BulkReviewItemMediaUrisUiModel) {
        val inboxID = reviewItemMediaUris.inboxID
        cancelInvalidUploadJobs(reviewItemMediaUris)
        if (reviewItemMediaUris.shouldResetFailedUploadStatus) {
            reviewItemsMediaUploadResults.update {
                it.toMutableList().apply {
                    find { reviewItemMediaUploadResults ->
                        reviewItemMediaUploadResults.inboxID == inboxID
                    }?.let { previousReviewItemMediaUploadResults ->
                        remove(previousReviewItemMediaUploadResults)
                        add(
                            previousReviewItemMediaUploadResults.copy(
                                mediaUploadResults = previousReviewItemMediaUploadResults
                                    .mediaUploadResults
                                    .filterValues { uploadResult ->
                                        uploadResult is CreateReviewMediaUploadResult.Success
                                    }
                            )
                        )
                    }
                }
            }
            reviewItemsMediaUris.update {
                it.toMutableList().apply {
                    find { reviewItemMediaUris ->
                        reviewItemMediaUris.inboxID == inboxID
                    }?.let { previousReviewItemMediaUris ->
                        remove(previousReviewItemMediaUris)
                        add(previousReviewItemMediaUris.copy(shouldResetFailedUploadStatus = false))
                    }
                }
            }
        } else {
            reviewItemMediaUris.uris.forEach { uri ->
                val hasActiveUploadJob = reviewItemsMediaUploadJobs.value.find {
                    it.inboxID == inboxID
                }?.jobs?.get(uri)?.isActive == true
                val hasUploadResult = reviewItemsMediaUploadResults.value.find {
                    it.inboxID == inboxID
                }?.mediaUploadResults?.get(uri) != null
                val needToStartNewJob = !hasActiveUploadJob && !hasUploadResult
                if (needToStartNewJob) {
                    updateMediaUploadJobs(inboxID, uri, startNewUploadMediaJob(inboxID, uri))
                    reviewItemsMediaUploadJobs.value.find {
                        it.inboxID == inboxID
                    }?.jobs?.values?.joinAll()
                }
            }
        }
    }

    private suspend fun cancelInvalidUploadJobs(reviewItemMediaUris: BulkReviewItemMediaUrisUiModel) {
        reviewItemsMediaUploadJobs.value.find {
            it.inboxID == reviewItemMediaUris.inboxID
        }?.jobs?.forEach { (uri, job) ->
            if (uri !in reviewItemMediaUris.uris && job.isActive) job.cancelAndJoin()
        }
    }

    private fun startNewUploadMediaJob(inboxID: String, uri: String): Job {
        return viewModelScope.launchCatchError(block = {
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
                existingMediaItems = reviewItemsMediaItems.value[reviewItemMediaUris.inboxID].orEmpty(),
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
                currentValue = reviewItemsPoem.value[inboxID] ?: Pair("", StringRes(Int.ZERO)),
                mediaItems = mediaItems[inboxID].orEmpty(),
                uploadBatchNumber = mediaItems[inboxID]?.firstOrNull()?.uploadBatchNumber.orZero()
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

    private fun enqueueToasterErrorUploadMedia(errorCode: String) {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = StringRes(R.string.review_form_media_picker_toaster_failed_upload_message, listOf(errorCode)),
                actionText = StringRes(Int.ZERO),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR
            )
        )
    }

    private fun enqueueToasterWaitForUploadMedia() {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = StringRes(R.string.review_form_media_picker_toaster_wait_for_upload_message),
                actionText = StringRes(Int.ZERO),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL
            )
        )
    }

    private fun enqueueToasterErrorEmptyBadRatingCategoryOtherReason() {
        _expandedTextAreaToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = StringRes(R.string.toaster_bulk_review_bad_rating_category_reason_cannot_be_empty),
                actionText = StringRes(R.string.review_oke),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR
            )
        )
    }

    private fun enqueueToasterErrorCannotRemoveAllReviewItems() {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = StringRes(R.string.toaster_bulk_review_cannot_remove_review_item, listOf(MIN_REVIEW_ITEM)),
                actionText = StringRes(R.string.review_oke),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR
            )
        )
    }

    private fun enqueueToasterSuccessRemoveReviewItem() {
        _bulkReviewPageToasterQueue.tryEmit(
            CreateReviewToasterUiModel(
                message = StringRes(R.string.toaster_bulk_review_success_remove_review_item),
                actionText = StringRes(R.string.review_oke),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL
            )
        )
    }

    private fun showBadRatingCategoryBottomSheet(inboxID: String) {
        _badRatingCategoryBottomSheetUiState.update {
            val initialBadRatingCategories = bulkReviewBadRatingCategoryMapper.map(
                badRatingCategoryRequestState = getBadRatingCategoryRequestState.value
            )
            val reviewItemBadRatingCategories = reviewItemsBadRatingCategory.value.find { reviewItemBadRatingCategory ->
                reviewItemBadRatingCategory.inboxID == inboxID
            }?.badRatingCategory
            if (!reviewItemBadRatingCategories.isNullOrEmpty() || initialBadRatingCategories.isNotEmpty()) {
                BulkReviewBadRatingCategoryBottomSheetUiState.Showing(
                    inboxID = inboxID,
                    badRatingCategories = reviewItemBadRatingCategories ?: initialBadRatingCategories
                )
            } else {
                it
            }
        }
    }

    private fun clearReviewItemBadRatingCategory(inboxID: String) {
        reviewItemsBadRatingCategory.update {
            it.filter { reviewItemBadRatingCategory ->
                reviewItemBadRatingCategory.inboxID != inboxID
            }
        }
    }
}
