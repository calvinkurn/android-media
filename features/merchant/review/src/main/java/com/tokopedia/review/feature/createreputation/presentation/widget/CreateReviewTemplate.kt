package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetCreateReviewTemplateBinding
import com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewTemplateAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewTemplateTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewTemplateItemUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTemplateUiState
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewTemplateItemViewHolder
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateReviewTemplate @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewTemplateBinding>(context, attrs, defStyleAttr) {

    private val transitionHandler = TransitionHandler()
    private val createReviewTemplateListener = CreateReviewTemplateListener()
    private val layoutManager = StaggeredGridLayoutManager(CreateReviewBottomSheet.TEMPLATES_ROW_COUNT, RecyclerView.HORIZONTAL)
    private val typeFactory = CreateReviewTemplateTypeFactory(createReviewTemplateListener)
    private val adapter = CreateReviewTemplateAdapter(typeFactory)

    override val binding = WidgetCreateReviewTemplateBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.rvTemplate.layoutManager = layoutManager
        binding.rvTemplate.adapter = adapter
    }

    private fun showLoading() {
        transitionHandler.transitionToShowLoading()
    }

    private fun showTemplate(templates: List<CreateReviewTemplateItemUiModel>) {
        transitionHandler.transitionToShowTemplate()
        setupTemplate(templates)
    }

    private fun setupTemplate(templates: List<CreateReviewTemplateItemUiModel>) {
        adapter.updateItems(templates)
        setupSpanCount()
    }

    private fun setupSpanCount() {
        layoutManager.spanCount = if (adapter.itemCount > 1) 2 else 1
    }

    fun updateUi(uiState: CreateReviewTemplateUiState, continuation: Continuation<Unit>) {
        when (uiState) {
            is CreateReviewTemplateUiState.Loading -> {
                showLoading()
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewTemplateUiState.Showing -> {
                showTemplate(uiState.templates)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewTemplateUiState.Hidden -> {
                animateHide(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
        }
    }

    fun setListener(newCreateReviewTemplateListener: Listener) {
        createReviewTemplateListener.listener = newCreateReviewTemplateListener
    }

    fun setMargins(left: Int = Int.ZERO, top: Int = Int.ZERO, right: Int = Int.ZERO, bottom: Int = Int.ZERO) {
        binding.rvTemplate.setMargin(left, top, right, bottom)
    }

    fun makeWrapContent() {
        updateRootHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private inner class CreateReviewTemplateListener: CreateReviewTemplateItemViewHolder.Listener {
        var listener: Listener? = null

        override fun onTemplateSelected(template: CreateReviewTemplate) {
            listener?.onTemplateSelected(template)
        }
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = ANIMATION_DURATION
                interpolator = AccelerateInterpolator()
            }
        }

        private fun WidgetCreateReviewTemplateBinding.showLoadingLayout() {
            layoutTemplateLoading.show()
        }

        private fun WidgetCreateReviewTemplateBinding.hideLoadingLayout() {
            layoutTemplateLoading.gone()
        }

        private fun WidgetCreateReviewTemplateBinding.showTemplateLayout() {
            rvTemplate.show()
        }

        private fun WidgetCreateReviewTemplateBinding.hideTemplateLayout() {
            rvTemplate.gone()
        }

        private fun WidgetCreateReviewTemplateBinding.beginDelayedTransition() {
            TransitionManager.beginDelayedTransition(root, fadeTransition)
        }

        fun transitionToShowTemplate() {
            with(binding) {
                beginDelayedTransition()
                hideLoadingLayout()
                showTemplateLayout()
            }
        }

        fun transitionToShowLoading() {
            with(binding) {
                beginDelayedTransition()
                hideTemplateLayout()
                showLoadingLayout()
            }
        }
    }

    interface Listener {
        fun onTemplateSelected(template: CreateReviewTemplate)
    }
}
