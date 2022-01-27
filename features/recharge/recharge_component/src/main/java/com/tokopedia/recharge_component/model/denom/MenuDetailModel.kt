package com.tokopedia.recharge_component.model.denom

import com.tokopedia.common.topupbills.data.TopupBillsBanner
import com.tokopedia.common.topupbills.data.TopupBillsCatalog
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.TopupBillsUserPerso
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

data class MenuDetailModel(
    val catalog: TopupBillsCatalog = TopupBillsCatalog(),
    val userPerso: TopupBillsUserPerso = TopupBillsUserPerso(),
    val recommendations: List<RecommendationCardWidgetModel> = listOf(),
    val tickers: List<TopupBillsTicker> = listOf(),
    val banners: List<TopupBillsBanner> = listOf()
)