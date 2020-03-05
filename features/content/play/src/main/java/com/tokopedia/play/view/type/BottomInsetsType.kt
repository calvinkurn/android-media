package com.tokopedia.play.view.type

/**
 * Created by jegul on 02/03/20
 */
sealed class BottomInsetsType {

    object Keyboard : BottomInsetsType()
    sealed class BottomSheet : BottomInsetsType() {

        abstract val height: Int?

        data class Product(override val height: Int?) : BottomSheet()
        data class Variant(override val height: Int?) : BottomSheet()
    }

    val isKeyboard: Boolean
        get() = this is Keyboard

    val isBottomSheet: Boolean
        get() = this is BottomSheet
}