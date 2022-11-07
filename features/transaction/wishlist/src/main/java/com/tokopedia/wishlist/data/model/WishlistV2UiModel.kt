package com.tokopedia.wishlist.data.model

data class WishlistV2UiModel(
    var errorMessage: String = "",
    var offset: Int = 0,
    var hasNextPage: Boolean = false,
    var query: String = "",
    var sortFilters: List<SortFiltersItem> = emptyList(),
    var limit: Int = -1,
    var totalData: Int = -1,
    var nextPageUrl: String = "",
    var page: Int = -1,
    var items: List<Item> = emptyList(),
    var emptyState: EmptyState = EmptyState(),
    var ticker: TickerState = TickerState(),
    var storageCleanerBottomSheet: StorageCleanerBottomSheet = StorageCleanerBottomSheet(),
    var countRemovableItems: Int = 0,
    var showDeleteProgress: Boolean = false
) {
    data class Item(
        var originalPrice: String = "",
        var labelGroup: List<LabelGroupItem> = emptyList(),
        var shop: Shop = Shop(),
        var priceFmt: String = "",
        var available: Boolean = false,
        var rating: String = "",
        var originalPriceFmt: String = "",
        var discountPercentage: Int = -1,
        var defaultChildId: String = "",
        var price: String = "",
        var wholesalePrice: List<WholesalePriceItem> = emptyList(),
        var id: String = "",
        var buttons: Buttons = Buttons(),
        var imageUrl: String = "",
        var discountPercentageFmt: String = "",
        var wishlistId: String = "",
        var variantName: String = "",
        var labelStock: String = "",
        var url: String = "",
        var labelStatus: String = "",
        var labels: List<String> = emptyList(),
        var badges: List<BadgesItem> = emptyList(),
        var name: String = "",
        var minOrder: String = "",
        var bebasOngkir: BebasOngkir = BebasOngkir(),
        var category: List<CategoryItem> = emptyList(),
        var preorder: Boolean = false,
        var soldCount: String = ""
    ) {
        data class LabelGroupItem(
            var position: String = "",
            var title: String = "",
            var type: String = "",
            var url: String = ""
        )

        data class WholesalePriceItem(
            var price: String = "",
            var maximum: String = "",
            var minimum: String = ""
        )

        data class BadgesItem(
            var imageUrl: String = "",
            var title: String = ""
        )

        data class BebasOngkir(
            var imageUrl: String = "",
            var type: Int = -1,
            var title: String = ""
        )

        data class CategoryItem(
            var categoryName: String = "",
            var categoryId: Int = -1
        )

        data class Shop(
            var isTokonow: Boolean = false,
            var name: String = "",
            var location: String = "",
            var id: String = "",
            var fulfillment: Fulfillment = Fulfillment(),
            var url: String = ""
        ) {
            data class Fulfillment(
                var text: String = "",
                var isFulfillment: Boolean = false
            )
        }

        data class Buttons(
            var additionalButtons: List<AdditionalButtonsItem> = emptyList(),
            var primaryButton: PrimaryButton = PrimaryButton()
        ) {
            data class AdditionalButtonsItem(
                var action: String = "",
                var text: String = "",
                var url: String = ""
            )

            data class PrimaryButton(
                var action: String = "",
                var text: String = "",
                var url: String = ""
            )
        }
    }

    data class TickerState(
        var message: String = "",
        var type: String = "",
        var button: ButtonTicker = ButtonTicker()
    ) {
        data class ButtonTicker(
            var action: String = "",
            var text: String = ""
        )
    }

    data class StorageCleanerBottomSheet(
        var title: String = "",
        var description: String = "",
        var options: List<OptionCleanerBottomsheet> = emptyList(),
        var btnCleanBottomSheet: ButtonCleanBottomSheet = ButtonCleanBottomSheet()
    ) {
        data class OptionCleanerBottomsheet(
            var name: String = "",
            var description: String = "")

        data class ButtonCleanBottomSheet(
            var text: String = ""
        )
    }

    data class EmptyState(
        var button: List<ButtonEmptyState> = emptyList(),
        var messages: List<MessageEmptyState> = emptyList(),
        var type: String = ""
    ) {
        data class ButtonEmptyState(
            var action: String = "",
            var text: String = "",
            var url: String = ""
        )

        data class MessageEmptyState(
            var title: String = "",
            var description: String = "",
            var imageUrl: String = ""
        )
    }

    data class SortFiltersItem(
        var selectionType: Int = -1,
        var isActive: Boolean = false,
        var name: String = "",
        var options: List<OptionsItem> = emptyList(),
        var id: Int = -1,
        var text: String = ""
    ) {
        data class OptionsItem(
            var isSelected: Boolean = false,
            var description: String = "",
            var optionId: String = "",
            var text: String = ""
        )
    }
}