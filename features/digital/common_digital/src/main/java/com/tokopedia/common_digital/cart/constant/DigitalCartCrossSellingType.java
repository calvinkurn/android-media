package com.tokopedia.common_digital.cart.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        DigitalCartCrossSellingType.DEFAULT,
        DigitalCartCrossSellingType.DEALS,
        DigitalCartCrossSellingType.MYBILLS,
        DigitalCartCrossSellingType.SUBSCRIBED
})
public @interface DigitalCartCrossSellingType {
    int DEFAULT = 0;
    int DEALS = 1;
    int MYBILLS = 2;
    int SUBSCRIBED = 3;
}
