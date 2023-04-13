package com.tokopedia.wishlistcollection.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetWishlistCollectionResponse(
    @SerializedName("get_wishlist_collections")
    val getWishlistCollections: GetWishlistCollections = GetWishlistCollections()
) {
    data class GetWishlistCollections(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("status")
        val status: String = "",

        @SerializedName("data")
        val data: WishlistCollectionResponseData = WishlistCollectionResponseData()
    ) {
        data class WishlistCollectionResponseData(
            @SerializedName("ticker")
            val ticker: Ticker = Ticker(),

            @SerializedName("is_empty_state")
            val isEmptyState: Boolean = false,

            @SerializedName("show_delete_progress")
            val showDeleteProgress: Boolean = false,

            @SerializedName("empty_wishlist_image_url")
            val emptyWishlistImageUrl: String = "",

            @SerializedName("empty_state")
            val emptyState: EmptyState = EmptyState(),

            @SerializedName("collections")
            val collections: List<CollectionsItem> = emptyList(),

            @SerializedName("placeholder")
            val placeholder: Placeholder = Placeholder(),

            @SerializedName("onboarding_bottomsheet")
            val onboardingBottomsheet: OnboardingBottomsheet = OnboardingBottomsheet(),

            @SerializedName("onboarding_coachmark")
            val onboardingCoachmark: OnboardingCoachmark = OnboardingCoachmark(),

            @SerializedName("total_collection")
            val totalCollection: Int = 0,

            @SerializedName("max_limit_collection")
            val maxLimitCollection: Int = 0,

            @SerializedName("wording_max_limit_collection")
            val wordingMaxLimitCollection: String = ""
        ) {

            data class Ticker(
                @SerializedName("description")
                val description: String = "",

                @SerializedName("title")
                val title: String = ""
            )

            data class EmptyState(
                @SerializedName("buttons")
                val buttons: List<CollectionWishlistButtonsItem> = emptyList(),

                @SerializedName("messages")
                val messages: List<MessagesItem> = emptyList()
            ) {
                data class MessagesItem(
                    @SerializedName("image_url")
                    val imageUrl: String = "",

                    @SerializedName("description")
                    val description: String = ""
                )
            }

            data class CollectionsItem(
                @SerializedName("total_item")
                val totalItem: Int = 0,

                @SerializedName("images")
                val images: List<String> = emptyList(),

                @SerializedName("name")
                val name: String = "",

                @SerializedName("item_text")
                val itemText: String = "",

                @SerializedName("id")
                val id: String = "",

                @SerializedName("actions")
                val actions: List<Action> = emptyList(),

                @SerializedName("indicator")
                val indicator: Indicator = Indicator()
            ) {
                data class Indicator(
                    @SerializedName("title")
                    val title: String = ""
                )
            }

            data class Placeholder(
                @SerializedName("image_url")
                val imageUrl: String = "",

                @SerializedName("action")
                val action: String = "",

                @SerializedName("text")
                val text: String = ""
            )

            data class OnboardingBottomsheet(
                @SerializedName("buttons")
                val buttons: List<CollectionWishlistButtonsItem> = emptyList(),

                @SerializedName("image_url")
                val imageUrl: String = "",

                @SerializedName("description")
                val description: String = "",

                @SerializedName("title")
                val title: String = ""
            )

            data class OnboardingCoachmark(
                @SerializedName("details")
                val details: List<DetailsItem> = emptyList(),

                @SerializedName("skip_button_text")
                val skipButtonText: String = ""
            ) {
                data class DetailsItem(

                    @SerializedName("buttons")
                    val buttons: List<CollectionWishlistButtonsItem> = emptyList(),

                    @SerializedName("step")
                    val step: Int = 0,

                    @SerializedName("title")
                    val title: String = "",

                    @SerializedName("message")
                    val message: String = ""
                )
            }

            @Parcelize
            data class Action(
                @SerializedName("text")
                val text: String = "",

                @SerializedName("action")
                val action: String = "",

                @SerializedName("url")
                val url: String = ""
            ) : Parcelable
        }
    }
}
