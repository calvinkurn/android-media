package com.tokopedia.feedcomponent.view.viewmodel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.adapter.post.DynamicPostTypeFactory
import com.tokopedia.feedcomponent.view.viewmodel.CardTitle

/**
 * @author by milhamj on 20/12/18.
 */
data class FeedRecommendationViewModel (
        val title: CardTitle = CardTitle(),
        val cards: MutableList<RecommendationCardViewModel> = ArrayList()
): Visitable<DynamicPostTypeFactory> {
    override fun type(typeFactory: DynamicPostTypeFactory?): Int {
        return 0
    }
}