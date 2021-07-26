package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactory

/**
 * Created by yfsx on 5/3/21.
 */
class RecommendationCarouselAdapter (items: List<Visitable<*>>,
                                     typeFactory: CommonRecomCarouselCardTypeFactory)
    : BaseAdapter<CommonRecomCarouselCardTypeFactory>(typeFactory, items){

    val data: List<Visitable<*>>
        get() = visitables

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

}