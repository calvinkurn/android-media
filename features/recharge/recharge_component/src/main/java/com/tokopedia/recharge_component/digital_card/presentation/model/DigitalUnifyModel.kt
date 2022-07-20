package com.tokopedia.recharge_component.digital_card.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recharge_component.digital_card.presentation.adapter.DigitalUnifyCardTypeFactory
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DigitalUnifyModel(
    val id: String,
    val mediaUrl: String,
    val mediaType: String,
    val mediaTitle: String,
    val iconUrl: String,
    val iconBackgroundColor: String,
    val campaign: DigitalCardCampaignModel,
    val productInfoLeft: DigitalCardInfoModel,
    val productInfoRight: DigitalCardInfoModel,
    val title: String,
    val rating: DigitalCardRatingModel,
    val specialInfo: DigitalCardInfoModel,
    val priceData: DigitalCardPriceModel,
    val cashback: String,
    val subtitle: String,
    val soldPercentage: DigitalCardSoldPercentageModel,
    val actionButton: DigitalCardActionModel
) : Visitable<DigitalUnifyCardTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: DigitalUnifyCardTypeFactory?): Int =
        typeFactory?.type(this).orZero()

    fun getMediaTypeRatio(): String =
        when (mediaType) {
            DigitalUnifyConst.MEDIA_TYPE_RECTANGLE -> DigitalUnifyConst.MEDIA_TYPE_RECTANGLE_RATIO
            DigitalUnifyConst.MEDIA_TYPE_SQUARE -> DigitalUnifyConst.MEDIA_TYPE_SQUARE_RATIO
            else -> DigitalUnifyConst.MEDIA_TYPE_RECTANGLE_RATIO
        }
}

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
    val ratingType: String?,
    val rating: Double,
    val review: String
) : Parcelable

@Parcelize
data class DigitalCardPriceModel(
    val price: String,
    val discountLabel: String,
    val discountType: String,
    val discountLabelType: Int,
    val slashedPrice: String,
    val pricePrefix: String,
    val priceSuffix: String
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
    val buttonType: String,
    val applink: String
) : Parcelable {
    fun getUnifyButtonType(): Int =
        when (buttonType) {
            DigitalUnifyConst.CTA_BUTTON_TYPE_ENABLE -> UnifyButton.Type.MAIN
            DigitalUnifyConst.CTA_BUTTON_TYPE_DISABLE -> UnifyButton.Type.ALTERNATE
            else -> UnifyButton.Type.MAIN
        }
}