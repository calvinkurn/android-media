package com.tokopedia.train.search.data.typedef;

import androidx.annotation.IntDef;

import static com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef.DEPARTURE_SCHEDULE;
import static com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef.RETURN_SCHEDULE;

/**
 * Created by nabillasabbaha on 3/14/18.
 */
@IntDef({DEPARTURE_SCHEDULE, RETURN_SCHEDULE})
public @interface TrainScheduleTypeDef {
    int DEPARTURE_SCHEDULE = 1;
    int RETURN_SCHEDULE = 2;
}
