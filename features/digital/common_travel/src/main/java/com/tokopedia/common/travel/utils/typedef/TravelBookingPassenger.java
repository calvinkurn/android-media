package com.tokopedia.common.travel.utils.typedef;

import android.support.annotation.IntDef;

import static com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger.ADULT;
import static com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger.CHILD;
import static com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger.INFANT;


/**
 * Created by nabillasabbaha on 26/06/18.
 */
@IntDef({ADULT, CHILD, INFANT})
public @interface TravelBookingPassenger {
    int ADULT = 1;
    int CHILD = 2;
    int INFANT = 3;
}
