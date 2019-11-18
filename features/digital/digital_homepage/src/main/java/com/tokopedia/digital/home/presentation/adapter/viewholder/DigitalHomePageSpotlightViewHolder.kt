package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePageSpotlightModel
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SPOTLIGHT_IMPRESSION
import com.tokopedia.digital.home.presentation.adapter.adapter.DigitalItemSpotlightAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_spotlight.view.*

class DigitalHomePageSpotlightViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageSpotlightModel>(itemView) {

    override fun bind(element: DigitalHomePageSpotlightModel) {
        val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        itemView.rv_digital_homepage_spotlight.layoutManager = layoutManager
        if (element.isLoaded) {
            element.data?.section?.run {
                onItemBindListener.onSectionItemImpression(SPOTLIGHT_IMPRESSION)
                itemView.digital_homepage_spotlight_shimmering.hide()
                itemView.digital_homepage_spotlight_container.show()
                itemView.digital_homepage_spotlight_title.text = title

                itemView.rv_digital_homepage_spotlight.apply {
                    adapter = DigitalItemSpotlightAdapter(items, onItemBindListener)
                    while (itemDecorationCount > 0) removeItemDecorationAt(0)
                    addItemDecoration(object: RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                            super.getItemOffsets(outRect, view, parent, state)
                            // Add offset to all items except the last one
                            if (parent.getChildAdapterPosition(view) < items.size - 1) {
                                outRect.right = itemView.context.resources.getDimension(ITEM_DECORATOR_SIZE).toInt()
                            }
                        }
                    })
                }
            }
        } else {
            itemView.digital_homepage_spotlight_shimmering.show()
            itemView.digital_homepage_spotlight_container.hide()
            onItemBindListener.onSpotlightItemDigitalBind(element.isLoadFromCloud)
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_spotlight

        val ITEM_DECORATOR_SIZE = R.dimen.dp_8
    }
}