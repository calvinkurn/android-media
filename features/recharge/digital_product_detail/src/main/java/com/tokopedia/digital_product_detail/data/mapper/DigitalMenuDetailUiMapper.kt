package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.digital_product_detail.data.model.data.DigitalDigiPersoGetPersonalizedItem
import com.tokopedia.digital_product_detail.data.model.data.PersoRecommendationItem
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

@DigitalPDPScope
class DigitalMenuDetailUiMapper @Inject constructor() {

    fun mapMenuDetailModel(data: TopupBillsMenuDetail): MenuDetailModel {
        return MenuDetailModel(
            catalog = data.catalog,
            userPerso = data.userPerso,
            tickers = data.tickers,
            banners = data.banners
        )
    }
}