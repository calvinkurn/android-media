package com.tokopedia.play.view.type

/**
 * Created by jegul on 02/03/20
 */
sealed class BottomInsetsType {

    object Keyboard : BottomInsetsType()
    object ProductSheet : BottomInsetsType()
    sealed class VariantSheet : BottomInsetsType() {

        /**
         * Used to represent variant state regardless of the type
         */
        object Variant : VariantSheet()

        data class Buy(val productId: String) : VariantSheet()
        data class AddToCart(val productId: String) : VariantSheet()
    }
}