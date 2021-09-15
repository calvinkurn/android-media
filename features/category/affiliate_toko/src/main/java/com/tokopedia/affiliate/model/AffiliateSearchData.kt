package com.tokopedia.affiliate.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AffiliateSearchData(
        @SerializedName("status") val status : Boolean?,
        @SerializedName("cards") val cards : Cards?,
        @SerializedName("error") val error : Error?
) {

    data class Cards (
            @SerializedName("id") val id : String?,
            @SerializedName("has_more") val has_more : Boolean?,
            @SerializedName("title") val title : String?,
            @SerializedName("items") val items : List<Items>?
    ){
        data class Items (
                @SerializedName("title") val title : String?,
                @SerializedName("image") val image : Image?,
                @SerializedName("additionalInformation") val additionalInformation : List<AdditionalInformation>?,
                @SerializedName("commission") val commission : Commission?,
                @SerializedName("footer") val footer : List<Footer>?,
                @SerializedName("rating") val rating : Double?,
                @SerializedName("status") val status : Status?
        ){

            data class Image (
                    @SerializedName("desktop") val desktop : String?,
                    @SerializedName("mobile") val mobile : String?,
                    @SerializedName("ios") val ios : String?,
                    @SerializedName("android") val android : String?
            )

            data class Commission (
                    @SerializedName("amountFormatted") val amountFormatted : String?,
                    @SerializedName("amount") val amount : Int?,
                    @SerializedName("percentageFormatted") val percentageFormatted : String?,
                    @SerializedName("percentage") val percentage : Int?
            )

            data class AdditionalInformation (
                    @SerializedName("htmlText") val htmlText : String?,
                    @SerializedName("type") val type : Int?,
                    @SerializedName("color") val color : String?
            ){
                override fun equals(other: Any?): Boolean {
                    return other is AdditionalInformation && type == other.type
                }

                override fun hashCode(): Int {
                    return type ?: super.hashCode()
                }
            }

            data class Footer (
                    @SerializedName("footerType") val footerType : Int?,
                    @SerializedName("footerIcon") val footerIcon : String?,
                    @SerializedName("footerText") val footerText : String?
            ){
                override fun equals(other: Any?): Boolean {
                    return other is Footer && footerType == other.footerType
                }

                override fun hashCode(): Int {
                    return footerType ?: super.hashCode()
                }
            }

            data class Status (
                    @SerializedName("isLinkGenerationAllowed") val isLinkGenerationAllowed : Boolean?
            )
        }
    }

    data class Error (
            @SerializedName("error_type") var error_type : Int?,
            @SerializedName("title") var title : String?,
            @SerializedName("message") var message : String?,
            @SerializedName("error_cta") val error_cta : List<ErrorCta>?
    ){
        data class ErrorCta (
                @SerializedName("cta_text") val cta_text : String?,
                @SerializedName("cta_type") val cta_type : Int?,
                @SerializedName("cta_action") val cta_action : Int?,
                @SerializedName("cta_image") val cta_image : Cards.Items.Image?,
                @SerializedName("cta_link") val cta_link : CtaLink?,
        ){

            data class CtaLink (
                    @SerializedName("desktop") val desktop : String?,
                    @SerializedName("mobile") val mobile : String?,
                    @SerializedName("ios") val ios : String?,
                    @SerializedName("android") val android : String?
            )
        }
    }
}