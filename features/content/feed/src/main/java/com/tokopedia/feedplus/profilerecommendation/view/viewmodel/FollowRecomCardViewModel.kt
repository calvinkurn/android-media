package com.tokopedia.feedplus.profilerecommendation.view.viewmodel

import com.tokopedia.feedplus.profilerecommendation.data.AuthorType
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by jegul on 2019-09-13.
 */
data class FollowRecomCardViewModel(
        val header: String?,
        val thumbnailList: List<FollowRecomCardThumbnailViewModel>,
        val avatar: String,
        val title: String,
        val description: String,
        val badgeUrl: String,
        val enabledFollowText: String,
        val disabledFollowText: String,
        val isFollowed: Boolean,
        val textFollowTrue: String,
        val textFollowFalse: String,
        val followInstruction: String,
        val authorId: String,
        val authorType: AuthorType?,
        val impressHolder: ImpressHolder = ImpressHolder()
) : FollowRecomViewModel