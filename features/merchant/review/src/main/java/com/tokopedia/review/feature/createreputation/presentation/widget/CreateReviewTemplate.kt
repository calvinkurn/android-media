package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetCreateReviewTemplateBinding
import com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewTemplateAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewTemplateTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewItemAnimator
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.old.CreateReviewBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewTemplateItemUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTemplateUiState
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewTemplateItemViewHolder

class CreateReviewTemplate @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCreateReviewCustomView<WidgetCreateReviewTemplateBinding>(context, attrs, defStyleAttr) {

    private val createReviewTemplateListener = CreateReviewTemplateListener()
    private val innerBaseCreateReviewCustomViewListener = BaseCreateReviewCustomViewListener()
    private val layoutManager = StaggeredGridLayoutManager(CreateReviewBottomSheet.TEMPLATES_ROW_COUNT, RecyclerView.HORIZONTAL)
    private val itemAnimator = CreateReviewItemAnimator()
    private val typeFactory = CreateReviewTemplateTypeFactory(createReviewTemplateListener, innerBaseCreateReviewCustomViewListener)
    private val adapter = CreateReviewTemplateAdapter(typeFactory)
    private val gridLayoutManagerSpanCountChanger = Runnable { setupSpanCount() }
    private val waitForRvAnimationToFinish = Runnable { waitForRvAnimationToFinish() }

    override val binding = WidgetCreateReviewTemplateBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.layoutTemplate.root.layoutManager = layoutManager
        binding.layoutTemplate.root.itemAnimator = itemAnimator
        binding.layoutTemplate.root.adapter = adapter
    }

    private fun showLoading() {
        binding.layoutTemplateLoading.show()
    }

    private fun showTemplate(templates: List<CreateReviewTemplateItemUiModel>) {
        binding.layoutTemplateLoading.gone()
        setupTemplate(templates)
    }

    private fun hideTemplate() {
        binding.layoutTemplateLoading.gone()
    }

    private fun setupTemplate(templates: List<CreateReviewTemplateItemUiModel>) {
        createReviewTemplateListener.listener?.onStartAnimatingTemplates()
        adapter.updateItems(templates)
        Handler(Looper.getMainLooper()).post(gridLayoutManagerSpanCountChanger)
        Handler(Looper.getMainLooper()).post(waitForRvAnimationToFinish)
    }

    private fun setupSpanCount() {
        layoutManager.spanCount = if (adapter.itemCount > 1) 2 else 1
    }

    /**
     * To check whether the recyclerview is still animating it's item, use this to prevent
     * updating recyclerview too rapidly since it will make the animation looks like a glitch
     */
    private fun waitForRvAnimationToFinish() {
        if (itemAnimator.isRunning) {
            itemAnimator.isRunning {
                Handler(Looper.getMainLooper()).post(waitForRvAnimationToFinish)
            }
            return
        }
        createReviewTemplateListener.listener?.onFinishAnimatingTemplates()
    }

    fun updateUi(uiState: CreateReviewTemplateUiState) {
        when (uiState) {
            is CreateReviewTemplateUiState.Changing -> {
                showTemplate(uiState.templates)
                animateShow()
            }
            is CreateReviewTemplateUiState.Loading -> {
                showLoading()
                animateShow()
            }
            is CreateReviewTemplateUiState.Showing -> {
                showTemplate(uiState.templates)
                animateShow()
            }
            is CreateReviewTemplateUiState.Hidden -> {
                hideTemplate()
                animateHide()
            }
        }
    }

    fun setListener(newCreateReviewTemplateListener: Listener) {
        createReviewTemplateListener.listener = newCreateReviewTemplateListener
    }

    private inner class CreateReviewTemplateListener: CreateReviewTemplateItemViewHolder.Listener {
        var listener: Listener? = null

        override fun onTemplateSelected(template: CreateReviewTemplate) {
            listener?.onTemplateSelected(template)
        }
    }

    private inner class BaseCreateReviewCustomViewListener: BaseCreateReviewCustomView.Listener {
        var listener: BaseCreateReviewCustomView.Listener? = null

        override fun onRequestClearTextAreaFocus() {
            listener?.onRequestClearTextAreaFocus()
        }

        override fun onRequestTextAreaFocus() {
            listener?.onRequestTextAreaFocus()
        }
    }

    interface Listener {
        fun onTemplateSelected(template: CreateReviewTemplate)
        fun onStartAnimatingTemplates()
        fun onFinishAnimatingTemplates()
    }
}