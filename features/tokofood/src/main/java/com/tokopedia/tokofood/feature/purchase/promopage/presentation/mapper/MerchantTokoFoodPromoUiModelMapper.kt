package com.tokopedia.tokofood.feature.purchase.promopage.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.MerchantPromoListTokoFoodCoupon
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodData
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoEligibilityHeaderUiModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoFragmentUiModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoHeaderUiModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoItemUiModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoTickerUiModel

object MerchantTokoFoodPromoUiModelMapper {

    fun mapResponseDataToVisitables(data: PromoListTokoFoodData): MutableList<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            val isAvailableSectionEnabled = data.availableSection.subSection.coupons.isNotEmpty()
            if (isAvailableSectionEnabled) {
                data.availableSection.title.takeIf { it.isNotEmpty() }?.let { title ->
                    add(TokoFoodPromoEligibilityHeaderUiModel(title = title))
                }
                data.availableSection.subSection.let { availableSubSection ->
                    availableSubSection.title.takeIf { it.isNotBlank() }?.let {
                        add(
                            TokoFoodPromoHeaderUiModel(
                                title = availableSubSection.title,
                                iconUrl = availableSubSection.iconUrl
                            )
                        )
                    }
                    availableSubSection.tickerMessage.takeIf { it.isNotEmpty() }
                        ?.let { tickerMessage ->
                            add(TokoFoodPromoTickerUiModel(tickerMessage))
                        }
                    addAll(
                        availableSubSection.coupons.map {
                            mapPromoItemUiModel(it, availableSubSection.isEnabled)
                        }
                    )
                }
            }

            val isUnavailableSectionEnabled = data.unavailableSection.subSection.coupons.isNotEmpty()
            if (isUnavailableSectionEnabled) {
                data.unavailableSection.title.takeIf { it.isNotEmpty() }?.let { title ->
                    add(TokoFoodPromoEligibilityHeaderUiModel(title = title))
                }
                data.unavailableSection.subSection.let { unavailableSubSection ->
                    unavailableSubSection.title.takeIf { it.isNotBlank() }?.let {
                        add(
                            TokoFoodPromoHeaderUiModel(
                                title = unavailableSubSection.title,
                                iconUrl = unavailableSubSection.iconUrl
                            )
                        )
                    }
                    unavailableSubSection.tickerMessage.takeIf { it.isNotEmpty() }
                        ?.let { tickerMessage ->
                            add(TokoFoodPromoTickerUiModel(tickerMessage))
                        }
                    addAll(
                        unavailableSubSection.coupons.map {
                            mapPromoItemUiModel(it, unavailableSubSection.isEnabled)
                        }
                    )
                }
            }
        }
    }

    fun mapResponseDataToFragmentUiModel(data: PromoListTokoFoodData): TokoFoodPromoFragmentUiModel {
        return TokoFoodPromoFragmentUiModel(
            pageTitle = data.title,
            promoTitle = data.promoSummary.title,
            promoAmountStr = data.promoSummary.totalFmt,
            promoCount = data.availableSection.subSection.coupons.count { it.isSelected }
        )
    }

    private fun mapPromoItemUiModel(coupon: MerchantPromoListTokoFoodCoupon,
                                    isAvailable: Boolean): TokoFoodPromoItemUiModel {
        return TokoFoodPromoItemUiModel(
            isAvailable = isAvailable,
            isSelected = coupon.isSelected,
            highlightWording = if (isAvailable) coupon.topBannerTitle else "",
            title = coupon.title,
            timeValidityWording = coupon.expiryInfo,
            additionalInformation = coupon.additionalInformation
        )
    }
}
