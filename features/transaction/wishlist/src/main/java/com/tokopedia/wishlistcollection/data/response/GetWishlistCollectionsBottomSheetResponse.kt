package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class GetWishlistCollectionsBottomSheetResponse(
    @SerializedName("get_wishlist_collections_bottomsheet")
    val getWishlistCollectionsBottomsheet: GetWishlistCollectionsBottomsheet
) {
    data class GetWishlistCollectionsBottomsheet(

        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("data")
        val data: Data = Data(),

        @SerializedName("status")
        val status: String = ""
    ) {
        data class Data(

            @SerializedName("title")
            val title: String = "",

            @SerializedName("title_button")
            val titleButton: TitleButton = TitleButton(),

            @SerializedName("notification")
            val notification: String = "",

            @SerializedName("main_section")
            val mainSection: MainSection = MainSection(),

            @SerializedName("additional_section")
            val additionalSection: AdditionalSection = AdditionalSection(),

            @SerializedName("placeholder")
            val placeholder: Placeholder = Placeholder(),

            @SerializedName("total_collection")
            val totalCollection: Int = 0,

            @SerializedName("max_limit_collection")
            val maxLimitCollection: Int = 0,

            @SerializedName("wording_max_limit_collection")
            val wordingMaxLimitCollection: String = ""
        ) {
            data class TitleButton(

                @SerializedName("image_url")
                val imageUrl: String = "",

                @SerializedName("action")
                val action: String = "",

                @SerializedName("text")
                val text: String = "",

                @SerializedName("url")
                val url: String = ""
            )

            data class MainSection(

                @SerializedName("collections")
                val collections: List<CollectionsItem> = emptyList(),

                @SerializedName("text")
                val text: String = ""
            ) {
                data class CollectionsItem(

                    @SerializedName("total_item")
                    val totalItem: Int = 0,

                    @SerializedName("image_url")
                    val imageUrl: String = "",

                    @SerializedName("name")
                    val name: String = "",

                    @SerializedName("item_text")
                    val itemText: String = "",

                    @SerializedName("id")
                    val id: String = "",

                    @SerializedName("label")
                    val label: String = "",

                    @SerializedName("is_contain_product")
                    val isContainProduct: Boolean = false,

                    @SerializedName("indicator")
                    val indicator: Indicator = Indicator()
                ) {
                    data class Indicator(
                        @SerializedName("title")
                        val title: String = ""
                    )
                }
            }

            data class AdditionalSection(

                @SerializedName("collections")
                val collections: List<MainSection.CollectionsItem> = emptyList(),

                @SerializedName("text")
                val text: String = ""
            )

            data class Placeholder(

                @SerializedName("image_url")
                val imageUrl: String = "",

                @SerializedName("action")
                val action: String = "",

                @SerializedName("text")
                val text: String = "",

                @SerializedName("url")
                val url: String = ""
            )
        }
    }
}
