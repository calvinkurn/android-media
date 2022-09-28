package com.tokopedia.search.result.product.samesessionrecommendation

import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultSameSessionRecommendationLayoutBinding
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ProductItemDecoration
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback.FeedbackItem
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class SameSessionRecommendationViewHolder(
    view: View,
    private val inspirationCarouselListener: InspirationCarouselListener,
    private val sameSessionRecommendationListener: SameSessionRecommendationListener,
) : AbstractViewHolder<SameSessionRecommendationDataView>(view) {
    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_same_session_recommendation_layout
    }

    private var binding: SearchResultSameSessionRecommendationLayoutBinding? by viewBinding()
    private val context: Context?
        get() = itemView.context

    private var recommendationDataView: SameSessionRecommendationDataView? = null

    init {
        setUpCloseButtonListener()
        setUpUndoButtonListener()
    }

    override fun bind(element: SameSessionRecommendationDataView) {
        val binding = binding ?: return
        recommendationDataView = element
        binding.tgRecommendationTitle.text = element.title
        bindProductCarousel(element.products)
        itemView.addOnImpressionListener(element) {
            sameSessionRecommendationListener.onSameSessionRecommendationImpressed(element)
        }
    }

    private fun createProductItemDecoration(): ProductItemDecoration =
        ProductItemDecoration(getSpacing())

    private fun getSpacing(): Int = context
        ?.resources
        ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
        ?: 0

    private fun bindProductCarousel(products: List<InspirationCarouselDataView.Option.Product>) {
        val binding = binding ?: return
        binding.rvSameSessionRecommendationProduct.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addItemDecorationIfNotExists(createProductItemDecoration())
            val typeFactory = SameSessionRecommendationProductTypeFactory(
                inspirationCarouselListener
            )
            adapter = SameSessionRecommendationProductAdapter(typeFactory).apply {
                clearData()
                addAll(products)
            }
        }
    }

    private fun bindFeedback(feedback: SameSessionRecommendationDataView.Feedback) {
        val binding = binding ?: return
        binding.tgHideRecommendation.text = feedback.title
        binding.rvSameSessionRecommendationFeedback.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecorationIfNotExists(createFeedbackItemDecoration())
            adapter = SameSessionRecommendationFeedbackAdapter(
                sameSessionRecommendationListener,
                this@SameSessionRecommendationViewHolder::onFeedbackItemClicked,
            ).apply {
                submitList(feedback.items)
            }
        }
    }

    private fun setUpCloseButtonListener() {
        binding?.btnClose?.setOnClickListener {
            onCloseButtonClicked()
        }
    }

    private fun onCloseButtonClicked() {
        val binding = binding ?: return
        val data = recommendationDataView ?: return
        binding.groupSameSessionRecommendationItem.hide()
        binding.groupSameSessionRecommendationFeedback.visible()
        bindFeedback(data.feedback)
    }

    private fun setUpUndoButtonListener() {
        binding?.btnUndo?.setOnClickListener {
            onUndoButtonClicked()
        }
    }

    private fun onUndoButtonClicked() {
        val binding = binding ?: return
        val data = recommendationDataView ?: return
        binding.groupSameSessionRecommendationFeedback.hide()
        binding.groupSameSessionRecommendationItem.visible()
        bindProductCarousel(data.products)
    }

    override fun onViewRecycled() {
        recommendationDataView = null
        super.onViewRecycled()
    }

    private fun createFeedbackItemDecoration(): RecyclerView.ItemDecoration {
        return FeedbackItemDecoration(getSpacing(), itemView.context)
    }

    private class FeedbackItemDecoration(
        private val left: Int,
        context: Context,
    ) : RecyclerView.ItemDecoration() {
        private val divider =
            context.getDrawable(R.drawable.search_divider_same_session_recommendation_feedback)

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)
            val divider = divider ?: return
            val childCount = parent.childCount

            val right = parent.width - parent.paddingRight
            val onePx = 1.toPx()
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin - onePx
                val bottom = top + onePx
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }
    }

    private fun onFeedbackItemClicked(feedbackItem: FeedbackItem) {
        sameSessionRecommendationListener.onSameSessionRecommendationFeedbackItemClicked(
            feedbackItem
        )
        bindSelectedFeedbackItem(feedbackItem)
    }

    private fun bindSelectedFeedbackItem(feedbackItem: FeedbackItem) {
        val binding = binding ?: return
        binding.tgSelectedFeedbackTitle.text = feedbackItem.titleOnClick
        binding.tgSelectedFeedbackBody.text = feedbackItem.messageOnClick
        binding.groupSameSessionRecommendationFeedback.hide()
        binding.groupSameSessionRecommendationSelectedFeedback.visible()
    }

}
