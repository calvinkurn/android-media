package com.tokopedia.recharge_component.model

enum class WidgetSource(val source: String) {
    TOPUP_BILLS("topup_dan_tagihan"),
    FINANCE("keuangan"),
    TRAVEL_ENTERTAINMENT("travel_dan_entertainment");

        companion object {
            fun findSourceByString(sourceString: String): WidgetSource {
                return values().firstOrNull { it.source == sourceString } ?: TOPUP_BILLS
            }
        }
}