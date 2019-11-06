package com.tokopedia.train.search.data.typedef;

import androidx.annotation.StringDef;

import static com.tokopedia.train.search.data.typedef.DepartureTimeTypeDef.AFTERNOON;
import static com.tokopedia.train.search.data.typedef.DepartureTimeTypeDef.MORNING;
import static com.tokopedia.train.search.data.typedef.DepartureTimeTypeDef.NIGHT;

/**
 * Created by nabillasabbaha on 3/21/18.
 */
@StringDef({MORNING, AFTERNOON, NIGHT})
public @interface DepartureTimeTypeDef {

    String MORNING = "00:00 - 11:59";
    String AFTERNOON = "12:00 - 17:59";
    String NIGHT = "18:00 - 23:59";
}
