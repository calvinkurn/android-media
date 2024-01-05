package com.tokopedia.homenav.mainnav.view.adapter.viewholder.review

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderReviewListBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.ReviewTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.NavOrderSpacingDecoration
import com.tokopedia.homenav.mainnav.view.datamodel.review.EmptyStateReviewDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.OtherReviewModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Frenzel on 18/04/22
 */

@MePage(MePage.Widget.REVIEW)
class ReviewViewHolder(itemView: View,
                       val mainNavListener: MainNavListener
): AbstractViewHolder<ReviewListDataModel>(itemView) {
    private var binding: HolderReviewListBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_review_list
        private const val EDGE_MARGIN = 12f
        private const val SPACING_BETWEEN = 0f
        private const val MAX_CARD_HEIGHT = 80f
    }

    override fun bind(element: ReviewListDataModel) {
        val context = itemView.context
        val adapter = BaseAdapter(ReviewTypeFactoryImpl(mainNavListener))

        val edgeMargin = EDGE_MARGIN.toDpInt()
        val spacingBetween = SPACING_BETWEEN.toDpInt()

        binding?.reviewRv?.adapter = adapter
        binding?.reviewRv?.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        if (binding?.reviewRv?.itemDecorationCount == 0) {
            binding?.reviewRv?.addItemDecoration(
                NavOrderSpacingDecoration(spacingBetween, edgeMargin)
            )
        }
        val visitableList = mutableListOf<Visitable<*>>()
        visitableList.addAll(element.reviewList.mapIndexed { index, it -> ReviewModel(it, index) })
        visitableList.add(OtherReviewModel())
        binding?.reviewRv?.setHeightBasedOnCardMaxHeight()
        adapter.setVisitables(visitableList)
    }

    private fun RecyclerView.setHeightBasedOnCardMaxHeight() {
        val productCardHeight = MAX_CARD_HEIGHT.toDpInt()

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }
}
