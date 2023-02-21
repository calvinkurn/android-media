package com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemBulkReviewFormBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewItemUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionsUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewProductInfoUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.widget.WidgetBulkReviewBadRatingCategories
import com.tokopedia.review.feature.bulk_write_review.presentation.widget.WidgetBulkReviewMiniActions
import com.tokopedia.review.feature.bulk_write_review.presentation.widget.WidgetBulkReviewRating
import com.tokopedia.review.feature.bulk_write_review.presentation.widget.WidgetBulkReviewTextArea
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewMediaPicker
import com.tokopedia.reviewcommon.extension.intersectWith

class BulkReviewItemViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<BulkReviewItemUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_bulk_review_form
        private const val OVERLAY_VISIBLE_ALPHA = 0.5f
        private const val OVERLAY_INVISIBLE_ALPHA = 0f
    }

    private val binding = ItemBulkReviewFormBinding.bind(itemView)

    init {
        setupRootView()
    }

    override fun bind(element: BulkReviewItemUiModel) {
        setupImpressionListener(element)
        setupProductInfo(productCardUiState = element.uiState.productCardUiState)
        setupRating(
            inboxID = element.inboxID,
            ratingUiState = element.uiState.ratingUiState
        )
        setupBadRatingCategories(
            inboxID = element.inboxID,
            badRatingCategoriesUiState = element.uiState.badRatingCategoriesUiState,
            animate = false
        )
        setupTextArea(
            inboxID = element.inboxID,
            textAreaUiState = element.uiState.textAreaUiState,
            animate = false
        )
        setupAttachments(
            inboxID = element.inboxID,
            mediaPickerUiState = element.uiState.mediaPickerUiState,
            animate = false
        )
        setupMiniActions(
            inboxID = element.inboxID,
            miniActionsUiState = element.uiState.miniActionsUiState,
            animate = false
        )
        setupRemoveIcon(inboxID = element.inboxID)
        setupOverlayVisibility(uiState = element.uiState)
        setupGestureListener(uiState = element.uiState)
    }

    override fun bind(element: BulkReviewItemUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            return
        }
        (payloads.firstOrNull())?.let { changePayloads ->
            if (changePayloads is List<*>) {
                changePayloads.forEach {
                    when (it) {
                        is BulkReviewItemUiModel.ChangePayload.BadRatingCategoriesChanged -> {
                            setupBadRatingCategories(
                                inboxID = element.inboxID,
                                badRatingCategoriesUiState = element.uiState.badRatingCategoriesUiState,
                                animate = true
                            )
                        }
                        is BulkReviewItemUiModel.ChangePayload.TextAreaChanged -> {
                            setupTextArea(
                                inboxID = element.inboxID,
                                textAreaUiState = element.uiState.textAreaUiState,
                                animate = true
                            )
                        }
                        is BulkReviewItemUiModel.ChangePayload.MediaPickerChanged -> {
                            setupAttachments(
                                inboxID = element.inboxID,
                                mediaPickerUiState = element.uiState.mediaPickerUiState,
                                animate = true
                            )
                        }
                        is BulkReviewItemUiModel.ChangePayload.MiniActionsChanged -> {
                            setupMiniActions(
                                inboxID = element.inboxID,
                                miniActionsUiState = element.uiState.miniActionsUiState,
                                animate = true
                            )
                        }
                        is BulkReviewItemUiModel.ChangePayload.OverlayVisibilityChanged -> {
                            setupOverlayVisibility(uiState = element.uiState)
                            setupGestureListener(uiState = element.uiState)
                        }
                    }
                }
            }
        }
    }

    private fun setupRootView() {
        binding.root.setOnClickListener { }
    }

    private fun setupImpressionListener(element: BulkReviewItemUiModel) {
        binding.root.addOnImpressionListener(element.impressHolder) {
            listener.onReviewItemImpressed(element.inboxID)
        }
    }

    private fun setupProductInfo(productCardUiState: BulkReviewProductInfoUiState) {
        binding.widgetBulkReviewProductInfo.updateUiState(productCardUiState)
    }

    private fun setupRating(inboxID: String, ratingUiState: BulkReviewRatingUiState) {
        binding.widgetBulkWriteReviewFormRating.reset()
        binding.widgetBulkWriteReviewFormRating.updateUiState(ratingUiState)
        binding.widgetBulkWriteReviewFormRating.setListener(object :
                WidgetBulkReviewRating.Listener {
                override fun onRatingChanged(rating: Int) {
                    listener.onRatingChanged(inboxID, rating)
                }
            })
        listener.onRatingSet(inboxID)
    }

    private fun setupBadRatingCategories(
        inboxID: String,
        badRatingCategoriesUiState: BulkReviewBadRatingCategoryUiState,
        animate: Boolean
    ) {
        binding.widgetBulkReviewBadRatingCategories.updateUiState(badRatingCategoriesUiState, animate)
        binding.widgetBulkReviewBadRatingCategories.setListener(object :
                WidgetBulkReviewBadRatingCategories.Listener {
                override fun onClickChangeBadRatingCategory() {
                    listener.onClickChangeBadRatingCategory(inboxID)
                }
            })
    }

    private fun setupTextArea(inboxID: String, textAreaUiState: BulkReviewTextAreaUiState, animate: Boolean) {
        binding.widgetBulkReviewTextArea.updateUiState(textAreaUiState, animate)
        binding.widgetBulkReviewTextArea.setListener(object : WidgetBulkReviewTextArea.Listener {
            override fun onGainFocus(view: View) {
                listener.onTextAreaGainFocus(inboxID, view)
            }

            override fun onLostFocus(view: View, text: String) {
                listener.onTextAreaLostFocus(inboxID, view, text)
            }

            override fun onExpandTextArea(text: String) {
                listener.onExpandTextArea(inboxID, text)
            }
        })
    }

    private fun setupAttachments(
        inboxID: String,
        mediaPickerUiState: CreateReviewMediaPickerUiState,
        animate: Boolean
    ) {
        binding.widgetBulkReviewAttachments.updateUi(uiState = mediaPickerUiState, animate = animate)
        binding.widgetBulkReviewAttachments.setListener(object : CreateReviewMediaPicker.Listener {
            override fun onAddMediaClicked(enabled: Boolean) {
                listener.onAddMediaClicked(inboxID, enabled)
            }

            override fun onRemoveMediaClicked(media: CreateReviewMediaUiModel) {
                listener.onRemoveMediaClicked(inboxID, media)
            }

            override fun onRetryUploadClicked() {
                listener.onRetryUploadClicked(inboxID)
            }
        })
    }

    private fun setupMiniActions(
        inboxID: String,
        miniActionsUiState: BulkReviewMiniActionsUiState,
        animate: Boolean
    ) {
        binding.widgetBulkReviewMiniActions.updateUiState(miniActionsUiState, animate)
        binding.widgetBulkReviewMiniActions.setListener(object :
                WidgetBulkReviewMiniActions.Listener {
                override fun onClickTestimonyMiniAction() {
                    listener.onClickTestimonyMiniAction(inboxID)
                }

                override fun onClickAddAttachmentMiniAction() {
                    listener.onClickAddAttachmentMiniAction(inboxID)
                }
            })
    }

    private fun setupRemoveIcon(inboxID: String) {
        binding.icBulkReviewRemove.setOnClickListener {
            listener.onClickRemoveReviewItem(inboxID)
        }
    }

    private fun setupOverlayVisibility(uiState: BulkReviewItemUiState) {
        binding.viewBulkReviewOverlay.alpha = if (uiState is BulkReviewItemUiState.Dimmed) {
            OVERLAY_VISIBLE_ALPHA
        } else {
            OVERLAY_INVISIBLE_ALPHA
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupGestureListener(uiState: BulkReviewItemUiState) {
        val gestureListener = BulkReviewItemGestureListener(uiState)
        val gestureDetector = GestureDetectorCompat(binding.root.context, gestureListener)
        binding.viewBulkReviewOverlay.setOnTouchListener { _, e ->
            gestureDetector.onTouchEvent(e)
            val dimmed = uiState is BulkReviewItemUiState.Dimmed
            val focused = uiState is BulkReviewItemUiState.Focused
            val touchedAboveTextArea = e.isAboveTextArea()
            val touchedAboveRating = e.isAboveRating()
            dimmed || (focused && !touchedAboveTextArea && !touchedAboveRating)
        }
    }

    private fun MotionEvent.isAboveTextArea(): Boolean {
        return intersectWith(binding.widgetBulkReviewTextArea, 0L)
    }

    private fun MotionEvent.isAboveRating(): Boolean {
        return intersectWith(binding.widgetBulkWriteReviewFormRating, 0L)
    }

    interface Listener {
        fun onClickRemoveReviewItem(inboxID: String)
        fun onRatingChanged(inboxID: String, rating: Int)
        fun onRatingSet(inboxID: String)
        fun onClickChangeBadRatingCategory(inboxID: String)
        fun onClickTestimonyMiniAction(inboxID: String)
        fun onClickAddAttachmentMiniAction(inboxID: String)
        fun onTextAreaGainFocus(inboxID: String, view: View)
        fun onTextAreaLostFocus(inboxID: String, view: View, text: String)
        fun onExpandTextArea(inboxID: String, text: String)
        fun onSingleTapToDismissKeyboard()
        fun onAddMediaClicked(inboxID: String, enabled: Boolean)
        fun onRemoveMediaClicked(inboxID: String, media: CreateReviewMediaUiModel)
        fun onRetryUploadClicked(inboxID: String)
        fun onReviewItemImpressed(inboxID: String)
    }

    private inner class BulkReviewItemGestureListener(
        private val uiState: BulkReviewItemUiState
    ) : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (uiState is BulkReviewItemUiState.Dimmed || uiState is BulkReviewItemUiState.Focused) {
                listener.onSingleTapToDismissKeyboard()
            }
            return false
        }
    }
}
