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
    val shop: Shop
) {

    companion object {
        val Empty: BottomNavUiModel
            get() =
                BottomNavUiModel(
                    title = "",
                    price = NormalPrice(priceFmt = ""),
                    stock = 0,
                    buttonState = ButtonState.Unknown,
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

    enum class ButtonState(val value: String, val text: String) {
        Active("ACTIVE", "+ Keranjang"),
        Inactive("INACTIVE","Stok Habis"),
        OOS("OOS", "Ingatkan Saya"),
        Unknown("","");

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
        val message: String,
    )
}

val BottomNavUiModel.Price.finalPrice: String
    get() = when (this) {
        is BottomNavUiModel.DiscountedPrice -> this.discountedPrice
        is BottomNavUiModel.NormalPrice -> this.priceFmt
    }
