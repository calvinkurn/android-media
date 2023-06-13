package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.CodDataPromo
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.EstimatedTimeArrivalPromo
import kotlinx.parcelize.Parcelize

/**
 * Created by fajarnuha on 29/03/19.
 */
@Parcelize
data class LogisticPromoUiModel(
    val promoCode: String = "",
    val title: String = "",
    val description: String = "",
    val shipperName: String = "",
    val serviceId: Int = 0,
    val shipperId: Int = 0,
    val shipperProductId: Int = 0,
    val shipperDesc: String = "",
    val disableText: String = "",
    val dialogMsg: String = "",
    val isApplied: Boolean = false,
    val imageUrl: String = "",
    val discountedRate: Int = 0,
    val shippingRate: Int = 0,
    val benefitAmount: Int = 0,
    val disabled: Boolean = false,
    val hideShipperName: Boolean = false,
    val codData: CodDataPromo = CodDataPromo(),
    val etaData: EstimatedTimeArrivalPromo = EstimatedTimeArrivalPromo(),
    val freeShippingItemTitle: String = "",
    val freeShippingChosenCourierTitle: String = "",
    val tickerAvailableFreeShippingCourierTitle: String = "",
    val isBebasOngkirExtra: Boolean = false,
    val bottomSheetDescription: String = "",
    val promoMessage: String = "",
    val tickerDescriptionPromoAdjusted: String = "",
    val freeShippingMetadata: String = "",
    val benefitClass: String = "",
    val shippingSubsidy: Long = 0,
    val boCampaignId: Long = 0
) : RatesViewModelType, Parcelable
