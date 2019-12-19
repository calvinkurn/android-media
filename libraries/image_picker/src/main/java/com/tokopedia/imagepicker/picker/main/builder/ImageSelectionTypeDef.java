package com.tokopedia.imagepicker.picker.main.builder;

import androidx.annotation.IntDef;

import static com.tokopedia.imagepicker.picker.main.builder.ImageSelectionTypeDef.TYPE_MULTIPLE;
import static com.tokopedia.imagepicker.picker.main.builder.ImageSelectionTypeDef.TYPE_SINGLE;

/**
 * Created by hendry on 04/05/18.
 */

@IntDef({TYPE_SINGLE, TYPE_MULTIPLE})
public @interface ImageSelectionTypeDef {
    int TYPE_SINGLE = 1;
    int TYPE_MULTIPLE = 2;
}
