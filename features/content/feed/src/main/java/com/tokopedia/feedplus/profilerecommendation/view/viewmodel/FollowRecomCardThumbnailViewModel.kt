package com.tokopedia.feedplus.profilerecommendation.view.viewmodel

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by jegul on 2019-09-19.
 */
data class FollowRecomCardThumbnailViewModel(
        val id: String,
        val url: String,
        val impressHolder: ImpressHolder = ImpressHolder()
)