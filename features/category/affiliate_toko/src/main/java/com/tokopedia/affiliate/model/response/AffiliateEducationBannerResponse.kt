package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateEducationBannerResponse(
    @SerializedName("dynamicBanner")
    val dynamicBanner: DynamicBanner? = null

) {
    data class DynamicBanner(
        @SerializedName("data")
        val data: BannerData? = null

    ) {
        data class BannerData(

            @SerializedName("banners")
            val banners: List<BannersItem?>? = null,

            @SerializedName("status")
            val status: String? = null
        ) {
            data class BannersItem(
                @SerializedName("sequence")
                val sequence: Int? = null,

                @SerializedName("media_type")
                val mediaType: String? = null,

                @SerializedName("title_color")
                val titleColor: String? = null,

                @SerializedName("description")
                val description: String? = null,

                @SerializedName("attributes")
                val attributes: Attributes? = null,

                @SerializedName("text")
                val text: Text? = null,

                @SerializedName("media")
                val media: Media? = null,

                @SerializedName("title")
                val title: String? = null,

                @SerializedName("description_color")
                val descriptionColor: String? = null,

                @SerializedName("id")
                val bannerId: Long? = null
            ) {
                data class Attributes(
                    @SerializedName("start")
                    val start: ColorDensity? = null,

                    @SerializedName("background_fill")
                    val backgroundFill: String? = null,

                    @SerializedName("end")
                    val end: ColorDensity? = null
                ) {
                    data class ColorDensity(

                        @SerializedName("color")
                        val color: String? = null,

                        @SerializedName("density")
                        val density: Int? = null
                    )
                }

                data class Media(
                    @SerializedName("desktop")
                    val desktop: String? = null,

                    @SerializedName("android")
                    val android: String? = null,

                    @SerializedName("mobile")
                    val mobile: String? = null,

                    @SerializedName("ios")
                    val ios: String? = null
                )

                data class Text(
                    @SerializedName("secondary")
                    val secondary: TextData? = null,

                    @SerializedName("primary")
                    val primary: TextData? = null
                ) {
                    data class TextData(
                        @SerializedName("color")
                        val color: String? = null,

                        @SerializedName("title")
                        val title: String? = null,

                        @SerializedName("redirect_url")
                        val redirectUrl: String? = null
                    )
                }
            }
        }
    }
}
