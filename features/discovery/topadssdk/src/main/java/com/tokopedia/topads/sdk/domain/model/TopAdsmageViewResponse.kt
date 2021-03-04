package com.tokopedia.topads.sdk.domain.model


import com.google.gson.annotations.SerializedName

data class TopAdsmageViewResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("header")
    val header: Header?,
    @SerializedName("status")
    val status: Status?
) {
    data class Data(
        @SerializedName("ad_click_url")
        val adClickUrl: String?,
        @SerializedName("ad_view_url")
        val adViewUrl: String?,
        @SerializedName("applinks")
        val applinks: String?,
        @SerializedName("banner")
        val banner: Banner?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("redirect")
        val redirect: String?
    ) {
        data class Banner(
            @SerializedName("images")
            val images: List<Image?>?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("shop")
            val shop: Shop?,
            @SerializedName("position")
            val position: Int = 0
        ) {
            data class Image(
                @SerializedName("full_ecs")
                val fullEcs: String?,
                @SerializedName("image_dimension")
                val imageDimension: ImageDimension?
            ) {
                data class ImageDimension(
                    @SerializedName("dimen_id")
                    val dimenId: Int?,
                    @SerializedName("height")
                    val height: Int?,
                    @SerializedName("width")
                    val width: Int?
                )
            }

            data class Shop(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("image_shop")
                val imageShop: ImageShop?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("power_merchant")
                val powerMerchant: Boolean?,
                @SerializedName("power_merchant_badge")
                val powerMerchantBadge: Boolean?,
                @SerializedName("shop_is_official")
                val shopIsOfficial: Boolean?
            ) {
                data class ImageShop(
                    @SerializedName("cover")
                    val cover: String?,
                    @SerializedName("cover_ecs")
                    val coverEcs: String?,
                    @SerializedName("s_ecs")
                    val sEcs: String?,
                    @SerializedName("s_url")
                    val sUrl: String?,
                    @SerializedName("xs_ecs")
                    val xsEcs: String?,
                    @SerializedName("xs_url")
                    val xsUrl: String?
                )
            }
        }
    }

    data class Header(
        @SerializedName("meta")
        val meta: Meta?,
        @SerializedName("pagination")
        val pagination: Pagination?,
        @SerializedName("process_time")
        val processTime: Double?,
        @SerializedName("total_data")
        val totalData: Int?
    ) {
        data class Meta(
            @SerializedName("ab_test")
            val abTest: String?,
            @SerializedName("display")
            val display: String?
        )

        data class Pagination(
            @SerializedName("kind")
            val kind: String?,
            @SerializedName("next_page_token")
            val nextPageToken: String?
        )
    }

    data class Status(
        @SerializedName("error_code")
        val errorCode: Int?,
        @SerializedName("message")
        val message: String?
    )
}