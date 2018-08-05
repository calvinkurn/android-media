package com.tokopedia.train.common.util;

import android.support.annotation.StringDef;

@StringDef({TrainNetworkErrorConstant.SOLD_OUT, TrainNetworkErrorConstant.RUTE_NOT_FOUND,
        TrainNetworkErrorConstant.LESS_THAN_3_HOURRS})
public @interface TrainNetworkErrorConstant {
    String SOLD_OUT = "BOOK49";
    String RUTE_NOT_FOUND = "BOOK48";
    String LESS_THAN_3_HOURRS = "BOOK47";
}
