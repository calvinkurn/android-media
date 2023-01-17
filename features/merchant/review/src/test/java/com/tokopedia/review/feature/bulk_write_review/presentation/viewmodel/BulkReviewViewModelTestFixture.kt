package com.tokopedia.review.feature.bulk_write_review.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.picker.common.utils.getFileFormatByMimeType
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestParam
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitResponse
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetBadRatingCategoryUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetFormUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewSubmitUseCase
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
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemMediaUploadResultsUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemMediaUrisUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemRatingUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemTestimonyUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryBottomSheetUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewCancelReviewSubmissionDialogUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewExpandedTextAreaBottomSheetUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewPageUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRemoveReviewItemDialogUiState
import com.tokopedia.review.feature.createreputation.model.BadRatingCategoriesResponse
import com.tokopedia.review.feature.createreputation.model.BadRatingCategory
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewToasterUiModel
import com.tokopedia.review.utils.createSuccessResponse
import com.tokopedia.reviewcommon.extension.get
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BulkReviewViewModelTestFixture {

    companion object {
        const val SAMPLE_USER_ID = "2546"
        const val SAMPLE_ERROR_MESSAGE = "Something went wrong!"
        const val SAMPLE_ERROR_CODE = "IS-WKWK"
        const val SAMPLE_GET_BAD_RATING_CATEGORY_RESULT_SUCCESS_NON_EMPTY =
            "json/get_bad_rating_category_use_case_result_success_non_empty.json"
        const val SAMPLE_GET_FORM_RESULT_SUCCESS =
            "json/bulk_write_review/get_form_use_case_result_success.json"
    }

    @get:Rule
    val rule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var getFormUseCase: BulkReviewGetFormUseCase

    @RelaxedMockK
    lateinit var getBadRatingCategoryUseCase: BulkReviewGetBadRatingCategoryUseCase

    @RelaxedMockK
    lateinit var uploaderUseCase: UploaderUseCase

    @RelaxedMockK
    lateinit var submitUseCase: BulkReviewSubmitUseCase

    @RelaxedMockK
    lateinit var gson: Gson

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var bulkWriteReviewTracker: BulkWriteReviewTracker

    protected lateinit var viewModel: BulkReviewViewModel

    protected val getFormUseCaseResultSuccess =
        createSuccessResponse<BulkReviewGetFormResponse.Data>(
            SAMPLE_GET_FORM_RESULT_SUCCESS
        ).getSuccessData<BulkReviewGetFormResponse.Data>().productRevGetBulkForm

    protected val getBadRatingCategoryUseCaseResultSuccessNonEmpty =
        createSuccessResponse<BadRatingCategoriesResponse>(
            SAMPLE_GET_BAD_RATING_CATEGORY_RESULT_SUCCESS_NON_EMPTY
        ).getSuccessData<BadRatingCategoriesResponse>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = BulkReviewViewModel(
            coroutineDispatchers = rule.dispatchers,
            getFormUseCase = getFormUseCase,
            getBadRatingCategoryUseCase = getBadRatingCategoryUseCase,
            uploaderUseCase = uploaderUseCase,
            submitUseCase = submitUseCase,
            bulkReviewProductInfoUiStateMapper = BulkReviewProductInfoUiStateMapper(),
            bulkReviewRatingUiStateMapper = BulkReviewRatingUiStateMapper(),
            bulkReviewBadRatingCategoryUiStateMapper = BulkReviewBadRatingCategoryUiStateMapper(),
            bulkReviewTextAreaUiStateMapper = BulkReviewTextAreaUiStateMapper(),
            bulkReviewMediaPickerUiStateMapper = BulkReviewMediaPickerUiStateMapper(),
            bulkReviewMiniActionsUiStateMapper = BulkReviewMiniActionsUiStateMapper(),
            bulkReviewVisitableMapper = BulkReviewVisitableMapper(),
            bulkReviewStickyButtonMapper = BulkReviewStickyButtonMapper(),
            bulkReviewPageUiStateMapper = BulkReviewPageUiStateMapper(userSession),
            bulkReviewBadRatingCategoryMapper = BulkReviewBadRatingCategoryMapper(),
            bulkReviewSubmissionParamsMapper = BulkReviewSubmissionParamsMapper(),
            gson = gson,
            userSession = userSession,
            bulkWriteReviewTracker = bulkWriteReviewTracker
        )

        every { userSession.userId } returns SAMPLE_USER_ID
        mockkStatic(::getFileFormatByMimeType)
        every {
            getFileFormatByMimeType(any(), any(), any())
        } answers {
            ((args[0] as String).endsWith("video") && (args[1] as String).endsWith(".mp4") && (args[2] as String).endsWith(".mp4")) ||
                ((args[0] as String).endsWith("image") && (args[1] as String).endsWith(".jpg") && (args[2] as String).endsWith(".jpg"))
        }
        mockkObject(ErrorHandler)
        every {
            ErrorHandler.getErrorMessagePair(any(), any(), any())
        } returns Pair(SAMPLE_ERROR_MESSAGE, SAMPLE_ERROR_CODE)
    }

    @After
    fun cleanup() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    protected fun mockSuccessGetFormResult(
        getFormResult: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm = getFormUseCaseResultSuccess
    ) {
        coEvery {
            getFormUseCase(Unit)
        } returns flow {
            emit(BulkReviewGetFormRequestState.Requesting())
            emit(BulkReviewGetFormRequestState.Complete.Success(getFormResult))
        }
    }

    protected fun mockErrorGetFormResult() {
        coEvery {
            getFormUseCase(Unit)
        } returns flow {
            emit(BulkReviewGetFormRequestState.Requesting())
            emit(BulkReviewGetFormRequestState.Complete.Error(mockk(relaxed = true)))
        }
    }

    protected fun mockSuccessBadRatingCategoryResult(
        getBadRatingCategoryResult: BadRatingCategoriesResponse = getBadRatingCategoryUseCaseResultSuccessNonEmpty
    ) {
        coEvery {
            getBadRatingCategoryUseCase(Unit)
        } returns flow {
            emit(BulkReviewGetBadRatingCategoryRequestState.Requesting())
            emit(
                BulkReviewGetBadRatingCategoryRequestState.Complete.Success(
                    getBadRatingCategoryResult
                )
            )
        }
    }

    protected fun mockErrorBadRatingCategoryResult() {
        coEvery {
            getBadRatingCategoryUseCase(Unit)
        } returns flow {
            emit(BulkReviewGetBadRatingCategoryRequestState.Requesting())
            emit(BulkReviewGetBadRatingCategoryRequestState.Complete.Error(mockk(relaxed = true)))
        }
    }

    protected fun mockSuccessUploadMedia(
        actionBeforeReturnSuccess: () -> Unit = {}
    ) {
        coEvery {
            uploaderUseCase(any())
        } answers {
            val filePath = (args[0] as RequestParams).getString("file_path", "")
            actionBeforeReturnSuccess()
            if (filePath.endsWith(".jpg")) {
                UploadResult.Success(filePath.replace(".jpg", ""))
            } else {
                UploadResult.Success(filePath.replace(".mp4", ""), "https://www.tokopedia.com/$filePath")
            }
        }
    }

    protected fun mockErrorUploadMedia(
        actionBeforeReturnError: () -> Unit = {}
    ) {
        coEvery { uploaderUseCase(any()) } answers {
            actionBeforeReturnError()
            UploadResult.Error("")
        }
    }

    protected fun mockAllSuccessSubmitBulkReview(
        actionBeforeReturnSuccess: () -> Unit = {}
    ) {
        coEvery {
            submitUseCase(any())
        } answers {
            flow {
                emit(BulkReviewSubmitRequestState.Requesting())
                actionBeforeReturnSuccess()
                emit(
                    BulkReviewSubmitRequestState.Complete.Success(
                        args[0] as List<BulkReviewSubmitRequestParam>,
                        BulkReviewSubmitResponse.Data.ProductRevBulkSubmitProductReview(
                            success = true,
                            failedInboxIDs = emptyList()
                        )
                    )
                )
            }
        }
    }

    protected fun mockPartialSuccessSubmitBulkReview(
        actionBeforeReturnSuccess: () -> Unit = {}
    ) {
        coEvery {
            submitUseCase(any())
        } answers {
            flow {
                emit(BulkReviewSubmitRequestState.Requesting())
                actionBeforeReturnSuccess()
                emit(
                    BulkReviewSubmitRequestState.Complete.Success(
                        args[0] as List<BulkReviewSubmitRequestParam>,
                        BulkReviewSubmitResponse.Data.ProductRevBulkSubmitProductReview(
                            success = false,
                            failedInboxIDs = getFormUseCaseResultSuccess.reviewForm.mapIndexedNotNull { index, reviewForm ->
                                if (index % 2 == 0) reviewForm.inboxID else null
                            }
                        )
                    )
                )
            }
        }
    }

    protected fun mockErrorSubmitBulkReview() {
        coEvery {
            submitUseCase(any())
        } answers {
            flow {
                emit(BulkReviewSubmitRequestState.Requesting())
                emit(BulkReviewSubmitRequestState.Complete.Error(mockk(relaxed = true)))
            }
        }
    }

    protected fun doSuccessGetInitialData() {
        mockSuccessGetFormResult()
        mockSuccessBadRatingCategoryResult()
        viewModel.getData()
    }

    protected fun doRestoreInstanceState(
        mockGetFormRequestState: BulkReviewGetFormRequestState? = BulkReviewGetFormRequestState.Complete.Success(getFormUseCaseResultSuccess),
        mockGetBadRatingCategoryRequestState: BulkReviewGetBadRatingCategoryRequestState? = BulkReviewGetBadRatingCategoryRequestState.Complete.Success(getBadRatingCategoryUseCaseResultSuccessNonEmpty),
        mockSubmitBulkReviewRequestState: BulkReviewSubmitRequestState? = BulkReviewSubmitRequestState.Requesting(),
        mockRemovedReviewItemsInboxID: Set<String>? = emptySet(),
        mockReviewItemsRating: List<BulkReviewItemRatingUiModel>? = arrayListOf(),
        mockReviewItemsBadRatingCategory: List<BulkReviewItemBadRatingCategoryUiModel>? = arrayListOf(),
        mockReviewItemsTestimony: List<BulkReviewItemTestimonyUiModel>? = arrayListOf(),
        mockReviewItemsMediaUris: List<BulkReviewItemMediaUrisUiModel>? = arrayListOf(),
        mockReviewItemsMediaUploadResults: List<BulkReviewItemMediaUploadResultsUiModel>? = arrayListOf(),
        mockReviewItemsMediaUploadBatchNumber: List<BulkReviewItemMediaUploadBatchNumberUiModel>? = arrayListOf(),
        mockAnonymous: Boolean? = false,
        mockShouldSubmitReview: Boolean? = false,
        mockActiveMediaPickerInboxID: String? = "",
        assertionBlock: (BulkReviewViewModel) -> Unit = {}
    ) {
        mockkStatic("com.tokopedia.reviewcommon.extension.CacheManagerExtKt") {
            val saveInstanceCacheManager = mockk<SaveInstanceCacheManager>(relaxed = true)
            val spykViewModel = spyk(viewModel)

            every {
                saveInstanceCacheManager.get<BulkReviewGetFormRequestState>(
                    customId = "getFormRequestState",
                    type = BulkReviewGetFormRequestState::class.java,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockGetFormRequestState

            every {
                saveInstanceCacheManager.get<BulkReviewGetBadRatingCategoryRequestState>(
                    customId = "getBadRatingCategoryRequestState",
                    type = BulkReviewGetBadRatingCategoryRequestState::class.java,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockGetBadRatingCategoryRequestState

            every {
                saveInstanceCacheManager.get<BulkReviewSubmitRequestState>(
                    customId = "submitBulkReviewRequestState",
                    type = BulkReviewSubmitRequestState::class.java,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockSubmitBulkReviewRequestState

            every {
                saveInstanceCacheManager.get<Set<String>>(
                    customId = "removedReviewItemsInboxID",
                    type = object : TypeToken<HashSet<String>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockRemovedReviewItemsInboxID

            every {
                saveInstanceCacheManager.get<List<BulkReviewItemRatingUiModel>>(
                    customId = "reviewItemsRating",
                    type = object : TypeToken<ArrayList<BulkReviewItemRatingUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockReviewItemsRating

            every {
                saveInstanceCacheManager.get<List<BulkReviewItemBadRatingCategoryUiModel>>(
                    customId = "reviewItemsBadRatingCategory",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemBadRatingCategoryUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockReviewItemsBadRatingCategory

            every {
                saveInstanceCacheManager.get<List<BulkReviewItemTestimonyUiModel>>(
                    customId = "reviewItemsTestimony",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemTestimonyUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockReviewItemsTestimony

            every {
                saveInstanceCacheManager.get<List<BulkReviewItemMediaUrisUiModel>>(
                    customId = "reviewItemsMediaUris",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemMediaUrisUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockReviewItemsMediaUris

            every {
                saveInstanceCacheManager.get<List<BulkReviewItemMediaUploadResultsUiModel>>(
                    customId = "reviewItemsMediaUploadResults",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemMediaUploadResultsUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockReviewItemsMediaUploadResults

            every {
                saveInstanceCacheManager.get<List<BulkReviewItemMediaUploadBatchNumberUiModel>>(
                    customId = "reviewItemsMediaUploadBatchNumber",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemMediaUploadBatchNumberUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockReviewItemsMediaUploadBatchNumber

            every {
                saveInstanceCacheManager.get<Boolean>(
                    customId = "anonymous",
                    type = Boolean::class.java,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockAnonymous

            every {
                saveInstanceCacheManager.get<Boolean>(
                    customId = "shouldSubmitReview",
                    type = Boolean::class.java,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockShouldSubmitReview

            every {
                saveInstanceCacheManager.get<String>(
                    customId = "activeMediaPickerInboxID",
                    type = String::class.java,
                    defaultValue = null,
                    gson = gson
                )
            } returns mockActiveMediaPickerInboxID

            spykViewModel.onRestoreInstanceState(saveInstanceCacheManager)

            verify(exactly = 1) {
                saveInstanceCacheManager.get<BulkReviewGetFormRequestState>(
                    customId = "getFormRequestState",
                    type = BulkReviewGetFormRequestState::class.java,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<BulkReviewGetBadRatingCategoryRequestState>(
                    customId = "getBadRatingCategoryRequestState",
                    type = BulkReviewGetBadRatingCategoryRequestState::class.java,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<BulkReviewSubmitRequestState>(
                    customId = "submitBulkReviewRequestState",
                    type = BulkReviewSubmitRequestState::class.java,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<Set<String>>(
                    customId = "removedReviewItemsInboxID",
                    type = object : TypeToken<HashSet<String>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<List<BulkReviewItemRatingUiModel>>(
                    customId = "reviewItemsRating",
                    type = object : TypeToken<ArrayList<BulkReviewItemRatingUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<List<BulkReviewItemBadRatingCategoryUiModel>>(
                    customId = "reviewItemsBadRatingCategory",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemBadRatingCategoryUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<List<BulkReviewItemTestimonyUiModel>>(
                    customId = "reviewItemsTestimony",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemTestimonyUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<List<BulkReviewItemMediaUrisUiModel>>(
                    customId = "reviewItemsMediaUris",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemMediaUrisUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<List<BulkReviewItemMediaUploadResultsUiModel>>(
                    customId = "reviewItemsMediaUploadResults",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemMediaUploadResultsUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<List<BulkReviewItemMediaUploadBatchNumberUiModel>>(
                    customId = "reviewItemsMediaUploadBatchNumber",
                    type = object :
                        TypeToken<ArrayList<BulkReviewItemMediaUploadBatchNumberUiModel>>() {}.type,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<Boolean>(
                    customId = "anonymous",
                    type = Boolean::class.java,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<Boolean>(
                    customId = "shouldSubmitReview",
                    type = Boolean::class.java,
                    defaultValue = null,
                    gson = gson
                )
                saveInstanceCacheManager.get<String>(
                    customId = "activeMediaPickerInboxID",
                    type = String::class.java,
                    defaultValue = null,
                    gson = gson
                )
            }

            assertionBlock(spykViewModel)
        }
    }

    protected fun getFirstReviewItem(): BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm {
        return getFormUseCaseResultSuccess.reviewForm.first()
    }

    protected fun getBadRatingCategory(index: Int = Int.ZERO): BadRatingCategory {
        return getBadRatingCategoryUseCaseResultSuccessNonEmpty
            .productrevGetBadRatingCategory
            .list[index]
    }

    protected fun getOtherBadRatingCategory(): BadRatingCategory {
        return getBadRatingCategoryUseCaseResultSuccessNonEmpty
            .productrevGetBadRatingCategory
            .list
            .first { it.id == "6" }
    }

    protected fun runCollectingBulkReviewPageUiState(block: (List<BulkReviewPageUiState>) -> Unit) {
        val uiStates = mutableListOf<BulkReviewPageUiState>()
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiStateCollectorJob = scope.launch {
            viewModel.bulkReviewPageUiState.toList(uiStates)
        }
        block(uiStates)
        uiStateCollectorJob.cancel()
    }

    protected fun runCollectingBulkReviewRemoveReviewItemDialogUiState(block: (List<BulkReviewRemoveReviewItemDialogUiState>) -> Unit) {
        val uiStates = mutableListOf<BulkReviewRemoveReviewItemDialogUiState>()
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiStateCollectorJob = scope.launch {
            viewModel.removeReviewItemDialogUiState.toList(uiStates)
        }
        block(uiStates)
        uiStateCollectorJob.cancel()
    }

    protected fun runCollectingBulkReviewBadRatingCategoryBottomSheetUiState(block: (List<BulkReviewBadRatingCategoryBottomSheetUiState>) -> Unit) {
        val uiStates = mutableListOf<BulkReviewBadRatingCategoryBottomSheetUiState>()
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiStateCollectorJob = scope.launch {
            viewModel.badRatingCategoryBottomSheetUiState.toList(uiStates)
        }
        block(uiStates)
        uiStateCollectorJob.cancel()
    }

    protected fun runCollectingBulkReviewExpandedTextAreaBottomSheetUiState(block: (List<BulkReviewExpandedTextAreaBottomSheetUiState>) -> Unit) {
        val uiStates = mutableListOf<BulkReviewExpandedTextAreaBottomSheetUiState>()
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiStateCollectorJob = scope.launch {
            viewModel.expandedTextAreaBottomSheetUiState.toList(uiStates)
        }
        block(uiStates)
        uiStateCollectorJob.cancel()
    }

    protected fun runCollectingBulkReviewCancelReviewSubmissionDialogUiState(block: (List<BulkReviewCancelReviewSubmissionDialogUiState>) -> Unit) {
        val uiStates = mutableListOf<BulkReviewCancelReviewSubmissionDialogUiState>()
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiStateCollectorJob = scope.launch {
            viewModel.cancelReviewSubmissionDialogUiState.toList(uiStates)
        }
        block(uiStates)
        uiStateCollectorJob.cancel()
    }

    protected fun runCollectingBulkReviewPageToasterQueue(block: (List<CreateReviewToasterUiModel<Any>>) -> Unit) {
        val toasterQueue = mutableListOf<CreateReviewToasterUiModel<Any>>()
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiStateCollectorJob = scope.launch {
            viewModel.bulkReviewPageToasterQueue.toList(toasterQueue)
        }
        block(toasterQueue)
        uiStateCollectorJob.cancel()
    }

    protected fun runCollectingExpandedTextAreaToasterQueue(block: (List<CreateReviewToasterUiModel<Any>>) -> Unit) {
        val toasterQueue = mutableListOf<CreateReviewToasterUiModel<Any>>()
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiStateCollectorJob = scope.launch {
            viewModel.expandedTextAreaToasterQueue.toList(toasterQueue)
        }
        block(toasterQueue)
        uiStateCollectorJob.cancel()
    }
}
