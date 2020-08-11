package com.tokopedia.play.ui.toolbar.model


/**
 * Created by mzennis on 2019-12-12.
 */
enum class PartnerType(val value: Int) {
    Admin(0),
    Shop(1),
    Influencer(4),
    Unknown(-1);

    companion object {
        private val values = values()

        fun getTypeByValue(value: Int): PartnerType {
            values.forEach {
                if (it.value == value) return it
            }
            return Unknown
        }
    }
}