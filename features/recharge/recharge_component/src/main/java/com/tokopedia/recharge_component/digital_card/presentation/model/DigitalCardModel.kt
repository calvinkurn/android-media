package com.tokopedia.recharge_component.digital_card.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DigitalCardModel(
    val mediaUrl: String,
    val mediaType: MediaType,
    val mediaTitle: String,
    val iconUrl: String,
    val campaign: DigitalCardCampaignModel,
    val productInfoLeft: DigitalCardInfoModel,
    val productInfoRight: DigitalCardInfoModel,
    val title: String,
    val rating: DigitalCardRatingModel,
    val specialInfo: DigitalCardInfoModel,
    val priceData: DigitalCardPriceModel,
    val subtitle: String,
    val soldPercentage: DigitalCardSoldPercentageModel,
    val actionButton: DigitalCardActionModel
) : Parcelable

@Parcelize
data class DigitalCardCampaignModel(
    val text: String,
    val textColor: String,
    val backgroundUrl: String
) : Parcelable

@Parcelize
data class DigitalCardInfoModel(
    val text: String,
    val textColor: String
) : Parcelable

@Parcelize
data class DigitalCardRatingModel(
    val ratingType: RatingType,
    val rating: Double,
    val review: String
) : Parcelable

@Parcelize
data class DigitalCardPriceModel(
    val price: String,
    val discount: String,
    val slashedPrice: String
) : Parcelable

@Parcelize
data class DigitalCardSoldPercentageModel(
    val showPercentage: Boolean,
    val value: Int,
    val label: String,
    val labelColor: String
) : Parcelable

@Parcelize
data class DigitalCardActionModel(
    val text: String,
    val textColor: String,
    val applink: String
) : Parcelable