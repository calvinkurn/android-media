package com.tokopedia.imagepicker.picker.main.util;

import android.support.annotation.IntDef;

import static com.tokopedia.imagepicker.picker.main.util.ImageEditActionTypeDef.TYPE_CROP;
import static com.tokopedia.imagepicker.picker.main.util.ImageEditActionTypeDef.TYPE_CROP_ROTATE;
import static com.tokopedia.imagepicker.picker.main.util.ImageEditActionTypeDef.TYPE_ROTATE;
import static com.tokopedia.imagepicker.picker.main.util.ImageEditActionTypeDef.TYPE_WATERMARK;

/**
 * Created by hendry on 04/05/18.
 */

@IntDef({TYPE_CROP, TYPE_ROTATE, TYPE_WATERMARK, TYPE_CROP_ROTATE})
public @interface ImageEditActionTypeDef {
    int TYPE_CROP = 1;
    int TYPE_ROTATE = 2;
    int TYPE_WATERMARK = 3;
    int TYPE_CROP_ROTATE = 4;
}