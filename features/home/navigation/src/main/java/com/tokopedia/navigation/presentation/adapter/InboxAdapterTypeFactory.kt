package com.tokopedia.navigation.presentation.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.domain.model.Inbox
import com.tokopedia.navigation.domain.model.RecomTitle
import com.tokopedia.navigation.domain.model.Recommendation
import com.tokopedia.navigation.presentation.adapter.viewholder.InboxViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.RecomTitleViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.RecommendationViewHolder
import com.tokopedia.navigation.presentation.view.InboxAdapterListener
import com.tokopedia.navigation.presentation.view.listener.RecommendationListener

/**
 * Author errysuprayogi on 13,March,2019
 */
class InboxAdapterTypeFactory(private val listener: InboxAdapterListener, private val recommendationListener: RecommendationListener) : BaseAdapterTypeFactory(), InboxTypeFactory {

    override fun type(inbox: Inbox): Int {
        return InboxViewHolder.LAYOUT
    }

    override fun type(recomendation: Recommendation): Int {
        return RecommendationViewHolder.LAYOUT
    }

    override fun type(recomTitle: RecomTitle): Int {
        return RecomTitleViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            InboxViewHolder.LAYOUT -> viewHolder = InboxViewHolder(view, listener)
            RecommendationViewHolder.LAYOUT -> viewHolder = RecommendationViewHolder(view, listener, recommendationListener)
            RecomTitleViewHolder.LAYOUT -> viewHolder = RecomTitleViewHolder(view)
            else -> viewHolder = super.createViewHolder(view, type)
        }
        return viewHolder
    }
}
