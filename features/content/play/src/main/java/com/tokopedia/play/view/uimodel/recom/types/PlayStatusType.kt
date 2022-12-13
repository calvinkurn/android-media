package com.tokopedia.play.view.uimodel.recom.types


/**
 * Created by mzennis on 02/02/21.
 */
enum class PlayStatusType(val value: String) {
    Active("ACTIVE"),
    Freeze("FREEZE"),
    Banned("BANNED"),
    Archived("ARCHIVED");

    val isFreeze: Boolean
        get() = this == Freeze

    val isBanned: Boolean
        get() = this == Banned

    val isActive: Boolean
        get() = this == Active

    val isArchive: Boolean
        get() = this == Archived

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
