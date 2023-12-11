package com.tokopedia.discovery2.data.play

enum class DiscoPlayWidgetType {
    DISCO_PAGE_V2,
    DISCO_PAGE
}

object DiscoPlayWidgetMapper {
    fun get(value: String?): DiscoPlayWidgetType {
        val stringValue = value ?: return DiscoPlayWidgetType.DISCO_PAGE

        return runCatching { enumValueOf<DiscoPlayWidgetType>(stringValue) }
            .getOrDefault(DiscoPlayWidgetType.DISCO_PAGE)
    }
}
