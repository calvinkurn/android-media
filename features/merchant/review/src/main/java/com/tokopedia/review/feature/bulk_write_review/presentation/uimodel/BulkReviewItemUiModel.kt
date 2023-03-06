package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.mapper.BulkReviewRatingUiStateMapper
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewItemUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewProductInfoUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.reviewcommon.uimodel.StringRes

data class BulkReviewItemUiModel(
    val position: Int,
    val inboxID: String,
    val reputationID: String,
    val orderID: String,
    val shopID: String,
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
            !isOverlayVisibilityChanged(other)
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
                if (isOverlayVisibilityChanged(other)) {
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
        return uiState.productCardUiState.let { productCardUiState ->
            if (productCardUiState is BulkReviewProductInfoUiState.Showing) {
                productCardUiState.productID
            } else {
                ""
            }
        }
    }

    fun getReviewItemRating(): Int {
        return uiState.ratingUiState.let { ratingUiState ->
            if (ratingUiState is BulkReviewRatingUiState.Showing) {
                ratingUiState.rating
            } else {
                Int.ZERO
            }
        }
    }

    fun isReviewItemHasEmptyReview(): Boolean {
        return uiState.textAreaUiState.let { textAreaUiState ->
            if (textAreaUiState is BulkReviewTextAreaUiState.Showing) {
                textAreaUiState.text.isBlank()
            } else {
                true
            }
        }
    }

    fun getReviewItemReviewLength(): Int {
        return uiState.textAreaUiState.let { textAreaUiState ->
            if (textAreaUiState is BulkReviewTextAreaUiState.Showing) {
                textAreaUiState.text.length
            } else {
                Int.ZERO
            }
        }
    }

    fun getReviewItemImageAttachmentCount(): Int {
        return uiState.mediaPickerUiState.let { mediaPickerUiState ->
            if (mediaPickerUiState is CreateReviewMediaPickerUiState.HasMedia) {
                mediaPickerUiState.getImageCount()
            } else {
                Int.ZERO
            }
        }
    }

    fun getReviewItemImageAttachmentIds(): List<String> {
        return uiState.mediaPickerUiState.let { mediaPickerUiState ->
            if (mediaPickerUiState is CreateReviewMediaPickerUiState.HasMedia) {
                mediaPickerUiState
                    .mediaItems
                    .filterIsInstance<CreateReviewMediaUiModel.Image>()
                    .map { it.uploadId }
            } else {
                emptyList()
            }
        }
    }

    fun getReviewItemAttachmentPaths(): List<String> {
        return uiState.mediaPickerUiState.let { mediaPickerUiState ->
            if (mediaPickerUiState is CreateReviewMediaPickerUiState.HasMedia) {
                mediaPickerUiState
                    .mediaItems
                    .mapNotNull {
                        if (it is CreateReviewMediaUiModel.Image || it is CreateReviewMediaUiModel.Video) {
                            it.uri
                        } else {
                            null
                        }
                    }
            } else {
                emptyList()
            }
        }
    }

    fun getReviewItemVideoAttachmentCount(): Int {
        return uiState.mediaPickerUiState.let { mediaPickerUiState ->
            if (mediaPickerUiState is CreateReviewMediaPickerUiState.HasMedia) {
                mediaPickerUiState.getVideoCount()
            } else {
                Int.ZERO
            }
        }
    }

    fun getReviewItemTextAreaHint(): StringRes {
        return uiState.textAreaUiState.let { textAreaUiState ->
            if (textAreaUiState is BulkReviewTextAreaUiState.Showing) {
                textAreaUiState.hint
            } else {
                StringRes(Int.ZERO)
            }
        }
    }

    fun getReviewItemTextAreaText(): String {
        return uiState.textAreaUiState.let { textAreaUiState ->
            if (textAreaUiState is BulkReviewTextAreaUiState.Showing) {
                textAreaUiState.text
            } else {
                ""
            }
        }
    }

    fun getReviewItemSelectedBadRatingCategoryIds(): List<String> {
        return uiState.badRatingCategoriesUiState.let { badRatingCategoriesUiState ->
            if (badRatingCategoriesUiState is BulkReviewBadRatingCategoryUiState.Showing) {
                badRatingCategoriesUiState
                    .badRatingCategory
                    .filter { badRatingCategory -> badRatingCategory.selected }
                    .map { badRatingCategory -> badRatingCategory.id }
            } else {
                emptyList()
            }
        }
    }

    private fun isOverlayVisibilityChanged(other: BulkReviewItemUiModel): Boolean {
        return uiState::class.java != other.uiState::class.java
    }

    sealed interface ChangePayload {
        object BadRatingCategoriesChanged : ChangePayload
        object TextAreaChanged : ChangePayload
        object MediaPickerChanged : ChangePayload
        object MiniActionsChanged : ChangePayload
        object OverlayVisibilityChanged : ChangePayload
    }
}
