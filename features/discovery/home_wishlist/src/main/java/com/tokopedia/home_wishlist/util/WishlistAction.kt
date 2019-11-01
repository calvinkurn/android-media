package com.tokopedia.home_wishlist.util

data class WishlistAction(
        val position: Int = -1,
        val productId: Int = -1,
        val cartId: Int = -1,
        val isSuccess: Boolean = false,
        val message: String = "",
        val typeAction: TypeAction = TypeAction.NONE
)

enum class TypeAction{
    ADD_TO_CART, ADD_WISHLIST, REMOVE_WISHLIST, BULK_DELETE_WISHLIST, LOAD_MORE_ERROR, NONE;

    companion object {
        fun fromString(state: String) : TypeAction = when(state){
            "atc" -> ADD_TO_CART
            "ad" -> ADD_WISHLIST
            "rw" -> REMOVE_WISHLIST
            "bk" -> BULK_DELETE_WISHLIST
            "load_more_error" -> LOAD_MORE_ERROR
            else -> NONE
        }
    }

    override fun toString(): String {
        return when(this){
            ADD_WISHLIST -> "aw"
            ADD_TO_CART -> "atc"
            REMOVE_WISHLIST -> "rw"
            BULK_DELETE_WISHLIST -> "bk"
            else -> "none"
        }
    }
}