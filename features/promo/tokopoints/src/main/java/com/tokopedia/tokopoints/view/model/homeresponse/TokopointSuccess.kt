package com.tokopedia.tokopoints.view.model.homeresponse

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopoints.notification.model.PopupNotification
import com.tokopedia.tokopoints.notification.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.RewardTickerListResponse
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.model.usersaving.TokopointsUserSaving

data class TokopointSuccess(
    val topSectionResponse: TopSectionResponse,
    val sectionList: MutableList<SectionContent>,
    val recomData: RewardsRecommendation?
)

data class TopSectionResponse(
    val tokopediaRewardTopSection: TokopediaRewardTopSection,
    val userSavingResponse: TokopointsUserSaving?,
    val rewardTickerResponse: RewardTickerListResponse?,
    var popupNotification: TokoPointDetailEntity?
)

data class RewardsRecommendation(
    val recommendationWrapper: List<RecommendationWrapper>,
    val title: String,
    val appLink: String
)

data class RecommendationWrapper(
    val recomendationItem: RecommendationItem,
    val recomData: ProductCardModel
)