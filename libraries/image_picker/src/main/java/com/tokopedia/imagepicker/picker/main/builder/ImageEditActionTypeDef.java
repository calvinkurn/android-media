package com.tokopedia.imagepicker.picker.main.builder;

import androidx.annotation.IntDef;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_WATERMARK;

/**
 * Created by hendry on 04/05/18.
 */

@IntDef({ACTION_CROP, ACTION_ROTATE, ACTION_WATERMARK, ACTION_CROP_ROTATE,
        ACTION_BRIGHTNESS, ACTION_CONTRAST})
public @interface ImageEditActionTypeDef {
    int ACTION_CROP = 1;
    int ACTION_ROTATE = 2;
    int ACTION_WATERMARK = 3;
    int ACTION_CROP_ROTATE = 4;
    int ACTION_BRIGHTNESS = 5;
    int ACTION_CONTRAST = 6;
}