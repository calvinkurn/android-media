package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.os.Bundle
import android.view.View
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemCreateReviewMediaPickerAddSmallBinding
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewMediaAdapter
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView

class CreateReviewMediaPickerAddSmallViewHolder(
    view: View,
    private val listener: CreateReviewMediaAdapter.Listener
) : AbstractViewHolder<CreateReviewMediaUiModel.AddSmall>(view) {

    companion object {
        val LAYOUT = R.layout.item_create_review_media_picker_add_small
    }

    private val binding = ItemCreateReviewMediaPickerAddSmallBinding.bind(view)
    private val transitionHandler = TransitionHandler()

    init {
        attachEnabledAddMediaClickListener()
        attachDisabledAddMediaClickListener()
    }

    override fun bind(element: CreateReviewMediaUiModel.AddSmall) {
        setupEnableState(element.enabled)
    }

    override fun bind(element: CreateReviewMediaUiModel.AddSmall?, payloads: MutableList<Any>) {
        payloads.firstOrNull().let { payload ->
            if (payload is Bundle) {
                payload.get(CreateReviewMediaUiModel.PAYLOAD_ADD_MEDIA_ENABLE_STATE).let {
                    if (it is Boolean) {
                        setupEnableState(it)
                    }
                }
            }
        }
    }

    private fun setupEnableState(enabled: Boolean) {
        if (enabled) transitionHandler.transitionToEnabled() else transitionHandler.transitionToDisabled()
    }

    private fun attachEnabledAddMediaClickListener() {
        binding.containerReviewMediaPickerCameraEnabled.setOnClickListener {
            listener.onAddMediaClicked(true)
        }
    }

    private fun attachDisabledAddMediaClickListener() {
        binding.containerReviewMediaPickerCameraDisabled.setOnClickListener {
            listener.onAddMediaClicked(false)
        }
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade()
                .addTarget(binding.containerReviewMediaPickerCameraEnabled)
                .addTarget(binding.containerReviewMediaPickerCameraDisabled)
                .setInterpolator(
                    PathInterpolatorCompat.create(
                        BaseReviewCustomView.CUBIC_BEZIER_X1,
                        BaseReviewCustomView.CUBIC_BEZIER_Y1,
                        BaseReviewCustomView.CUBIC_BEZIER_X2,
                        BaseReviewCustomView.CUBIC_BEZIER_Y2
                    )
                )
                .setDuration(BaseReviewCustomView.ANIMATION_DURATION)
        }

        private fun ItemCreateReviewMediaPickerAddSmallBinding.showEnabledState() {
            containerReviewMediaPickerCameraEnabled.show()
            containerReviewMediaPickerCameraDisabled.gone()
        }

        private fun ItemCreateReviewMediaPickerAddSmallBinding.showDisabledState() {
            containerReviewMediaPickerCameraDisabled.show()
            containerReviewMediaPickerCameraEnabled.gone()
        }

        fun transitionToEnabled() {
            TransitionManager.beginDelayedTransition(binding.root, fadeTransition)
            binding.showEnabledState()
        }

        fun transitionToDisabled() {
            TransitionManager.beginDelayedTransition(binding.root, fadeTransition)
            binding.showDisabledState()
        }
    }
}