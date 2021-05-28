package com.tokopedia.flight.search.data.cache.db

import androidx.room.TypeConverter
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum

/**
 * Created by Furqan on 05/11/20.
 */
object FlightTypeConverters {
    @JvmStatic
    @TypeConverter
    fun toRefundableEnum(id: Int): RefundableEnum {
        for (refundableEnum in RefundableEnum.values()) {
            if (refundableEnum.id == id) return refundableEnum
        }
        return RefundableEnum.NOT_REFUNDABLE
    }

    @JvmStatic
    @TypeConverter
    fun toInteger(refundableEnum: RefundableEnum): Int {
        return refundableEnum.id
    }
}