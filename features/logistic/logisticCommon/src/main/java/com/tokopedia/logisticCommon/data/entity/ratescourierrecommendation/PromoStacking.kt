package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by fajarnuha on 01/04/19.
 */
data class PromoStacking(
    @SerializedName("is_promo")
    val isPromo: Int = 0,

    @SerializedName("promo_code")
    val promoCode: String = "",

    @SerializedName("title")
    val title: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("service_id")
    val serviceId: Int = 0,

    @SuppressLint("Invalid Data Type")
    @SerializedName("shipper_id")
    val shipperId: Int = 0,

    @SuppressLint("Invalid Data Type")
    @SerializedName("shipper_product_id")
    val shipperProductId: Int = 0,

    @SerializedName("shipper_name")
    val shipperName: String = "",

    @SerializedName("shipper_desc")
    val shipperDesc: String = "",

    @SerializedName("promo_detail")
    val promoDetail: String = "",

    @SerializedName("benefit_desc")
    val benefitDesc: String = "",

    @SerializedName("promo_tnc_html")
    val promoTncHtml: String = "",

    @SerializedName("shipper_disable_text")
    val shipperDisableText: String = "",

    @SerializedName("is_applied")
    val isApplied: Int = 0,

    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("image_url_chosen")
    val imageUrlChosen: String = "",

    @SerializedName("discounted_rate")
    val discontedRate: Int = 0,

    @SerializedName("shipping_rate")
    val shippingRate: Int = 0,

    @SerializedName("benefit_amount")
    val benefitAmount: Int = 0,

    @SerializedName("disabled")
    val isDisabled: Boolean = false,

    @SerializedName("hide_shipper_name")
    val isHideShipperName: Boolean = false,

    @SerializedName("cod")
    val cod: CodDataPromo = CodDataPromo(),

    @SerializedName("eta")
    val eta: EstimatedTimeArrivalPromo = EstimatedTimeArrivalPromo(),

    @SerializedName("is_bebas_ongkir_extra")
    val isBebasOngkirExtra: Boolean = false,

    @SerializedName("texts")
    val texts: Texts = Texts(),

    @SerializedName("free_shipping_metadata")
    val freeShippingMetadata: FreeShippingMetadata = FreeShippingMetadata(),

    @SuppressLint("Invalid Data Type")
    @SerializedName("bo_campaign_id")
    val boCampaignId: Long = 0,

    @SerializedName("quota_message")
    val quotaMessage: String = ""
)
