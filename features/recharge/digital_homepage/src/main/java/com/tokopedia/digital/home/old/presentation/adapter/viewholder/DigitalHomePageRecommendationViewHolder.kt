package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_digital.common.util.AnalyticUtils
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageRecommendationModel
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemRecommendationAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_recommendation.view.*

class DigitalHomePageRecommendationViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageRecommendationModel>(itemView) {

    override fun bind(element: DigitalHomePageRecommendationModel) {
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        itemView.rv_digital_homepage_recommendation.layoutManager = layoutManager
        if (element.isLoaded) {
            if (element.isSuccess && element.data.recommendationItemEntityList.isNotEmpty()) {
                element.data.run {
                    val items = recommendationItemEntityList
                    itemView.digital_homepage_recommendation_shimmering.hide()
                    itemView.digital_homepage_recommendation_container.show()
                    itemView.digital_homepage_recommendation_title.text = title

                    itemView.rv_digital_homepage_recommendation.apply {
                        adapter = DigitalItemRecommendationAdapter(items, onItemBindListener)

                        while (itemDecorationCount > 0) removeItemDecorationAt(0)
                        addItemDecoration(object : RecyclerView.ItemDecoration() {
                            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                                super.getItemOffsets(outRect, view, parent, state)
                                // Add offset to all items except the last one
                                if (parent.getChildAdapterPosition(view) < items.size - 1) {
                                    outRect.right = itemView.context.resources.getDimension(ITEM_DECORATOR_SIZE).toInt()
                                }
                            }
                        })

                        clearOnScrollListeners()
                        addOnScrollListener(object: RecyclerView.OnScrollListener() {
                            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    val indexes = AnalyticUtils.getVisibleItemIndexes(this@apply)
                                    if (AnalyticUtils.hasVisibleItems(indexes)) {
                                        onItemBindListener.onRecommendationImpression(items.subList(indexes.first, indexes.second + 1))
                                    }
                                }
                            }
                        })
                    }
                    onItemBindListener.onRecommendationImpression(items)
                }
            } else {
                itemView.digital_homepage_recommendation_shimmering.hide()
                itemView.digital_homepage_recommendation_container.hide()
            }
        } else {
            itemView.digital_homepage_recommendation_shimmering.show()
            itemView.digital_homepage_recommendation_container.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_recommendation

        val ITEM_DECORATOR_SIZE = com.tokopedia.unifyprinciples.R.dimen.layout_lvl1
    }
}