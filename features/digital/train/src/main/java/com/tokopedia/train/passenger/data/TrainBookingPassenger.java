package com.tokopedia.train.passenger.data;

import androidx.annotation.IntDef;

import static com.tokopedia.train.passenger.data.TrainBookingPassenger.ADULT;
import static com.tokopedia.train.passenger.data.TrainBookingPassenger.CHILD;
import static com.tokopedia.train.passenger.data.TrainBookingPassenger.INFANT;


/**
 * Created by nabillasabbaha on 26/06/18.
 */
@IntDef({ADULT, CHILD, INFANT})
public @interface TrainBookingPassenger {
    int ADULT = 1;
    int CHILD = 2;
    int INFANT = 3;
}
