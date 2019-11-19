package com.tokopedia.train.common.util;

import androidx.annotation.StringDef;

@StringDef({TrainNetworkErrorConstant.SOLD_OUT, TrainNetworkErrorConstant.RUTE_NOT_FOUND,
        TrainNetworkErrorConstant.LESS_THAN_3_HOURS, TrainNetworkErrorConstant.TOO_MANY_SOFTBOOK})
public @interface TrainNetworkErrorConstant {
    String SOLD_OUT = "BOOK49";
    String RUTE_NOT_FOUND = "BOOK48";
    String LESS_THAN_3_HOURS = "BOOK47";
    String TOO_MANY_SOFTBOOK = "BOOK46";
}
