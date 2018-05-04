package com.tokopedia.imagepicker.picker.main.util;

import android.support.annotation.IntDef;

import static com.tokopedia.imagepicker.picker.main.util.ExpectedImageRatioDef.TYPE_1_1;
import static com.tokopedia.imagepicker.picker.main.util.ExpectedImageRatioDef.TYPE_4_5;
import static com.tokopedia.imagepicker.picker.main.util.ExpectedImageRatioDef.TYPE_5_4;

/**
 * Created by hendry on 04/05/18.
 */

@IntDef({TYPE_1_1, TYPE_4_5, TYPE_5_4})
public @interface ExpectedImageRatioDef {
    int TYPE_1_1 = 1;
    int TYPE_4_5 = 2;
    int TYPE_5_4 = 3;
}