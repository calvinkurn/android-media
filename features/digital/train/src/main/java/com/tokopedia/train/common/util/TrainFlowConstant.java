package com.tokopedia.train.common.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TrainFlowConstant.RESEARCH, TrainFlowConstant.HOME})
@Retention(RetentionPolicy.SOURCE)
public @interface TrainFlowConstant {
    int RESEARCH = 1;
    int HOME = 2;
}

