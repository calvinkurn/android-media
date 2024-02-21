package com.tokopedia.content.product.preview.view.uimodel

/**
 * @author by astidhiyaa on 27/11/23
 *
 * Button State [Ingatkan Saya, +Keranjang, Stok Habis]
 */
data class BottomNavUiModel(
    val title: String,
    val price: Price,
    val stock: Int,
    val buttonState: ButtonState,
    val hasVariant: Boolean,
    val shop: Shop,
    val categoryTree: List<CategoryTree>
) {

    companion object {
        val Empty: BottomNavUiModel
            get() =
                BottomNavUiModel(
                    title = "",
                    price = Price.NormalPrice(ogPriceFmt = ""),
                    stock = 0,
                    buttonState = ButtonState.Unknown,
                    hasVariant = false,
                    shop = Shop.Empty,
                    categoryTree = emptyList()
                )
    }

    sealed interface Price {
        val ogPriceFmt: String
        data class NormalPrice(
            override val ogPriceFmt: String,
        ) : Price

        data class DiscountedPrice(
            override val ogPriceFmt: String,
            val discountedPrice: String,
            val discountPercentage: String
        ) : Price

        data class NettPrice(
            override val ogPriceFmt: String,
            val nettPriceFmt: String,
        ) : Price
    }
    enum class ButtonState(val value: String, val text: String) {
        Active("ACTIVE", "+ Keranjang"),
        Inactive("INACTIVE", "Stok Habis"),
        OOS("OOS", "Ingatkan Saya"),
        Unknown("", "");

        companion object {
            private val values = ButtonState.values()

            fun getByValue(value: String): ButtonState {
                values.forEach {
                    if (it.value == value) return it
                }
                return Unknown
            }
        }
    }

    data class Shop(
        val id: String,
        val name: String
    ) {
        companion object {
            val Empty: Shop
                get() =
                    Shop(id = "", name = "")
        }
    }

    data class RemindMeUiModel(
        val isSuccess: Boolean,
        val message: String
    )

    data class CategoryTree(
        val id: String,
        val name: String,
        val title: String
    )
}

val BottomNavUiModel.Price.finalPrice: String
    get() = when (this) {
        is BottomNavUiModel.Price.DiscountedPrice -> this.discountedPrice
        is BottomNavUiModel.Price.NormalPrice -> this.ogPriceFmt
        is BottomNavUiModel.Price.NettPrice -> this.nettPriceFmt
    }
