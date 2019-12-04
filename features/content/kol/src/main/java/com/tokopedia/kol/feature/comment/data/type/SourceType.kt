package com.tokopedia.kol.feature.comment.data.type

/**
 * Created by jegul on 2019-10-28
 */
enum class SourceType(val typeInt: Int) {

    OFFICIAL_ACCOUNT(1),
    SHOP(2),
    USER(3);

    companion object {

        private val values = values()

        @JvmStatic
        fun getSourceByTypeInt(typeInt: Int): SourceType {
            for (value in values) {
                if (value.typeInt == typeInt) return value
            }
            throw IllegalStateException("Type is not supported")
        }
    }
}