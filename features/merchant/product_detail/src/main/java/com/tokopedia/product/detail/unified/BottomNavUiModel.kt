package com.tokopedia.product.detail.unified

/**
 * @author by astidhiyaa on 27/11/23
 *
 * Button State [Ingatkan Saya, +Keranjang, Stok Habis], map product
 */
data class BottomNavUiModel(
    val title: String,
    val price: Price,
    val stock: Stock,
    val buttonState: ButtonState,
    val variant: Variant, // or just hasVariant
    val shop: Shop
) {

    companion object {
        val Empty: BottomNavUiModel
            get() =
                BottomNavUiModel(
                    title = "",
                    price = NormalPrice(price = 0.0, priceFmt = ""),
                    stock = OutOfStock,
                    buttonState = Inactive,
                    variant = NoVariant,
                    shop = Shop.Empty
                )
    }

    sealed interface Price {
        val price: Double
    }

    data class NormalPrice(
        override val price: Double,
        val priceFmt: String
    ) : Price

    data class DiscountedPrice(
        override val price: Double,
        val ogPriceFmt: String,
        val discountedPrice: String,
        val discountPercentage: String
    ) : Price

    sealed interface ButtonState // define

    object Active : ButtonState
    object Inactive : ButtonState
    object OOS : ButtonState

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

    val Price.finalPrice: String
        get() = when (this) {
            is DiscountedPrice -> this.discountedPrice
            is NormalPrice -> this.priceFmt
            else -> this.price.toString()
        }
}

// ATC butuh data apa aja?
