package com.tokopedia.common.travel.utils.typedef;

import android.support.annotation.IntDef;

import static com.tokopedia.common.travel.utils.typedef.TravelPassengerTitle.NONA;
import static com.tokopedia.common.travel.utils.typedef.TravelPassengerTitle.NYONYA;
import static com.tokopedia.common.travel.utils.typedef.TravelPassengerTitle.TUAN;


/**
 * Created by nabillasabbaha on 26/06/18.
 */
@IntDef({TUAN, NYONYA, NONA})
public @interface TravelPassengerTitle {

    int TUAN = 1;
    int NYONYA = 2;
    int NONA = 3;
}
