package com.tokopedia.flight.searchV4.data.cache.db;

import androidx.room.TypeConverter;

import com.tokopedia.flight.searchV4.presentation.model.filter.RefundableEnum;

/**
 * Created by Rizky on 09/10/18.
 */
public class FlightTypeConverters {

    @TypeConverter
    public static RefundableEnum toRefundableEnum(Integer id) {
        for (RefundableEnum refundableEnum : RefundableEnum.values()) {
            if (refundableEnum.getId() == id) return refundableEnum;
        }
        return RefundableEnum.NOT_REFUNDABLE;
    }

    @TypeConverter
    public static Integer toInteger(RefundableEnum refundableEnum) {
        return refundableEnum.getId();
    }

}
