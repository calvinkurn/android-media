package com.tokopedia.recommendation_widget_common.widget.foryou

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.recomcard.RecomEntityCardViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.recomcard.RecomEntityModel

class ForYouRecommendationTypeFactoryImpl constructor(
    private val listener: RecomEntityCardViewHolder.Listener
) : BaseAdapterTypeFactory(), ForYouRecommendationTypeFactory {

    override fun type(model: RecomEntityModel) = RecomEntityCardViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            RecomEntityCardViewHolder.LAYOUT -> RecomEntityCardViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
