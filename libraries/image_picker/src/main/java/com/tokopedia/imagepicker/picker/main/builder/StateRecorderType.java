package com.tokopedia.imagepicker.picker.main.builder;

import androidx.annotation.IntDef;

@IntDef({StateRecorderType.START, StateRecorderType.STOP})
public @interface StateRecorderType {
    int START = 1;
    int STOP = 2;
}
