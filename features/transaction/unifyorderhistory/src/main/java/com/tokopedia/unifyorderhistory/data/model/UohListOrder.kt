package com.tokopedia.unifyorderhistory.data.model

import com.google.gson.annotations.SerializedName

data class UohListOrder(
    @SerializedName("uohOrders")
    val uohOrders: UohOrders = UohOrders()
) {
    data class UohOrders(
        @SerializedName("orders")
        val orders: List<Order> = listOf(),

        @SerializedName("next")
        val next: String = "",

        @SerializedName("dateLimit")
        val dateLimit: String = "",

        @SerializedName("tickers")
        val tickers: List<Ticker> = listOf()
    ) {
        data class Order(
            @SerializedName("orderUUID")
            val orderUUID: String = "",

            @SerializedName("verticalID")
            val verticalID: String = "",

            @SerializedName("verticalCategory")
            val verticalCategory: String = "",

            @SerializedName("userID")
            val userID: String = "",

            @SerializedName("status")
            val status: String = "",

            @SerializedName("verticalStatus")
            val verticalStatus: String = "",

            @SerializedName("searchableText")
            val searchableText: String = "",

            @SerializedName("metadata")
            val metadata: Metadata = Metadata(),

            @SerializedName("createTime")
            val createTime: String,

            @SerializedName("createBy")
            val createBy: String,

            @SerializedName("updateTime")
            val updateTime: String,

            @SerializedName("updateBy")
            val updateBy: String
        ) {
            data class Metadata(
                @SerializedName("upstream")
                val upstream: String = "",

                @SerializedName("detailURL")
                val detailURL: DetailUrl = DetailUrl(),

                @SerializedName("verticalLogo")
                val verticalLogo: String = "",

                @SerializedName("verticalLabel")
                val verticalLabel: String = "",

                @SerializedName("paymentDate")
                val paymentDate: String = "",

                @SerializedName("paymentDateStr")
                val paymentDateStr: String = "",

                @SerializedName("status")
                val status: Status = Status(),

                @SerializedName("products")
                val products: List<Product> = listOf(),

                @SerializedName("otherInfo")
                val otherInfo: OtherInfo = OtherInfo(),

                @SerializedName("totalPrice")
                val totalPrice: TotalPrice = TotalPrice(),

                @SerializedName("tickers")
                val tickers: List<Ticker> = listOf(),

                @SerializedName("buttons")
                val buttons: List<Button> = listOf(),

                @SerializedName("dotMenus")
                val dotMenus: List<DotMenu> = listOf(),

                @SerializedName("queryParams")
                val queryParams: String = "",

                @SerializedName("listProducts")
                val listProducts: String = "",

                @SerializedName("extraComponents")
                val extraComponents: List<ExtraComponent> = listOf()
            ) {

                fun getReviewRatingComponent(): ExtraComponent? {
                    return extraComponents.find {
                        it.type == ExtraComponent.TYPE_REVIEW_GOPAY_COINS_WITH_STARS ||
                            it.type == ExtraComponent.TYPE_REVIEW_INTERACTIVE_STARS ||
                            it.type == ExtraComponent.TYPE_REVIEW_GOPAY_COINS
                    }
                }

                data class DetailUrl(
                    @SerializedName("appURL")
                    val appURL: String = "",

                    @SerializedName("appTypeLink")
                    val appTypeLink: String = "",

                    @SerializedName("webURL")
                    val webURL: String = "",

                    @SerializedName("webTypeLink")
                    val webTypeLink: String = ""
                )

                data class Status(
                    @SerializedName("label")
                    val label: String = "",

                    @SerializedName("textColor")
                    val textColor: String = "",

                    @SerializedName("bgColor")
                    val bgColor: String = ""
                )

                data class Product(
                    @SerializedName("title")
                    val title: String = "",

                    @SerializedName("imageURL")
                    val imageURL: String = "",

                    @SerializedName("inline1")
                    val inline1: Inline1 = Inline1(),

                    @SerializedName("inline2")
                    val inline2: Inline2 = Inline2()
                ) {
                    data class Inline1(
                        @SerializedName("label")
                        val label: String = "",

                        @SerializedName("textColor")
                        val textColor: String = "",

                        @SerializedName("bgColor")
                        val bgColor: String = ""
                    )

                    data class Inline2(
                        @SerializedName("label")
                        val label: String = "",

                        @SerializedName("textColor")
                        val textColor: String = "",

                        @SerializedName("bgColor")
                        val bgColor: String = ""
                    )
                }

                data class OtherInfo(
                    @SerializedName("actionType")
                    val actionType: String = "",

                    @SerializedName("appURL")
                    val appURL: String = "",

                    @SerializedName("webURL")
                    val webURL: String = "",

                    @SerializedName("label")
                    val label: String = "",

                    @SerializedName("textColor")
                    val textColor: String = "",

                    @SerializedName("bgColor")
                    val bgColor: String = ""
                )

                data class TotalPrice(
                    @SerializedName("value")
                    val value: String = "",

                    @SerializedName("label")
                    val label: String = "",

                    @SerializedName("textColor")
                    val textColor: String = "",

                    @SerializedName("bgColor")
                    val bgColor: String = ""
                )

                data class Button(
                    @SerializedName("Label")
                    val label: String = "",

                    @SerializedName("variantColor")
                    val variantColor: String = "",

                    @SerializedName("type")
                    val type: String = "",

                    @SerializedName("actionType")
                    val actionType: String = "",

                    @SerializedName("appURL")
                    val appURL: String = "",

                    @SerializedName("webURL")
                    val webURL: String = ""
                )

                data class DotMenu(
                    @SerializedName("actionType")
                    val actionType: String = "",

                    @SerializedName("appURL")
                    val appURL: String = "",

                    @SerializedName("webURL")
                    val webURL: String = "",

                    @SerializedName("label")
                    val label: String = "",

                    @SerializedName("textColor")
                    val textColor: String = "",

                    @SerializedName("bgColor")
                    val bgColor: String = ""
                )

                data class ExtraComponent(
                    @SerializedName("type")
                    val type: String = "",

                    @SerializedName("action")
                    val action: Action = Action(),

                    @SerializedName("label")
                    val label: String = ""
                ) {

                    companion object {
                        const val TYPE_REVIEW_GOPAY_COINS_WITH_STARS = "gopay_coins_with_stars"
                        const val TYPE_REVIEW_INTERACTIVE_STARS = "interactive_stars"
                        const val TYPE_REVIEW_GOPAY_COINS = "gopay_coins"
                    }

                    data class Action(
                        @SerializedName("appURL")
                        val appUrl: String = ""
                    )
                }
            }
        }

        data class Ticker(
            @SerializedName("action")
            val action: Action = Action(),

            @SerializedName("title")
            val title: String = "",

            @SerializedName("text")
            val text: String = "",

            @SerializedName("type")
            val type: String = "",

            @SerializedName("isFull")
            val isFull: Boolean = false
        ) {
            data class Action(
                @SerializedName("actionType")
                val actionType: String = "",

                @SerializedName("appURL")
                val appUrl: String = "",

                @SerializedName("webURL")
                val webUrl: String = "",

                @SerializedName("label")
                val label: String = "",

                @SerializedName("textColor")
                val textColor: String = "",

                @SerializedName("bgColor")
                val bgColor: String = ""
            )
        }
    }
}
