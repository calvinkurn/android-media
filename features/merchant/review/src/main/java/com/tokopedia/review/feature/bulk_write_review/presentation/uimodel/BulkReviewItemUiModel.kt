package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewRatingUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewItemUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState

data class BulkReviewItemUiModel(
    val position: Int,
    val inboxID: String,
    val reputationID: String,
    val orderID: String,
    val impressHolder: ImpressHolder,
    val uiState: BulkReviewItemUiState
) : Visitable<BulkReviewAdapterTypeFactory>, BulkReviewVisitable<BulkReviewAdapterTypeFactory> {
    override fun type(typeFactory: BulkReviewAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: BulkReviewVisitable<BulkReviewAdapterTypeFactory>): Boolean {
        return other is BulkReviewItemUiModel && inboxID == other.inboxID
    }

    override fun areContentsTheSame(other: BulkReviewVisitable<BulkReviewAdapterTypeFactory>): Boolean {
        return other is BulkReviewItemUiModel &&
            uiState.badRatingCategoriesUiState == other.uiState.badRatingCategoriesUiState &&
            uiState.textAreaUiState.areContentsTheSame(other.uiState.textAreaUiState) &&
            uiState.mediaPickerUiState == other.uiState.mediaPickerUiState &&
            uiState.miniActionsUiState == other.uiState.miniActionsUiState &&
            uiState::class.java == other.uiState::class.java
    }

    override fun getChangePayload(other: BulkReviewVisitable<BulkReviewAdapterTypeFactory>): Any? {
        return if (other is BulkReviewItemUiModel) {
            mutableListOf<ChangePayload>().apply {
                if (uiState.badRatingCategoriesUiState != other.uiState.badRatingCategoriesUiState) {
                    add(ChangePayload.BadRatingCategoriesChanged)
                }
                if (!uiState.textAreaUiState.areContentsTheSame(other.uiState.textAreaUiState)) {
                    add(ChangePayload.TextAreaChanged)
                }
                if (uiState.mediaPickerUiState != other.uiState.mediaPickerUiState) {
                    add(ChangePayload.MediaPickerChanged)
                }
                if (uiState.miniActionsUiState != other.uiState.miniActionsUiState) {
                    add(ChangePayload.MiniActionsChanged)
                }
                if (uiState::class.java != other.uiState::class.java) {
                    add(ChangePayload.OverlayVisibilityChanged)
                }
            }
        } else {
            null
        }
    }

    fun hasDefaultState(): Boolean {
        val ratingUiState = uiState.ratingUiState
        val badRatingCategoriesUiState = uiState.badRatingCategoriesUiState
        val textAreaUiState = uiState.textAreaUiState
        val mediaPickerUiState = uiState.mediaPickerUiState
        return ratingUiState is BulkReviewRatingUiState.Showing &&
            ratingUiState.rating == BulkReviewRatingUiStateMapper.DEFAULT_PRODUCT_RATING &&
            badRatingCategoriesUiState is BulkReviewBadRatingCategoryUiState.Hidden &&
            textAreaUiState is BulkReviewTextAreaUiState.Hidden &&
            mediaPickerUiState is CreateReviewMediaPickerUiState.Hidden
    }

    fun getReviewItemProductId(): String {
        return uiState.getReviewItemProductId()
    }

    fun getReviewItemRating(): Int {
        return uiState.getReviewItemRating()
    }

    fun isReviewItemHasEmptyReview(): Boolean {
        return uiState.isReviewItemHasEmptyReview()
    }

    fun getReviewItemReviewLength(): Int {
        return uiState.getReviewItemReviewLength()
    }

    fun getReviewItemImageAttachmentCount(): Int {
        return uiState.getReviewItemImageAttachmentCount()
    }

    fun getReviewItemVideoAttachmentCount(): Int {
        return uiState.getReviewItemVideoAttachmentCount()
    }

    sealed interface ChangePayload {
        object BadRatingCategoriesChanged : ChangePayload
        object TextAreaChanged : ChangePayload
        object MediaPickerChanged : ChangePayload
        object MiniActionsChanged : ChangePayload
        object OverlayVisibilityChanged : ChangePayload
    }
}
