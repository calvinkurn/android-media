package com.tokopedia.train.common.util;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({TrainFlowExtraConstant.EXTRA_FLOW_DATA})
@Retention(RetentionPolicy.SOURCE)
public @interface TrainFlowExtraConstant {
    String EXTRA_FLOW_DATA = "EXTRA_FLOW_DATA";
}
