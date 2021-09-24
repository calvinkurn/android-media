package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeSubscriptionBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageSubscriptionModel
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemSubscriptionAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DigitalHomePageSubscriptionViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageSubscriptionModel>(itemView) {

    override fun bind(element: DigitalHomePageSubscriptionModel) {
        val bind = LayoutDigitalHomeSubscriptionBinding.bind(itemView)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        bind.rvDigitalHomepageSubscription.layoutManager = layoutManager
        if (element.isLoaded) {
            if (element.isSuccess
                    && element.data != null
                    && element.data.section.items.isNotEmpty()) {
                with (element.data.section) {
                    bind.digitalHomepageSubscriptionShimmering.hide()
                    bind.rvDigitalHomepageSubscription.show()

                    bind.rvDigitalHomepageSubscription.apply {
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
                bind.digitalHomepageSubscriptionShimmering.hide()
                bind.rvDigitalHomepageSubscription.hide()
            }
        } else {
            bind.digitalHomepageSubscriptionShimmering.show()
            bind.rvDigitalHomepageSubscription.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_subscription

        val ITEM_DECORATOR_SIZE = com.tokopedia.unifyprinciples.R.dimen.layout_lvl1
    }
}