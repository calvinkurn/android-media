package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageSubscriptionModel
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemSubscriptionAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_subscription.view.*

class DigitalHomePageSubscriptionViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageSubscriptionModel>(itemView) {

    override fun bind(element: DigitalHomePageSubscriptionModel) {
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        itemView.rv_digital_homepage_subscription.layoutManager = layoutManager
        if (element.isLoaded) {
            if (element.isSuccess
                    && element.data != null
                    && element.data.section.items.isNotEmpty()) {
                with (element.data.section) {
                    itemView.digital_homepage_subscription_shimmering.hide()
                    itemView.rv_digital_homepage_subscription.show()

                    itemView.rv_digital_homepage_subscription.apply {
                        adapter = DigitalItemSubscriptionAdapter(items, onItemBindListener)
                        while (itemDecorationCount > 0) removeItemDecorationAt(0)
                        addItemDecoration(object : RecyclerView.ItemDecoration() {
                            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                                super.getItemOffsets(outRect, view, parent, state)
                                if (parent.getChildAdapterPosition(view) < items.size - 1) {
                                    outRect.right = itemView.context.resources.getDimension(ITEM_DECORATOR_SIZE).toInt()
                                }
                            }
                        })
                    }
                }
            } else {
                itemView.digital_homepage_subscription_shimmering.hide()
                itemView.rv_digital_homepage_subscription.hide()
            }
        } else {
            itemView.digital_homepage_subscription_shimmering.show()
            itemView.rv_digital_homepage_subscription.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_subscription

        val ITEM_DECORATOR_SIZE = com.tokopedia.unifyprinciples.R.dimen.layout_lvl1
    }
}