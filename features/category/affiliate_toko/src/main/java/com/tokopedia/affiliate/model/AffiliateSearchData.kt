package com.tokopedia.affiliate.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AffiliateSearchData(
        @SerializedName("status") val status : Boolean?,
        @SerializedName("cards") val cards : Cards?
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
}