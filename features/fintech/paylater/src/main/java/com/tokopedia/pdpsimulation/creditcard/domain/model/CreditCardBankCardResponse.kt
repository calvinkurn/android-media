package com.tokopedia.pdpsimulation.creditcard.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class CreditCardBankCardResponse(
        @SerializedName("cc_fetchbankcardlist")
        val creditCardBankData: CreditCardBankData,
)

data class CreditCardBankData(
        @SerializedName("data")
        val bankCardList: ArrayList<BankCardListItem>?,
)

data class BankCardListItem(
        @SerializedName("bank_name")
        val bankName: String?,
        @SerializedName("bank_slug")
        val bankSlug: String?,
        @SerializedName("bank_logo_url")
        val bankLogoUrl: String?,
        @SerializedName("bank_pdp_info")
        val bankPdpInfo: String?,
        @SerializedName("is_promo")
        val isPromo: Boolean?,
        @SerializedName("card_list")
        val cardList: ArrayList<CreditCardItem>?,
)

@Parcelize
data class CreditCardItem(
        @SerializedName("card_name")
        val cardName: String?,
        @SerializedName("card_slug")
        val cardSlug: String?,
        @SerializedName("card_image_url")
        val cardImageUrl: String?,
        @SerializedName("special_label")
        val specialLabel: String?,
        @SerializedName("main_benefit")
        val mainBenefit: String?,
        @SerializedName("is_special_offer")
        val isSpecialOffer: Boolean?,
) : Parcelable
