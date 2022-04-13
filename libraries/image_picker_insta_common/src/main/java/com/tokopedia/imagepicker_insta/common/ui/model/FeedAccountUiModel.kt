package com.tokopedia.imagepicker_insta.common.ui.model

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
data class FeedAccountUiModel(
    val name: String,
    val iconUrl: String,
    val type: Type,
) {
    enum class Type(val value: Int) {
        UNKNOWN(0),
        BUYER(1),
        SELLER(2),
    }

    companion object {
        val Empty = FeedAccountUiModel(
            name = "",
            iconUrl = "",
            type = Type.UNKNOWN,
        )

        fun getTypeByValue(value: Int): Type {
            return when(value) {
                1 -> Type.BUYER
                2 -> Type.SELLER
                else -> Type.UNKNOWN
            }
        }
    }
}