package com.tokopedia.feedcomponent.view.viewmodel.recommendation

import com.tokopedia.feedcomponent.view.viewmodel.CardTitle

/**
 * @author by milhamj on 20/12/18.
 */
data class FeedRecommendationViewModel (
        val title: CardTitle = CardTitle(),
        val cards: MutableList<RecommendationCardViewModel> = ArrayList()
)