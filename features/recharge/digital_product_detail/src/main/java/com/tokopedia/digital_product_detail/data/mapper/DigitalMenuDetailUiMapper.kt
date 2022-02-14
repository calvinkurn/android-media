package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

@DigitalPDPScope
class DigitalMenuDetailUiMapper @Inject constructor() {

    fun mapMenuDetailModel(data: TopupBillsMenuDetail, isBigRecommendation: Boolean = false): MenuDetailModel {
        return MenuDetailModel(
            catalog = data.catalog,
            userPerso = data.userPerso,
            recommendations = data.recommendations.map {
                mapTopUpBillsToRecommendation(it, isBigRecommendation)
            },
            tickers = data.tickers,
            banners = data.banners
        )
    }

    private fun mapTopUpBillsToRecommendation(topupBillsRecommendation: TopupBillsRecommendation, isBigRecommendation: Boolean): RecommendationCardWidgetModel {
        return topupBillsRecommendation.let {
            RecommendationCardWidgetModel(
                if (isBigRecommendation) RecommendationCardEnum.BIG else RecommendationCardEnum.SMALL,
                imageUrl = it.iconUrl,
                clientNumber = it.clientNumber,
                title = if (it.description.isEmpty()) it.clientNumber else it.description,
                price = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productPrice, false),
                appUrl = it.applink,
                id = it.productId.toString(),
                categoryId = it.categoryId.toString(),
                productId = it.productId.toString(),
                operatorId = it.operatorId.toString(),
                productType = if (isBigRecommendation) "30GB" else "", //todo get data from gql
                productExpired = if (isBigRecommendation) "30 Days" else "", //todo get data from gql
            )
        }
    }
}