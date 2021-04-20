package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_digital.common.util.AnalyticUtils
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageSpotlightModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SPOTLIGHT_IMPRESSION
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemSpotlightAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_spotlight.view.*

class DigitalHomePageSpotlightViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageSpotlightModel>(itemView) {

    override fun bind(element: DigitalHomePageSpotlightModel) {
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        itemView.rv_digital_homepage_spotlight.layoutManager = layoutManager
        if (element.isLoaded) {
            if (element.isSuccess
                    && element.data != null
                    && element.data.section.items.isNotEmpty()) {
                with (element.data.section) {
                    itemView.digital_homepage_spotlight_shimmering.hide()
                    itemView.digital_homepage_spotlight_container.show()
                    itemView.digital_homepage_spotlight_title.text = title

                    itemView.rv_digital_homepage_spotlight.apply {
                        adapter = DigitalItemSpotlightAdapter(items, onItemBindListener)

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
                                        onItemBindListener.onSectionItemImpression(items.subList(indexes.first, indexes.second + 1), SPOTLIGHT_IMPRESSION)
                                    }
                                }
                            }
                        })
                    }
                    onItemBindListener.onSectionItemImpression(items, SPOTLIGHT_IMPRESSION)
                }
            } else {
                itemView.digital_homepage_spotlight_shimmering.hide()
                itemView.digital_homepage_spotlight_container.hide()
            }
        } else {
            itemView.digital_homepage_spotlight_shimmering.show()
            itemView.digital_homepage_spotlight_container.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_spotlight

        val ITEM_DECORATOR_SIZE = com.tokopedia.unifyprinciples.R.dimen.layout_lvl1
    }
}