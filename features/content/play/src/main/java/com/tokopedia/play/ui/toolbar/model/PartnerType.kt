package com.tokopedia.play.ui.toolbar.model


/**
 * Created by mzennis on 2019-12-12.
 */
enum class PartnerType(val value: String) {
    Tokopedia("tokopedia"),
    Shop("shop"),
    Buyer("buyer"),
    TokoNow("tokonow"),
    Unknown("");

    companion object {
        private val values = values()

        fun getTypeByValue(value: String): PartnerType {
            values.forEach {
                if (it.value == value) return it
            }
            return Unknown
        }
    }
}
