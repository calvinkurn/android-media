package com.tokopedia.kol.model

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendation
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendationData
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampDataUiModel

/**
 * Created by meyta.taliti on 07/08/22.
 */
class ContentDetailModelBuilder {

    fun getContentDetail(content: FeedXCard = FeedXCard()) = ContentDetailRevampDataUiModel(
        postList = listOf(content),
        cursor = "",
    )

    fun getContentRecommendation(
        contents: List<FeedXCard> = emptyList(),
        nextCursor: String = "",
    ) = FeedXPostRecommendation(
        posts = contents,
        nextCursor = nextCursor
    )

    fun getContentRecommendationData(
        postRecommendation: FeedXPostRecommendation
    ) = FeedXPostRecommendationData(
        postRecommendation
    )
}