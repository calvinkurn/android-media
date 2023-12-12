package com.tokopedia.content.product.preview.view.uimodel

/**
 * @author by astidhiyaa on 27/11/23
 *
 * Button State [Ingatkan Saya, +Keranjang, Stok Habis], map product
 */
data class BottomNavUiModel(
    val title: String,
    val price: Price,
    val stock: Int,
    val buttonState: ButtonState,
    val hasVariant: Boolean,
    val shop: Shop
) {

    companion object {
        val Empty: BottomNavUiModel
            get() =
                BottomNavUiModel(
                    title = "Test",
                    price = NormalPrice(priceFmt = ""),
                    stock = 0,
                    buttonState = ButtonState.Inactive,
                    hasVariant = false,
                    shop = Shop.Empty
                )
    }

    sealed interface Price

    data class NormalPrice(
        val priceFmt: String
    ) : Price

    data class DiscountedPrice(
        val ogPriceFmt: String,
        val discountedPrice: String,
        val discountPercentage: String
    ) : Price

    enum class ButtonState(val value: String) {
        Active("ACTIVE"),
        Inactive("INACTIVE"),
        OOS("OOS"),
        Unknown("");

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

    sealed interface Variant

    object NoVariant : Variant
    data class WithVariant(val lastSelected: Int) : Variant

    sealed interface Stock {
        val stock: Int
    }

    object OutOfStock : Stock {
        override val stock: Int
            get() = 0
    }

    data class Available(override val stock: Int) : Stock

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
}

val BottomNavUiModel.Price.finalPrice: String
    get() = when (this) {
        is BottomNavUiModel.DiscountedPrice -> this.discountedPrice
        is BottomNavUiModel.NormalPrice -> this.priceFmt
    }
