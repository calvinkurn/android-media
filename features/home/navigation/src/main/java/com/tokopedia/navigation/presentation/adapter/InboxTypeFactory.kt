package com.tokopedia.navigation.presentation.adapter

import com.tokopedia.navigation.domain.model.*

/**
 * Author errysuprayogi on 13,March,2019
 */
interface InboxTypeFactory {

    fun type(inbox: Inbox): Int

    fun type(recomendation: Recommendation): Int

    fun type(recomTitle: RecomTitle): Int

    fun type(inboxTopAdsBanner: InboxTopAdsBannerUiModel): Int

    fun type(topadsHeadlineUiModel: TopadsHeadlineUiModel): Int

}
