package com.tokopedia.play.view.uimodel.recom.types


/**
 * Created by mzennis on 02/02/21.
 */
enum class PlayStatusType(val value: String) {
    Active("ACTIVE"),
    Freeze("FREEZE"),
    Banned("BANNED");

    val isFreeze: Boolean
        get() = this == Freeze

    val isBanned: Boolean
        get() = this == Banned

    val isActive: Boolean
        get() = this == Active

    companion object {

        private val values = values()

        fun getByValue(value: String): PlayStatusType {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return Freeze
        }
    }
}