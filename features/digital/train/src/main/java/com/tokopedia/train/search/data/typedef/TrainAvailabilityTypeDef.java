package com.tokopedia.train.search.data.typedef;

import androidx.annotation.IntDef;

import static com.tokopedia.train.search.data.typedef.TrainAvailabilityTypeDef.DEFAULT_VALUE;

/**
 * Created by nabillasabbaha on 3/14/18.
 */
@IntDef({DEFAULT_VALUE})
public @interface TrainAvailabilityTypeDef {
    int DEFAULT_VALUE = -1;
}
