package com.tokopedia.hotel.common.util

/**
 * @author by jessica on 20/04/20
 */

enum class CurrencyEnum(format: String, useCommaAsThousand: Boolean) {
    RPwithSpace("Rp %s", false), RP("Rp%s", false), USD("$%s", true);

    val formatString: String
    private val useCommaAsThousand: Boolean
    fun getFormat(): String {
        return formatString
    }

    fun isUseCommaAsThousand(): Boolean {
        return useCommaAsThousand
    }

    init {
        this.formatString = format
        this.useCommaAsThousand = useCommaAsThousand
    }
}
