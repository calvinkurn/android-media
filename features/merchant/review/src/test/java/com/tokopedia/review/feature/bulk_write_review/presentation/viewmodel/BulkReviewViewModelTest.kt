package com.tokopedia.review.feature.bulk_write_review.presentation.viewmodel

import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryBottomSheetUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewCancelReviewSubmissionDialogUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewExpandedTextAreaBottomSheetUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewPageUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRemoveReviewItemDialogUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.util.ResourceProvider
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewToasterUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.Toaster
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BulkReviewViewModelTest : BulkReviewViewModelTestFixture() {

    @Test
    fun `initial bulk review page ui state should be Loading`() = runCollectingBulkReviewPageUiState { uiStates ->
        assertTrue(uiStates.last() is BulkReviewPageUiState.Loading)
    }

    @Test
    fun `bulk review page ui state should be Showing when getFormUseCase and getBadRatingCategoryUseCase is success`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            doSuccessGetInitialData()
            assertTrue(uiStates.last() is BulkReviewPageUiState.Showing)
        }

    @Test
    fun `bulk review page ui state should be Error when getFormUseCase is error and getBadRatingCategoryUseCase is success`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            mockErrorGetFormResult()
            mockSuccessBadRatingCategoryResult()
            viewModel.getData()
            assertTrue(uiStates.last() is BulkReviewPageUiState.Error)
        }

    @Test
    fun `bulk review page ui state should be Error when getFormUseCase is success and getBadRatingCategoryUseCase is error`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            mockSuccessGetFormResult()
            mockErrorBadRatingCategoryResult()
            viewModel.getData()
            assertTrue(uiStates.last() is BulkReviewPageUiState.Error)
        }

    @Test
    fun `getData should execute getFormUseCase once`() = runCollectingBulkReviewPageUiState {
        doSuccessGetInitialData()
        coVerify(exactly = 1) { getFormUseCase(Unit) }
    }

    @Test
    fun `getData should execute getBadRatingCategoryUseCase once`() =
        runCollectingBulkReviewPageUiState {
            doSuccessGetInitialData()
            coVerify(exactly = 1) { getBadRatingCategoryUseCase(Unit) }
        }

    @Test
    fun `onRemoveReviewItem should execute BulkWriteReviewTracker#trackReviewItemRemoveButtonClick once`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            viewModel.onRemoveReviewItem(reviewItem.inboxID)

            verify(exactly = 1) {
                bulkWriteReviewTracker.trackReviewItemRemoveButtonClick(
                    position = Int.ZERO,
                    orderId = reviewItem.orderID,
                    reputationId = reviewItem.reputationID,
                    productId = reviewItem.product.productID,
                    userId = SAMPLE_USER_ID
                )
            }
        }

    @Test
    fun `onRemoveReviewItem should remove review item when haveMinimumReviewItem and hasDefaultState return true`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            // load data and verify that there's no removed review item before removal
            doSuccessGetInitialData()
            assertTrue(
                uiStates.last().let { uiState ->
                    uiState is BulkReviewPageUiState.Showing && uiState.items.filterIsInstance<BulkReviewItemUiModel>().size == getFormUseCaseResultSuccess.reviewForm.size
                }
            )

            // remove review item and verify that the review item is removed
            viewModel.onRemoveReviewItem(reviewItem.inboxID)
            assertTrue(
                uiStates.last().let { uiState ->
                    uiState is BulkReviewPageUiState.Showing && uiState.items.filterIsInstance<BulkReviewItemUiModel>().size == getFormUseCaseResultSuccess.reviewForm.size.dec()
                }
            )
        }

    @Test
    fun `onRemoveReviewItem should enqueue toaster when haveMinimumReviewItem and hasDefaultState return true`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewPageToasterQueue { toasterQueue ->
                val reviewItem = getFirstReviewItem()
                val expectedToaster = CreateReviewToasterUiModel(
                    message = ResourceProvider.getMessageReviewItemRemoved(),
                    actionText = ResourceProvider.getCtaCancel(),
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_NORMAL,
                    id = 0,
                    payload = reviewItem.inboxID
                )

                // load data and verify that there's no enqueued toaster
                doSuccessGetInitialData()
                assertTrue(toasterQueue.isEmpty())

                // remove review item and verify the expected toaster is enqueued
                viewModel.onRemoveReviewItem(reviewItem.inboxID)
                assertEquals(expectedToaster, toasterQueue.last())
            }
        }

    @Test
    fun `onRemoveReviewItem should show confirmation dialog when haveMinimumReviewItem return true and hasDefaultState return false`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewRemoveReviewItemDialogUiState { dialogUiStates ->
                val reviewItem = getFirstReviewItem()
                val expectedDialogUiState = BulkReviewRemoveReviewItemDialogUiState.Showing(
                    reviewItem.inboxID
                )

                // load data and verify that the dialog is initially dismissed
                doSuccessGetInitialData()
                assertTrue(dialogUiStates.last() is BulkReviewRemoveReviewItemDialogUiState.Dismissed)

                // change review rating to make hasDefaultState return false, remove review item and
                // verify the expected dialog ui state
                viewModel.onRatingChanged(reviewItem.inboxID, 4)
                viewModel.onRemoveReviewItem(reviewItem.inboxID)
                assertEquals(expectedDialogUiState, dialogUiStates.last())
            }
        }

    @Test
    fun `onRemoveReviewItem should enqueue toaster when haveMinimumReviewItem return false`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewPageToasterQueue { toasterQueue ->
                val expectedToaster = CreateReviewToasterUiModel(
                    message = ResourceProvider.getMessageCannotRemoveMoreReviewItem(1),
                    actionText = ResourceProvider.getCtaOke(),
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_ERROR,
                    payload = Unit
                )

                // load data and verify that there's no enqueued toaster
                doSuccessGetInitialData()
                assertTrue(toasterQueue.isEmpty())

                // remove review items one by one until reach minimum review item and verify the
                // expected toaster is enqueued
                getFormUseCaseResultSuccess.reviewForm.forEach { reviewForm ->
                    viewModel.onRemoveReviewItem(reviewForm.inboxID)
                }
                assertEquals(expectedToaster, toasterQueue.last())
            }
        }

    @Test
    fun `onRemoveReviewItem will not remove any review if provided inbox id is invalid`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            doSuccessGetInitialData()
            // verify that there's no removed review item before removal
            assertTrue(
                uiStates.last().let { uiState ->
                    uiState is BulkReviewPageUiState.Showing && uiState.items.filterIsInstance<BulkReviewItemUiModel>().size == getFormUseCaseResultSuccess.reviewForm.size
                }
            )

            // remove review item and verify that the review item is removed
            viewModel.onRemoveReviewItem("")
            assertTrue(
                uiStates.last().let { uiState ->
                    uiState is BulkReviewPageUiState.Showing && uiState.items.filterIsInstance<BulkReviewItemUiModel>().size == getFormUseCaseResultSuccess.reviewForm.size
                }
            )
        }

    @Test
    fun `onRatingChanged should execute BulkWriteReviewTracker#trackReviewItemRatingChanged once`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()

            viewModel.onRatingChanged(reviewItem.inboxID, 4)

            verify(exactly = 1) {
                bulkWriteReviewTracker.trackReviewItemRatingChanged(
                    position = Int.ZERO,
                    orderId = reviewItem.orderID,
                    reputationId = reviewItem.reputationID,
                    productId = reviewItem.product.productID,
                    userId = SAMPLE_USER_ID,
                    rating = 4,
                    timestamp = any()
                )
            }
        }

    @Test
    fun `onRatingChanged should not execute BulkWriteReviewTracker#trackReviewItemRatingChanged when provided inbox id is invalid`() =
        runCollectingBulkReviewPageUiState {
            doSuccessGetInitialData()

            viewModel.onRatingChanged("", 4)

            verify(inverse = true) {
                bulkWriteReviewTracker.trackReviewItemRatingChanged(
                    position = any(),
                    orderId = any(),
                    reputationId = any(),
                    productId = any(),
                    userId = any(),
                    rating = any(),
                    timestamp = any()
                )
            }
        }

    @Test
    fun `onRatingChanged should change review item rating`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()
            val initialRating = 5
            val newRating = 4

            // load data and verify that review item initially have default rating which is 5
            doSuccessGetInitialData()
            assertEquals(
                initialRating,
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .getReviewItemRating()
            )

            // change review item rating and verify that review item rating has changed
            viewModel.onRatingChanged(reviewItem.inboxID, newRating)
            assertEquals(
                newRating,
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .getReviewItemRating()
            )
        }

    @Test
    fun `onRatingChanged should not change any review item rating when provided inbox id is invalid`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            // load data and verify that all review item initially have default rating which is 5
            doSuccessGetInitialData()
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .all { it.getReviewItemRating() == 5 }
            )

            // change review item rating and verify that all review item rating still 5
            viewModel.onRatingChanged("", 4)
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .all { it.getReviewItemRating() == 5 }
            )
        }

    @Test
    fun `onRatingChanged should show bad rating category bottom sheet when change rating to bad rating from good rating`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState { uiStates ->
                val reviewItem = getFirstReviewItem()
                val newRating = 2

                // load data and verify that bad rating category bottom sheet is initially dismissed
                doSuccessGetInitialData()
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)

                // change rating to 2 and verify that bad rating category bottom sheet is showing
                viewModel.onRatingChanged(reviewItem.inboxID, newRating)
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Showing)
            }
        }

    @Test
    fun `onRatingChanged should not show bad rating category bottom sheet when change rating to bad rating from good rating on invalid review item`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState { uiStates ->
                val newRating = 2

                // load data and verify that bad rating category bottom sheet is initially dismissed
                doSuccessGetInitialData()
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)

                // change rating to 2 and verify that bad rating category bottom sheet is still dismissed
                viewModel.onRatingChanged("", newRating)
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)
            }
        }

    @Test
    fun `onRatingChanged should not bad rating category bottom sheet when change rating to bad rating from bad rating`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState { uiStates ->
                val reviewItem = getFirstReviewItem()
                val badRatingCategory = getFirstBadRatingCategory()
                val initialRating = 2
                val newRating = 1

                // load data and verify that bad rating category bottom sheet is initially dismissed
                doSuccessGetInitialData()
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)

                // simulate bad rating category selection and apply
                viewModel.onRatingChanged(reviewItem.inboxID, initialRating)
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Showing)
                viewModel.onBadRatingCategorySelectionChanged(
                    position = Int.ZERO,
                    badRatingCategoryID = badRatingCategory.id,
                    reason = badRatingCategory.description,
                    selected = true
                )
                viewModel.onApplyBadRatingCategory()
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)

                // change review item rating to other bad rating and verify that bad rating category
                // bottom sheet is still dismissed
                viewModel.onRatingChanged(reviewItem.inboxID, newRating)
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)
            }
        }

    @Test
    fun `onRatingChanged should hide bad rating category widget when changing rating to good rating`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()
            val badRatingCategory = getFirstBadRatingCategory()
            val initialRating = 2
            val newRating = 5

            // load data and verify that bad rating category widget is initially hidden
            doSuccessGetInitialData()
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .badRatingCategoriesUiState is BulkReviewBadRatingCategoryUiState.Hidden
            )

            // simulate bad rating category selection and apply
            viewModel.onRatingChanged(reviewItem.inboxID, initialRating)
            viewModel.onBadRatingCategorySelectionChanged(
                position = Int.ZERO,
                badRatingCategoryID = badRatingCategory.id,
                reason = badRatingCategory.description,
                selected = true
            )
            viewModel.onApplyBadRatingCategory()
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .badRatingCategoriesUiState is BulkReviewBadRatingCategoryUiState.Showing
            )

            // change review item rating to good rating and verify that bad rating category widget is hidden
            viewModel.onRatingChanged(reviewItem.inboxID, newRating)
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .badRatingCategoriesUiState is BulkReviewBadRatingCategoryUiState.Hidden
            )
        }

    @Test
    fun `onRatingSet should update rating animate value to false`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            // load data and verify that rating animate value is initially true
            doSuccessGetInitialData()
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .ratingUiState.let { ratingUiState ->
                        (ratingUiState as BulkReviewRatingUiState.Showing).animate
                    }
            )

            // call onRatingSet and verify that rating animate value is false
            viewModel.onRatingSet(reviewItem.inboxID)
            assertFalse(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .ratingUiState.let { ratingUiState ->
                        (ratingUiState as BulkReviewRatingUiState.Showing).animate
                    }
            )

            // call onRatingSet again and verify that rating animate value is still false
            viewModel.onRatingSet(reviewItem.inboxID)
            assertFalse(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .ratingUiState.let { ratingUiState ->
                        (ratingUiState as BulkReviewRatingUiState.Showing).animate
                    }
            )
        }

    @Test
    fun `onDismissExpandedTextAreaBottomSheet should apply testimony value`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            runCollectingBulkReviewExpandedTextAreaBottomSheetUiState {
                val reviewItem = getFirstReviewItem()
                val initialTestimony = ""
                val newTestimony = "Barangnya bagus harga terjangkau, mantap!"

                // load data then show text area and verify that testimony is initially empty
                doSuccessGetInitialData()
                viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
                assertEquals(
                    initialTestimony,
                    (uiStates.last() as BulkReviewPageUiState.Showing)
                        .items
                        .filterIsInstance<BulkReviewItemUiModel>()
                        .first { it.inboxID == reviewItem.inboxID }
                        .uiState
                        .textAreaUiState.let { textAreaUiState ->
                            (textAreaUiState as BulkReviewTextAreaUiState.Showing).text
                        }
                )

                // simulate click on expand text area then type testimony and then apply it by dismissing
                // the bottom sheet
                viewModel.onExpandTextArea(reviewItem.inboxID, initialTestimony)
                viewModel.onDismissExpandedTextAreaBottomSheet(newTestimony)

                // verify that new testimony is applied
                assertEquals(
                    newTestimony,
                    (uiStates.last() as BulkReviewPageUiState.Showing)
                        .items
                        .filterIsInstance<BulkReviewItemUiModel>()
                        .first { it.inboxID == reviewItem.inboxID }
                        .uiState
                        .textAreaUiState.let { textAreaUiState ->
                            (textAreaUiState as BulkReviewTextAreaUiState.Showing).text
                        }
                )
            }
        }

    @Test
    fun `onDismissExpandedTextAreaBottomSheet should dismiss bottom sheet`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewExpandedTextAreaBottomSheetUiState { uiStates ->
                val reviewItem = getFirstReviewItem()

                // load data then show text area and verify that bottom sheet is showing
                doSuccessGetInitialData()
                viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
                viewModel.onExpandTextArea(reviewItem.inboxID, "")
                assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Showing)

                // dismiss bottom sheet and verify that bottom sheet is dismissed
                viewModel.onDismissExpandedTextAreaBottomSheet("")
                assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)
            }
        }

    @Test
    fun `onDismissExpandedTextAreaBottomSheet should enqueue expected toaster when expanded text area is empty`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState {
                runCollectingBulkReviewExpandedTextAreaBottomSheetUiState {
                    runCollectingExpandedTextAreaToasterQueue { toasterQueue ->
                        val reviewItem = getFirstReviewItem()
                        val badRatingCategory = getOtherBadRatingCategory()
                        val expectedToaster = CreateReviewToasterUiModel(
                            message = ResourceProvider.getMessageBadRatingReasonCannotEmpty(),
                            actionText = ResourceProvider.getCtaOke(),
                            duration = Toaster.LENGTH_SHORT,
                            type = Toaster.TYPE_ERROR,
                            payload = Unit
                        )

                        doSuccessGetInitialData()
                        // change rating to bad rating to show the bad rating category bottom sheet
                        viewModel.onRatingChanged(reviewItem.inboxID, 2)
                        // select Lainnya reason to show the expanded text area bottom sheet
                        viewModel.onBadRatingCategorySelectionChanged(
                            position = Int.ZERO,
                            badRatingCategoryID = badRatingCategory.id,
                            reason = badRatingCategory.description,
                            selected = true
                        )

                        // dismiss the text area bottom sheet with empty text (no text typed on the bottom sheet)
                        viewModel.onDismissExpandedTextAreaBottomSheet("")
                        assertEquals(expectedToaster, toasterQueue.last())
                    }
                }
            }
        }

    @Test
    fun `onBadRatingCategorySelectionChanged should execute BulkWriteReviewTracker#trackReviewItemBadRatingCategorySelected once when selected is true`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()
            val badRatingCategory = getFirstBadRatingCategory()

            doSuccessGetInitialData()

            // change rating to bad rating to show the bad rating category bottom sheet
            viewModel.onRatingChanged(reviewItem.inboxID, 2)
            viewModel.onBadRatingCategorySelectionChanged(
                position = Int.ZERO,
                badRatingCategoryID = badRatingCategory.id,
                reason = badRatingCategory.description,
                selected = true
            )

            verify(exactly = 1) {
                bulkWriteReviewTracker.trackReviewItemBadRatingCategorySelected(
                    position = Int.ZERO,
                    orderId = reviewItem.orderID,
                    reputationId = reviewItem.reputationID,
                    productId = reviewItem.product.productID,
                    userId = SAMPLE_USER_ID,
                    rating = 2,
                    reason = badRatingCategory.description
                )
            }
        }

    @Test
    fun `onBadRatingCategorySelectionChanged should not execute BulkWriteReviewTracker#trackReviewItemBadRatingCategorySelected when selected is false`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()
            val badRatingCategory = getFirstBadRatingCategory()

            doSuccessGetInitialData()

            // change rating to bad rating to show the bad rating category bottom sheet
            viewModel.onRatingChanged(reviewItem.inboxID, 2)
            viewModel.onBadRatingCategorySelectionChanged(
                position = Int.ZERO,
                badRatingCategoryID = badRatingCategory.id,
                reason = badRatingCategory.description,
                selected = false
            )

            verify(inverse = true) {
                bulkWriteReviewTracker.trackReviewItemBadRatingCategorySelected(
                    position = Int.ZERO,
                    orderId = reviewItem.orderID,
                    reputationId = reviewItem.reputationID,
                    productId = reviewItem.product.productID,
                    userId = SAMPLE_USER_ID,
                    rating = 2,
                    reason = badRatingCategory.description
                )
            }
        }

    @Test
    fun `onBadRatingCategorySelectionChanged should change bad rating category selected status`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState { uiStates ->
                val reviewItem = getFirstReviewItem()
                val badRatingCategory = getFirstBadRatingCategory()

                doSuccessGetInitialData()

                // change rating to bad rating to show the bad rating category bottom sheet
                viewModel.onRatingChanged(reviewItem.inboxID, 2)

                // verify that initially no bad rating category is selected
                assertTrue(
                    (uiStates.last() as BulkReviewBadRatingCategoryBottomSheetUiState.Showing)
                        .badRatingCategories
                        .none { it.selected }
                )

                // select bad rating category
                viewModel.onBadRatingCategorySelectionChanged(
                    position = Int.ZERO,
                    badRatingCategoryID = badRatingCategory.id,
                    reason = badRatingCategory.description,
                    selected = true
                )

                // verify that first bad rating category is selected
                assertTrue(
                    (uiStates.last() as BulkReviewBadRatingCategoryBottomSheetUiState.Showing)
                        .badRatingCategories
                        .first()
                        .selected
                )

                // unselect bad rating category
                viewModel.onBadRatingCategorySelectionChanged(
                    position = Int.ZERO,
                    badRatingCategoryID = badRatingCategory.id,
                    reason = badRatingCategory.description,
                    selected = false
                )

                // verify that first bad rating category is unselected
                assertFalse(
                    (uiStates.last() as BulkReviewBadRatingCategoryBottomSheetUiState.Showing)
                        .badRatingCategories
                        .first()
                        .selected
                )
            }
        }

    @Test
    fun `onBadRatingCategorySelectionChanged should show expanded text area bottom sheet when selecting Lainnya bad rating category`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState {
                runCollectingBulkReviewExpandedTextAreaBottomSheetUiState { uiStates ->
                    val reviewItem = getFirstReviewItem()
                    val badRatingCategory = getOtherBadRatingCategory()

                    doSuccessGetInitialData()

                    // change rating to bad rating to show the bad rating category bottom sheet
                    viewModel.onRatingChanged(reviewItem.inboxID, 2)

                    // verify that initially text area bottom sheet is dismissed
                    assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)

                    // select bad rating category and apply to dismiss bad rating category bottom sheet
                    viewModel.onBadRatingCategorySelectionChanged(
                        position = Int.ZERO,
                        badRatingCategoryID = badRatingCategory.id,
                        reason = badRatingCategory.description,
                        selected = true
                    )

                    // verify that text area bottom sheet is showing
                    assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Showing)
                }
            }
        }

    @Test
    fun `onBadRatingCategorySelectionChanged should not show expanded text area bottom sheet when selecting other than Lainnya bad rating category`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState {
                runCollectingBulkReviewExpandedTextAreaBottomSheetUiState { uiStates ->
                    val reviewItem = getFirstReviewItem()
                    val badRatingCategory = getFirstBadRatingCategory()

                    doSuccessGetInitialData()

                    viewModel.onRatingChanged(reviewItem.inboxID, 2)

                    // verify that initially text area bottom sheet is dismissed
                    assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)

                    // select bad rating category and apply to dismiss bad rating category bottom sheet
                    viewModel.onBadRatingCategorySelectionChanged(
                        position = Int.ZERO,
                        badRatingCategoryID = badRatingCategory.id,
                        reason = badRatingCategory.description,
                        selected = true
                    )

                    // verify that text area bottom sheet is still dismissed
                    assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)
                }
            }
        }

    @Test
    fun `onBadRatingCategorySelectionChanged should not show expanded text area bottom sheet when deselecting Lainnya bad rating category`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState {
                runCollectingBulkReviewExpandedTextAreaBottomSheetUiState { uiStates ->
                    val reviewItem = getFirstReviewItem()
                    val badRatingCategory = getOtherBadRatingCategory()

                    doSuccessGetInitialData()

                    // select Lainnya bad rating category and dismiss the expanded text area bottom sheet
                    viewModel.onRatingChanged(reviewItem.inboxID, 2)
                    viewModel.onBadRatingCategorySelectionChanged(
                        position = Int.ZERO,
                        badRatingCategoryID = badRatingCategory.id,
                        reason = badRatingCategory.description,
                        selected = true
                    )
                    assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Showing)
                    viewModel.onDismissExpandedTextAreaBottomSheet("Dummy")
                    assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)

                    // unselect Lainnya bad rating category and verify that the expanded text area bottom sheet is not showing
                    viewModel.onChangeBadRatingCategory(reviewItem.inboxID)
                    viewModel.onBadRatingCategorySelectionChanged(
                        position = Int.ZERO,
                        badRatingCategoryID = badRatingCategory.id,
                        reason = badRatingCategory.description,
                        selected = false
                    )
                    assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)
                }
            }
        }

    @Test
    fun `onApplyBadRatingCategory should apply bad rating category to the review item`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()
            val badRatingCategory = getFirstBadRatingCategory()

            doSuccessGetInitialData()
            // change rating to bad rating to show the bad rating category bottom sheet
            viewModel.onRatingChanged(reviewItem.inboxID, 2)
            viewModel.onBadRatingCategorySelectionChanged(
                position = Int.ZERO,
                badRatingCategoryID = badRatingCategory.id,
                reason = badRatingCategory.description,
                selected = true
            )

            viewModel.onApplyBadRatingCategory()
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .badRatingCategoriesUiState.let { badRatingCategoriesUiState ->
                        (badRatingCategoriesUiState as BulkReviewBadRatingCategoryUiState.Showing)
                            .badRatingCategory
                            .all { (it.id == badRatingCategory.id && it.selected) || (it.id != badRatingCategory.id && !it.selected) }
                    }
            )
        }

    @Test
    fun `onApplyBadRatingCategory should dismiss bad rating category bottom sheet`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState { uiStates ->
                val reviewItem = getFirstReviewItem()
                val badRatingCategory = getFirstBadRatingCategory()

                doSuccessGetInitialData()
                // change rating to bad rating to show the bad rating category bottom sheet
                viewModel.onRatingChanged(reviewItem.inboxID, 2)
                viewModel.onBadRatingCategorySelectionChanged(
                    position = Int.ZERO,
                    badRatingCategoryID = badRatingCategory.id,
                    reason = badRatingCategory.description,
                    selected = true
                )
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Showing)

                viewModel.onApplyBadRatingCategory()
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)
            }
        }

    @Test
    fun `onChangeBadRatingCategory should show bad rating category bottom sheet`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState { uiStates ->
                val reviewItem = getFirstReviewItem()
                val badRatingCategory = getFirstBadRatingCategory()

                doSuccessGetInitialData()
                // change rating to bad rating to show the bad rating category bottom sheet
                viewModel.onRatingChanged(reviewItem.inboxID, 2)
                viewModel.onBadRatingCategorySelectionChanged(
                    position = Int.ZERO,
                    badRatingCategoryID = badRatingCategory.id,
                    reason = badRatingCategory.description,
                    selected = true
                )
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Showing)

                viewModel.onApplyBadRatingCategory()
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed)

                viewModel.onChangeBadRatingCategory(reviewItem.inboxID)
                assertTrue(uiStates.last() is BulkReviewBadRatingCategoryBottomSheetUiState.Showing)
            }
        }

    @Test
    fun `onReviewItemTextAreaGainFocus should change review item text area focused state to true`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            // show the text area section on the review item
            viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
            // simulate type "Dummy" and click outside text area to lost focus of the text area
            viewModel.onReviewItemTextAreaLostFocus(reviewItem.inboxID, "Dummy")
            assertFalse(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        (textAreaUiState as BulkReviewTextAreaUiState.Showing).focused
                    }
            )

            viewModel.onReviewItemTextAreaGainFocus(reviewItem.inboxID)
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        (textAreaUiState as BulkReviewTextAreaUiState.Showing).focused
                    }
            )
        }

    @Test
    fun `onReviewItemTextAreaGainFocus should not change any review item text area focused state to true when provided inbox id`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            // show the text area section on the review item
            viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
            // simulate type "Dummy" and click outside text area to lost focus of the text area
            viewModel.onReviewItemTextAreaLostFocus(reviewItem.inboxID, "Dummy")
            assertFalse(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        (textAreaUiState as BulkReviewTextAreaUiState.Showing).focused
                    }
            )

            viewModel.onReviewItemTextAreaGainFocus("")
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .none {
                        it.uiState.textAreaUiState.let { textAreaUiState ->
                            textAreaUiState is BulkReviewTextAreaUiState.Showing && textAreaUiState.focused
                        }
                    }
            )
        }

    @Test
    fun `onReviewItemTextAreaLostFocus should change review item text area focused state to false`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            // show the text area section on the review item
            viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        (textAreaUiState as BulkReviewTextAreaUiState.Showing).focused
                    }
            )

            viewModel.onReviewItemTextAreaLostFocus(reviewItem.inboxID, "Dummy")
            assertFalse(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        (textAreaUiState as BulkReviewTextAreaUiState.Showing).focused
                    }
            )
        }

    @Test
    fun `onReviewItemTextAreaLostFocus should cause review item text area widget to be hidden when text is blank`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            // region simulate click add testimony mini action to show the text area widget and type "Dummy" then click outside text area to lost the focus
            viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
            viewModel.onReviewItemTextAreaLostFocus(reviewItem.inboxID, "Dummy")
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        textAreaUiState is BulkReviewTextAreaUiState.Showing
                    }
            )
            // endregion simulate click add testimony mini action to show the text area widget and type "Dummy" then click outside text area to lost the focus

            // region simulate click on the filled text area to gain focus then replace the text with "   " then click outside text area to lost the focus
            viewModel.onReviewItemTextAreaGainFocus(reviewItem.inboxID)
            viewModel.onReviewItemTextAreaLostFocus(reviewItem.inboxID, "   ")
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        textAreaUiState is BulkReviewTextAreaUiState.Hidden
                    }
            )
            // endregion simulate click on the filled text area to gain focus then replace the text with "   " then click outside text area to lost the focus
        }

    @Test
    fun `onConfirmRemoveReviewItem should execute BulkWriteReviewTracker#trackRemoveReviewItemDialogRemoveButtonClick once`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewRemoveReviewItemDialogUiState {
                val reviewItem = getFirstReviewItem()

                doSuccessGetInitialData()
                // change rating to 4 so that remove review item confirmation dialog will be showed when calling onRemoveReviewItem
                viewModel.onRatingChanged(reviewItem.inboxID, 4)
                viewModel.onRemoveReviewItem(reviewItem.inboxID)
                viewModel.onConfirmRemoveReviewItem(
                    reviewItem.inboxID,
                    "Dummy Title",
                    "Dummy Subtitle"
                )

                verify(exactly = 1) {
                    bulkWriteReviewTracker.trackRemoveReviewItemDialogRemoveButtonClick(
                        userId = SAMPLE_USER_ID,
                        title = any(),
                        subtitle = any()
                    )
                }
            }
        }

    @Test
    fun `onConfirmRemoveReviewItem should remove review item`() =
        runCollectingBulkReviewPageUiState { bulkReviewPageUiStates ->
            runCollectingBulkReviewRemoveReviewItemDialogUiState {
                val reviewItem = getFirstReviewItem()

                doSuccessGetInitialData()
                // make sure that initially there's no removed review item
                assertTrue(
                    bulkReviewPageUiStates.last().let { bulkReviewPageUiState ->
                        bulkReviewPageUiState is BulkReviewPageUiState.Showing &&
                            bulkReviewPageUiState
                            .items
                            .filterIsInstance<BulkReviewItemUiModel>()
                            .size == getFormUseCaseResultSuccess.reviewForm.size
                    }
                )
                // change rating to 4 so that remove review item confirmation dialog will be showed when calling onRemoveReviewItem
                viewModel.onRatingChanged(reviewItem.inboxID, 4)
                viewModel.onRemoveReviewItem(reviewItem.inboxID)
                viewModel.onConfirmRemoveReviewItem(
                    reviewItem.inboxID,
                    "Dummy Title",
                    "Dummy Subtitle"
                )

                // make sure that 1 review item is removed
                assertTrue(
                    bulkReviewPageUiStates.last().let { bulkReviewPageUiState ->
                        bulkReviewPageUiState is BulkReviewPageUiState.Showing &&
                            bulkReviewPageUiState
                            .items
                            .filterIsInstance<BulkReviewItemUiModel>()
                            .size == getFormUseCaseResultSuccess.reviewForm.size.dec()
                    }
                )
            }
        }

    @Test
    fun `onConfirmRemoveReviewItem should enqueue toaster`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewRemoveReviewItemDialogUiState {
                runCollectingBulkReviewPageToasterQueue { toasterQueue ->
                    val reviewItem = getFirstReviewItem()
                    val expectedToaster = CreateReviewToasterUiModel(
                        message = ResourceProvider.getMessageReviewItemRemoved(),
                        actionText = ResourceProvider.getCtaCancel(),
                        duration = Toaster.LENGTH_SHORT,
                        type = Toaster.TYPE_NORMAL,
                        id = 0,
                        payload = reviewItem.inboxID
                    )

                    doSuccessGetInitialData()
                    // change rating to 4 so that remove review item confirmation dialog will be showed when calling onRemoveReviewItem
                    viewModel.onRatingChanged(reviewItem.inboxID, 4)
                    viewModel.onRemoveReviewItem(reviewItem.inboxID)
                    viewModel.onConfirmRemoveReviewItem(
                        reviewItem.inboxID,
                        "Dummy Title",
                        "Dummy Subtitle"
                    )

                    assertEquals(expectedToaster, toasterQueue.last())
                }
            }
        }

    @Test
    fun `onConfirmRemoveReviewItem should dismiss remove review confirmation dialog`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewRemoveReviewItemDialogUiState { dialogUiStates ->
                val reviewItem = getFirstReviewItem()

                doSuccessGetInitialData()
                // change rating to 4 so that remove review item confirmation dialog will be showed when calling onRemoveReviewItem
                viewModel.onRatingChanged(reviewItem.inboxID, 4)
                viewModel.onRemoveReviewItem(reviewItem.inboxID)
                assertEquals(
                    BulkReviewRemoveReviewItemDialogUiState.Showing(
                        reviewItem.inboxID
                    ),
                    dialogUiStates.last()
                )
                viewModel.onConfirmRemoveReviewItem(
                    reviewItem.inboxID,
                    "Dummy Title",
                    "Dummy Subtitle"
                )

                assertEquals(BulkReviewRemoveReviewItemDialogUiState.Dismissed, dialogUiStates.last())
            }
        }

    @Test
    fun `onCancelRemoveReviewItem should execute BulkWriteReviewTracker#trackRemoveReviewItemDialogCancelButtonClick once`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewRemoveReviewItemDialogUiState {
                val reviewItem = getFirstReviewItem()

                doSuccessGetInitialData()
                // change rating to 4 so that remove review item confirmation dialog will be showed when calling onRemoveReviewItem
                viewModel.onRatingChanged(reviewItem.inboxID, 4)
                viewModel.onRemoveReviewItem(reviewItem.inboxID)
                viewModel.onCancelRemoveReviewItem(
                    "Dummy Title",
                    "Dummy Subtitle"
                )

                verify(exactly = 1) {
                    bulkWriteReviewTracker.trackRemoveReviewItemDialogCancelButtonClick(
                        userId = SAMPLE_USER_ID,
                        title = any(),
                        subtitle = any()
                    )
                }
            }
        }

    @Test
    fun `onCancelRemoveReviewItem should dismiss remove review confirmation dialog`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewRemoveReviewItemDialogUiState { dialogUiStates ->
                val reviewItem = getFirstReviewItem()

                doSuccessGetInitialData()
                // change rating to 4 so that remove review item confirmation dialog will be showed when calling onRemoveReviewItem
                viewModel.onRatingChanged(reviewItem.inboxID, 4)
                viewModel.onRemoveReviewItem(reviewItem.inboxID)
                assertEquals(
                    BulkReviewRemoveReviewItemDialogUiState.Showing(
                        reviewItem.inboxID
                    ),
                    dialogUiStates.last()
                )
                viewModel.onCancelRemoveReviewItem(
                    "Dummy Title",
                    "Dummy Subtitle"
                )

                assertEquals(BulkReviewRemoveReviewItemDialogUiState.Dismissed, dialogUiStates.last())
            }
        }

    @Test
    fun `onClickTestimonyMiniAction should execute BulkWriteReviewTracker#trackReviewItemAddTestimonyMiniActionClick once`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)

            verify(exactly = 1) {
                bulkWriteReviewTracker.trackReviewItemAddTestimonyMiniActionClick(
                    position = Int.ZERO,
                    orderId = reviewItem.orderID,
                    reputationId = reviewItem.reputationID,
                    productId = reviewItem.product.productID,
                    userId = SAMPLE_USER_ID
                )
            }
        }

    @Test
    fun `onClickTestimonyMiniAction should not execute BulkWriteReviewTracker#trackReviewItemAddTestimonyMiniActionClick when provided invalid inbox id`() =
        runCollectingBulkReviewPageUiState {
            doSuccessGetInitialData()
            viewModel.onClickTestimonyMiniAction("")

            verify(inverse = true) {
                bulkWriteReviewTracker.trackReviewItemAddTestimonyMiniActionClick(
                    position = any(),
                    orderId = any(),
                    reputationId = any(),
                    productId = any(),
                    userId = any()
                )
            }
        }

    @Test
    fun `onClickTestimonyMiniAction should show review item text area widget`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            // make sure that the text area widget is initially hidden
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        textAreaUiState is BulkReviewTextAreaUiState.Hidden
                    }
            )

            viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
            // make sure that the text area widget is showing after click on the add testimony mini action
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        textAreaUiState is BulkReviewTextAreaUiState.Showing
                    }
            )
        }

    @Test
    fun `onExpandTextArea should update review item testimony`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()
            val testimony = "Mantap betul ini!"

            doSuccessGetInitialData()
            // click on the add testimony mini action to show the text area widget
            viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)

            // click on the text area expand icon
            viewModel.onExpandTextArea(reviewItem.inboxID, testimony)
            assertEquals(
                testimony,
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .textAreaUiState.let { textAreaUiState ->
                        (textAreaUiState as BulkReviewTextAreaUiState.Showing).text
                    }
            )
        }

    @Test
    fun `onExpandTextArea should not update any review item testimony when provided invalid inbox id`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()
            val testimony = "Mantap betul ini!"

            doSuccessGetInitialData()
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .all { it.getReviewItemTextAreaText().isBlank() }
            )
            // click on the add testimony mini action to show the text area widget
            viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
            viewModel.onExpandTextArea("", testimony) // call onExpandTextArea with invalid inboxID
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .all { it.getReviewItemTextAreaText().isBlank() }
            )
        }

    @Test
    fun `onExpandTextArea should make expanded text area bottom sheet showing`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewExpandedTextAreaBottomSheetUiState { uiStates ->
                val reviewItem = getFirstReviewItem()

                doSuccessGetInitialData()
                viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
                assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)

                viewModel.onExpandTextArea(reviewItem.inboxID, "")
                assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Showing)
            }
        }

    @Test
    fun `onExpandTextArea should not make expanded text area bottom sheet showing when provided invalid inbox id`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewExpandedTextAreaBottomSheetUiState { uiStates ->
                val reviewItem = getFirstReviewItem()

                doSuccessGetInitialData()
                viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)
                assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)

                viewModel.onExpandTextArea("", "") // call onExpandTextArea with invalid inboxID
                assertTrue(uiStates.last() is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed)
            }
        }

    @Test
    fun `onAnonymousCheckChanged should change review anonymous value`() {
        assertFalse(viewModel.isAnonymous())
        viewModel.onAnonymousCheckChanged(true)
        assertTrue(viewModel.isAnonymous())
    }

    @Test
    fun `onReceiveMediaPickerResult should trigger media upload`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            coVerify(exactly = 5) { uploaderUseCase(any()) }
        }

    @Test
    fun `onReceiveMediaPickerResult should not trigger media upload when the same media already uploaded`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            mockSuccessUploadMedia()

            doSuccessGetInitialData()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Video(uri = "1.mp4", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Image(uri = "2.jpg", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Image(uri = "3.jpg", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Image(uri = "4.jpg", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Image(uri = "5.jpg", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState is CreateReviewMediaPickerUiState.Hidden
            )
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            coVerify(exactly = 5) { uploaderUseCase(any()) }
        }

    @Test
    fun `onReceiveMediaPickerResult should trigger media upload when the same media previously failed to upload`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            mockErrorUploadMedia()

            doSuccessGetInitialData()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Video(uri = "1.mp4", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Image(uri = "2.jpg", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Image(uri = "3.jpg", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Image(uri = "4.jpg", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            viewModel.onRemoveMedia(reviewItem.inboxID, CreateReviewMediaUiModel.Image(uri = "5.jpg", uploadBatchNumber = 1, state = CreateReviewMediaUiModel.State.UPLOADED))
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState is CreateReviewMediaPickerUiState.Hidden
            )
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            coVerify(exactly = 10) { uploaderUseCase(any()) }
        }

    @Test
    fun `onReceiveMediaPickerResult should show media picker widget if not empty`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            mockSuccessUploadMedia()
            doSuccessGetInitialData()
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        mediaPickerUiState is CreateReviewMediaPickerUiState.Hidden
                    }
            )

            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        mediaPickerUiState is CreateReviewMediaPickerUiState.SuccessUpload
                    }
            )
        }

    @Test
    fun `onReceiveMediaPickerResult should hide media picker widget if empty`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            `onReceiveMediaPickerResult should show media picker widget if not empty`()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf())
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        mediaPickerUiState is CreateReviewMediaPickerUiState.Hidden
                    }
            )
        }

    @Test
    fun `onRetryUploadClicked should re-upload and success`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            mockErrorUploadMedia()
            doSuccessGetInitialData()
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        mediaPickerUiState is CreateReviewMediaPickerUiState.Hidden
                    }
            )

            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        mediaPickerUiState is CreateReviewMediaPickerUiState.FailedUpload
                    }
            )

            mockSuccessUploadMedia()
            viewModel.onRetryUploadClicked(reviewItem.inboxID)
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        mediaPickerUiState is CreateReviewMediaPickerUiState.SuccessUpload
                    }
            )
        }

    @Test
    fun `onRemoveMedia should remove review item media item`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            mockSuccessUploadMedia()
            doSuccessGetInitialData()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            assertEquals(
                5,
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        (mediaPickerUiState as CreateReviewMediaPickerUiState.SuccessUpload)
                            .mediaItems
                            .count { it is CreateReviewMediaUiModel.Video || it is CreateReviewMediaUiModel.Image }
                    }
            )
            viewModel.onRemoveMedia(
                reviewItem.inboxID,
                CreateReviewMediaUiModel.Video(
                    uri = "1.mp4",
                    uploadBatchNumber = 1,
                    state = CreateReviewMediaUiModel.State.UPLOADED
                )
            )
            assertEquals(
                4,
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        (mediaPickerUiState as CreateReviewMediaPickerUiState.SuccessUpload)
                            .mediaItems
                            .count { it is CreateReviewMediaUiModel.Video || it is CreateReviewMediaUiModel.Image }
                    }
            )
        }

    @Test
    fun `onRemoveMedia should not remove any review item media item when provided invalid inbox id`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            mockSuccessUploadMedia()
            doSuccessGetInitialData()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            assertEquals(
                5,
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        (mediaPickerUiState as CreateReviewMediaPickerUiState.SuccessUpload)
                            .mediaItems
                            .count { it is CreateReviewMediaUiModel.Video || it is CreateReviewMediaUiModel.Image }
                    }
            )
            viewModel.onRemoveMedia(
                "",
                CreateReviewMediaUiModel.Video(
                    uri = "1.mp4",
                    uploadBatchNumber = 1,
                    state = CreateReviewMediaUiModel.State.UPLOADED
                )
            )
            assertEquals(
                5,
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .uiState
                    .mediaPickerUiState.let { mediaPickerUiState ->
                        (mediaPickerUiState as CreateReviewMediaPickerUiState.SuccessUpload)
                            .mediaItems
                            .count { it is CreateReviewMediaUiModel.Video || it is CreateReviewMediaUiModel.Image }
                    }
            )
        }

    @Test
    fun `onSubmitReviews should enqueue error upload media toaster when there's any failing media upload`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewPageToasterQueue { toasterQueue ->
                val reviewItem = getFirstReviewItem()
                val expectedToaster = CreateReviewToasterUiModel(
                    message = ResourceProvider.getMessageFailedUploadMedia(SAMPLE_ERROR_CODE),
                    actionText = StringRes(Int.ZERO),
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_ERROR,
                    payload = Unit
                )

                mockErrorUploadMedia()
                doSuccessGetInitialData()
                viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
                viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
                viewModel.onSubmitReviews()

                assertEquals(expectedToaster, toasterQueue.last())
            }
        }

    @Test
    fun `onSubmitReviews should enqueue wait for media upload toaster when there's any media upload in progress`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewPageToasterQueue { toasterQueue ->
                val reviewItem = getFirstReviewItem()
                val expectedToaster = CreateReviewToasterUiModel(
                    message = ResourceProvider.getMessageWaitForUploadMedia(),
                    actionText = StringRes(Int.ZERO),
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_NORMAL,
                    payload = Unit
                )
                var passed = false

                mockSuccessUploadMedia {
                    passed = try {
                        viewModel.onSubmitReviews()
                        assertEquals(expectedToaster, toasterQueue.last())
                        true
                    } catch (e: Throwable) {
                        false
                    }
                }
                doSuccessGetInitialData()
                viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
                viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
                assertTrue(passed)
            }
        }

    @Test
    fun `onSubmitReviews should execute submitUseCase exactly once`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()
            val badRatingCategory = getFirstBadRatingCategory()

            mockSuccessUploadMedia()
            mockAllSuccessSubmitBulkReview()

            doSuccessGetInitialData()
            viewModel.onRatingChanged(reviewItem.inboxID, 2)
            viewModel.onBadRatingCategorySelectionChanged(
                position = Int.ZERO,
                badRatingCategoryID = badRatingCategory.id,
                reason = badRatingCategory.description,
                selected = true
            )
            viewModel.onApplyBadRatingCategory()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            viewModel.onSubmitReviews()

            coVerify(exactly = 1) { submitUseCase(any()) }
        }

    @Test
    fun `onSubmitReviews should change ui state to Submitted when submitUseCase return all success`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()
            val badRatingCategory = getFirstBadRatingCategory()

            mockSuccessUploadMedia()
            mockAllSuccessSubmitBulkReview()

            doSuccessGetInitialData()
            viewModel.onRatingChanged(reviewItem.inboxID, 2)
            viewModel.onBadRatingCategorySelectionChanged(
                position = Int.ZERO,
                badRatingCategoryID = badRatingCategory.id,
                reason = badRatingCategory.description,
                selected = true
            )
            viewModel.onApplyBadRatingCategory()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            viewModel.onSubmitReviews()

            assertTrue(uiStates.last() is BulkReviewPageUiState.Submitted)
        }

    @Test
    fun `onSubmitReviews should change ui state to Showing when submitUseCase return partial success`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()
            val badRatingCategory = getFirstBadRatingCategory()

            mockSuccessUploadMedia()
            mockPartialSuccessSubmitBulkReview()

            doSuccessGetInitialData()
            viewModel.onRatingChanged(reviewItem.inboxID, 2)
            viewModel.onBadRatingCategorySelectionChanged(
                position = Int.ZERO,
                badRatingCategoryID = badRatingCategory.id,
                reason = badRatingCategory.description,
                selected = true
            )
            viewModel.onApplyBadRatingCategory()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            viewModel.onSubmitReviews()

            assertTrue(uiStates.last() is BulkReviewPageUiState.Showing)
        }

    @Test
    fun `onSubmitReviews should change ui state to Showing when submitUseCase return error`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()
            val badRatingCategory = getFirstBadRatingCategory()

            mockSuccessUploadMedia()
            mockErrorSubmitBulkReview()

            doSuccessGetInitialData()
            viewModel.onRatingChanged(reviewItem.inboxID, 2)
            viewModel.onBadRatingCategorySelectionChanged(
                position = Int.ZERO,
                badRatingCategoryID = badRatingCategory.id,
                reason = badRatingCategory.description,
                selected = true
            )
            viewModel.onApplyBadRatingCategory()
            viewModel.getAndUpdateActiveMediaPickerInboxID(reviewItem.inboxID)
            viewModel.onReceiveMediaPickerResult(listOf("1.mp4", "2.jpg", "3.jpg", "4.jpg", "5.jpg"))
            viewModel.onSubmitReviews()

            assertTrue(uiStates.last() is BulkReviewPageUiState.Showing)
        }

    @Test
    fun `onBackPressed should execute BulkWriteReviewTracker#trackPageDismissal once when not submitting bulk review`() =
        runCollectingBulkReviewPageUiState {
            doSuccessGetInitialData()
            viewModel.onBackPressed()

            verify(exactly = 10) {
                bulkWriteReviewTracker.trackPageDismissal(
                    position = any(),
                    orderId = any(),
                    reputationId = any(),
                    productId = any(),
                    userId = any(),
                    rating = any(),
                    isReviewEmpty = any(),
                    reviewLength = any(),
                    imageAttachmentCount = any(),
                    videoAttachmentCount = any(),
                    isAnonymous = any()
                )
            }
        }

    @Test
    fun `onBackPressed should show bulk review submission cancellation dialog when submitting bulk review`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewCancelReviewSubmissionDialogUiState { uiState ->
                var passed = false

                mockAllSuccessSubmitBulkReview {
                    passed = try {
                        viewModel.onBackPressed()
                        assertTrue(uiState.last() is BulkReviewCancelReviewSubmissionDialogUiState.Showing)
                        true
                    } catch (e: Throwable) {
                        false
                    }
                }
                doSuccessGetInitialData()
                viewModel.onSubmitReviews()

                assertTrue(passed)
            }
        }

    @Test
    fun `onCancelReviewSubmissionCancellation should dismiss bulk review submission cancellation dialog`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewCancelReviewSubmissionDialogUiState { uiState ->
                var passed = false

                mockAllSuccessSubmitBulkReview {
                    passed = try {
                        viewModel.onBackPressed()
                        assertTrue(uiState.last() is BulkReviewCancelReviewSubmissionDialogUiState.Showing)
                        viewModel.onCancelReviewSubmissionCancellation()
                        assertTrue(uiState.last() is BulkReviewCancelReviewSubmissionDialogUiState.Dismissed)
                        true
                    } catch (e: Throwable) {
                        false
                    }
                }
                doSuccessGetInitialData()
                viewModel.onSubmitReviews()

                assertTrue(passed)
            }
        }

    @Test
    fun `onConfirmCancelReviewSubmission should change bulk review page ui state to Cancelled and dismiss bulk review submission cancellation dialog`() =
        runCollectingBulkReviewPageUiState { pageUiStates ->
            runCollectingBulkReviewCancelReviewSubmissionDialogUiState { dialogUiStates ->
                var passed = false

                mockAllSuccessSubmitBulkReview {
                    passed = try {
                        viewModel.onBackPressed()
                        assertTrue(dialogUiStates.last() is BulkReviewCancelReviewSubmissionDialogUiState.Showing)
                        viewModel.onConfirmCancelReviewSubmission()
                        assertTrue(dialogUiStates.last() is BulkReviewCancelReviewSubmissionDialogUiState.Dismissed)
                        true
                    } catch (e: Throwable) {
                        false
                    }
                }
                doSuccessGetInitialData()
                viewModel.onSubmitReviews()

                assertTrue(passed)
                assertTrue(pageUiStates.last() is BulkReviewPageUiState.Cancelled)
            }
        }

    @Test
    fun `onSaveInstanceState should save current states`() = runCollectingBulkReviewPageUiState {
        val saveInstanceCacheManager = mockk<SaveInstanceCacheManager>(relaxed = true)
        doSuccessGetInitialData()
        viewModel.onSaveInstanceState(saveInstanceCacheManager)
        verify(exactly = 1) {
            saveInstanceCacheManager.put("getFormRequestState", any())
            saveInstanceCacheManager.put("getBadRatingCategoryRequestState", any())
            saveInstanceCacheManager.put("submitBulkReviewRequestState", any())
            saveInstanceCacheManager.put("removedReviewItemsInboxID", any())
            saveInstanceCacheManager.put("reviewItemsRating", any())
            saveInstanceCacheManager.put("reviewItemsBadRatingCategory", any())
            saveInstanceCacheManager.put("reviewItemsTestimony", any())
            saveInstanceCacheManager.put("reviewItemsMediaUris", any())
            saveInstanceCacheManager.put("reviewItemsMediaUploadResults", any())
            saveInstanceCacheManager.put("reviewItemsMediaUploadBatchNumber", any())
            saveInstanceCacheManager.put("anonymous", any())
            saveInstanceCacheManager.put("shouldSubmitReview", any())
            saveInstanceCacheManager.put("activeMediaPickerInboxID", any())
        }
    }

    @Test
    fun `onRestoreInstanceState should restore saved states`() =
        runCollectingBulkReviewPageUiState {
            doSuccessGetInitialData()
            doRestoreInstanceState()
        }

    @Test
    fun `onRestoreInstanceState should not call getData when all previous state is not null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState { verify(inverse = true) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should trigger submit bulk review when shouldSubmitReview is true`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            mockAllSuccessSubmitBulkReview()
            doRestoreInstanceState(
                mockShouldSubmitReview = true
            ) { assertTrue(uiStates.last() is BulkReviewPageUiState.Submitted) }
        }

    @Test
    fun `onRestoreInstanceState should call getData when getFormRequestState is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockGetFormRequestState = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when getBadRatingCategoryRequestState is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockGetBadRatingCategoryRequestState = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when submitBulkReviewRequestState is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockSubmitBulkReviewRequestState = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when removedReviewItemsInboxID is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockRemovedReviewItemsInboxID = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when reviewItemsRating is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockReviewItemsRating = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when reviewItemsBadRatingCategory is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockReviewItemsBadRatingCategory = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when reviewItemsTestimony is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockReviewItemsTestimony = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when reviewItemsMediaUris is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockReviewItemsMediaUris = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when reviewItemsMediaUploadResults is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockReviewItemsMediaUploadResults = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when reviewItemsMediaUploadBatchNumber is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockReviewItemsMediaUploadBatchNumber = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when anonymous is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockAnonymous = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when shouldSubmitReview is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockShouldSubmitReview = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `onRestoreInstanceState should call getData when activeMediaPickerInboxID is null`() =
        runCollectingBulkReviewPageUiState {
            mockSuccessGetFormResult()
            mockSuccessBadRatingCategoryResult()
            doRestoreInstanceState(
                mockActiveMediaPickerInboxID = null
            ) { verify(exactly = 1) { it.getData() } }
        }

    @Test
    fun `findFocusedReviewItemVisitable should return null when there's no focused review item`() =
        runCollectingBulkReviewPageUiState {
            doSuccessGetInitialData()
            assertNull(viewModel.findFocusedReviewItemVisitable())
        }

    @Test
    fun `findFocusedReviewItemVisitable should return non-null when there's focused review item`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            viewModel.onClickTestimonyMiniAction(reviewItem.inboxID)

            val focusedReviewItem = viewModel.findFocusedReviewItemVisitable()
            assertEquals(Int.ONE, focusedReviewItem?.first)
            assertNotNull(focusedReviewItem?.second)
        }

    @Test
    fun `getAndUpdateActiveMediaPickerInboxID should update activeMediaPickerInboxID then return it's prior value`() {
        val initialValue = viewModel.getAndUpdateActiveMediaPickerInboxID("123")
        val priorValue = viewModel.getAndUpdateActiveMediaPickerInboxID("321")

        assertEquals("", initialValue)
        assertEquals("123", priorValue)
    }

    @Test
    fun `enqueueToasterDisabledAddMoreMedia should enqueue expected toaster`() =
        runCollectingBulkReviewPageToasterQueue { toasterQueue ->
            val expectedToaster = CreateReviewToasterUiModel(
                message = ResourceProvider.getErrorMessageCannotAddMediaWhileUploading(),
                actionText = StringRes(Int.ZERO),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL,
                payload = Unit
            )

            assertTrue(toasterQueue.isEmpty())
            viewModel.enqueueToasterDisabledAddMoreMedia()
            assertEquals(expectedToaster, toasterQueue.last())
        }

    @Test
    fun `isAnonymous should return false when anonymous is false`() {
        assertFalse(viewModel.isAnonymous())
    }

    @Test
    fun `isAnonymous should return true when anonymous is true`() {
        viewModel.onAnonymousCheckChanged(true)
        assertTrue(viewModel.isAnonymous())
    }

    @Test
    fun `getUserId should return userID from user session`() {
        assertEquals(SAMPLE_USER_ID, viewModel.getUserId())
    }

    @Test
    fun `onReviewItemImpressed should set review item ImpressHolder as invoked`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            assertFalse(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .impressHolder
                    .isInvoke
            )

            viewModel.onReviewItemImpressed(reviewItem.inboxID)
            assertTrue(
                (uiStates.last() as BulkReviewPageUiState.Showing)
                    .items
                    .filterIsInstance<BulkReviewItemUiModel>()
                    .first { it.inboxID == reviewItem.inboxID }
                    .impressHolder
                    .isInvoke
            )
        }

    @Test
    fun `onReviewItemImpressed should trigger BulkWriteReviewTracker#trackReviewItemImpression once`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            viewModel.onReviewItemImpressed(reviewItem.inboxID)

            verify(exactly = 1) {
                bulkWriteReviewTracker.trackReviewItemImpression(
                    position = Int.ZERO,
                    orderId = reviewItem.orderID,
                    reputationId = reviewItem.reputationID,
                    productId = reviewItem.product.productID,
                    userId = SAMPLE_USER_ID
                )
            }
        }

    @Test
    fun `onBadRatingCategoryImpressed should trigger BulkWriteReviewTracker#trackReviewItemBadRatingCategoryImpression once`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewBadRatingCategoryBottomSheetUiState {
                val reviewItem = getFirstReviewItem()
                val badRatingCategory = getFirstBadRatingCategory()

                doSuccessGetInitialData()
                viewModel.onRatingChanged(reviewItem.inboxID, 2)
                viewModel.onBadRatingCategoryImpressed(Int.ZERO, badRatingCategory.description)

                verify(exactly = 1) {
                    bulkWriteReviewTracker.trackReviewItemBadRatingCategoryImpression(
                        position = Int.ZERO,
                        orderId = reviewItem.orderID,
                        reputationId = reviewItem.reputationID,
                        productId = reviewItem.product.productID,
                        userId = SAMPLE_USER_ID,
                        rating = 2,
                        reason = badRatingCategory.description
                    )
                }
            }
        }

    @Test
    fun `onRemoveReviewItemDialogImpressed should trigger BulkWriteReviewTracker#trackRemoveReviewItemDialogImpression once`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewRemoveReviewItemDialogUiState {
                val reviewItem = getFirstReviewItem()

                doSuccessGetInitialData()
                viewModel.onRatingChanged(reviewItem.inboxID, 4)
                viewModel.onRemoveReviewItem(reviewItem.inboxID)
                viewModel.onRemoveReviewItemDialogImpressed(reviewItem.inboxID, "Dummy title", "Dummy subtitle")

                verify(exactly = 1) {
                    bulkWriteReviewTracker.trackRemoveReviewItemDialogImpression(
                        position = Int.ZERO,
                        orderId = reviewItem.orderID,
                        reputationId = reviewItem.reputationID,
                        productId = reviewItem.product.productID,
                        userId = SAMPLE_USER_ID,
                        title = "Dummy title",
                        subtitle = "Dummy subtitle"
                    )
                }
            }
        }

    @Test
    fun `onRemoveReviewItemDialogImpressed should not trigger BulkWriteReviewTracker#trackRemoveReviewItemDialogImpression when provided inbox id`() =
        runCollectingBulkReviewPageUiState {
            runCollectingBulkReviewRemoveReviewItemDialogUiState {
                val reviewItem = getFirstReviewItem()

                doSuccessGetInitialData()
                viewModel.onRatingChanged(reviewItem.inboxID, 4)
                viewModel.onRemoveReviewItem(reviewItem.inboxID)
                viewModel.onRemoveReviewItemDialogImpressed("", "Dummy title", "Dummy subtitle")

                verify(inverse = true) {
                    bulkWriteReviewTracker.trackRemoveReviewItemDialogImpression(
                        position = any(),
                        orderId = any(),
                        reputationId = any(),
                        productId = any(),
                        userId = any(),
                        title = any(),
                        subtitle = any()
                    )
                }
            }
        }

    @Test
    fun `onClickAddAttachmentMiniAction should trigger BulkWriteReviewTracker#trackReviewItemAddAttachmentMiniActionClick once`() =
        runCollectingBulkReviewPageUiState {
            val reviewItem = getFirstReviewItem()

            doSuccessGetInitialData()
            viewModel.onClickAddAttachmentMiniAction(reviewItem.inboxID)

            verify(exactly = 1) {
                bulkWriteReviewTracker.trackReviewItemAddAttachmentMiniActionClick(
                    position = Int.ZERO,
                    orderId = reviewItem.orderID,
                    reputationId = reviewItem.reputationID,
                    productId = reviewItem.product.productID,
                    userId = SAMPLE_USER_ID
                )
            }
        }

    @Test
    fun `onClickAddAttachmentMiniAction should not trigger BulkWriteReviewTracker#trackReviewItemAddAttachmentMiniActionClick when provided inbox id`() =
        runCollectingBulkReviewPageUiState {
            doSuccessGetInitialData()
            viewModel.onClickAddAttachmentMiniAction("")

            verify(inverse = true) {
                bulkWriteReviewTracker.trackReviewItemAddAttachmentMiniActionClick(
                    position = any(),
                    orderId = any(),
                    reputationId = any(),
                    productId = any(),
                    userId = any()
                )
            }
        }

    @Test
    fun `sendAllTrackers should trigger BulkWriteReviewTracker#sendAllTrackers once`() {
        viewModel.sendAllTrackers()
        verify(exactly = 1) { bulkWriteReviewTracker.sendAllTrackers() }
    }

    @Test
    fun `onToasterCtaClicked should undo review item removal`() =
        runCollectingBulkReviewPageUiState { uiStates ->
            runCollectingBulkReviewPageToasterQueue { toasterQueue ->
                `onRemoveReviewItem should remove review item when haveMinimumReviewItem and hasDefaultState return true`()
                viewModel.onToasterCtaClicked(toasterQueue.last())
                assertTrue(
                    uiStates.last().let { uiState ->
                        uiState is BulkReviewPageUiState.Showing && uiState.items.filterIsInstance<BulkReviewItemUiModel>().size == getFormUseCaseResultSuccess.reviewForm.size
                    }
                )
            }
        }
}
