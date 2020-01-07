package com.tokopedia.play.ui.toolbar.model


/**
 * Created by mzennis on 2019-12-12.
 */
enum class PartnerType(val value: Int) {
    ADMIN(0),
    SHOP(1),
    INFLUENCER(4),
    UNKNOWN(-1);

    companion object {
        private val values = values()

        fun getTypeByValue(value: Int): PartnerType {
            values.forEach {
                if (it.value == value) return it
            }
            return UNKNOWN
        }
    }
}