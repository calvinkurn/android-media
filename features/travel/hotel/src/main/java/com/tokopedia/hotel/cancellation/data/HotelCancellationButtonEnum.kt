package com.tokopedia.hotel.cancellation.data

import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by jessica on 08/05/20
 */

enum class HotelCancellationButtonEnum(val value: String, val buttonType: Int,
                                       val buttonVariant: Int = UnifyButton.Variant.FILLED) {
    BUY("buy", UnifyButton.Type.TRANSACTION),
    PRIMARY("primary", UnifyButton.Type.MAIN),
    SECONDARY("secondary", UnifyButton.Type.ALTERNATE, UnifyButton.Variant.GHOST);

    companion object {
        fun getEnumFromValue(value: String): HotelCancellationButtonEnum {
            return when(value) {
                BUY.value -> BUY
                PRIMARY.value -> PRIMARY
                SECONDARY.value -> SECONDARY
                else -> PRIMARY
            }
        }
    }
}