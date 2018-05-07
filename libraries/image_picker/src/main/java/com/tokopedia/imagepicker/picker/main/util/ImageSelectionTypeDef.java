package com.tokopedia.imagepicker.picker.main.util;

import android.support.annotation.IntDef;

import static com.tokopedia.imagepicker.picker.main.util.ImageSelectionTypeDef.TYPE_MULTIPLE_NO_PREVIEW;
import static com.tokopedia.imagepicker.picker.main.util.ImageSelectionTypeDef.TYPE_MULTIPLE_WITH_PREVIEW;
import static com.tokopedia.imagepicker.picker.main.util.ImageSelectionTypeDef.TYPE_SINGLE;

/**
 * Created by hendry on 04/05/18.
 */

@IntDef({TYPE_SINGLE, TYPE_MULTIPLE_NO_PREVIEW, TYPE_MULTIPLE_WITH_PREVIEW})
public @interface ImageSelectionTypeDef {
    int TYPE_SINGLE = 1;
    int TYPE_MULTIPLE_NO_PREVIEW = 2;
    int TYPE_MULTIPLE_WITH_PREVIEW = 3;
}
