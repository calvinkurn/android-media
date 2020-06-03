package com.tokopedia.play.broadcaster.type

/**
 * Created by jegul on 03/06/20
 */
enum class EtalaseType(val typeInt: Int) {

    User(1),
    Group(-1),
    Unknown(Int.MIN_VALUE);

    companion object {
        val values = values()

        fun getByType(type: Int): EtalaseType {
            values.forEach {
                if (it.typeInt == type) return it
            }
            return Unknown
        }
    }
}