package com.tokopedia.buyerorder.detail.revamp.adapter

import android.view.View
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.detail.data.ItemsDeals
import com.tokopedia.buyerorder.detail.data.ItemsDealsOMP
import com.tokopedia.buyerorder.detail.data.ItemsDealsShort
import com.tokopedia.buyerorder.detail.data.ItemsDefault
import com.tokopedia.buyerorder.detail.data.ItemsEvents
import com.tokopedia.buyerorder.detail.data.ItemsInsurance
import com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder.DealsOMPViewHolder
import com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder.DealsShortViewHolder
import com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder.DealsViewHolder
import com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder.DefaultViewHolder
import com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder.EventsViewHolder
import com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder.InsuranceViewHolder

/**
 * created by @bayazidnasir on 23/8/2022
 */

class OrderDetailTypeFactoryImpl constructor(
    private val gson: Gson,
    private val listener: EventDetailsListener,
): BaseAdapterTypeFactory(), OrderDetailTypeFactory {

    override fun type(item: ItemsDealsShort): Int {
        return DealsShortViewHolder.LAYOUT
    }

    override fun type(item: ItemsDealsOMP): Int {
        return DealsOMPViewHolder.LAYOUT
    }

    override fun type(item: ItemsDeals): Int {
        return DealsViewHolder.LAYOUT
    }

    override fun type(item: ItemsEvents): Int {
        return EventsViewHolder.LAYOUT
    }

    override fun type(item: ItemsInsurance): Int {
        return InsuranceViewHolder.LAYOUT
    }

    override fun type(item: ItemsDefault): Int {
        return DefaultViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            DefaultViewHolder.LAYOUT -> DefaultViewHolder(parent, gson, listener)
            InsuranceViewHolder.LAYOUT -> InsuranceViewHolder(parent, gson, listener)
            EventsViewHolder.LAYOUT -> EventsViewHolder(parent, gson, listener)
            DealsShortViewHolder.LAYOUT -> DealsShortViewHolder(parent, gson)
            DealsOMPViewHolder.LAYOUT -> DealsOMPViewHolder(parent, gson, listener)
            DealsViewHolder.LAYOUT -> DealsViewHolder(parent, gson, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}