package com.tokopedia.imagepicker.picker.main.builder;

import android.support.annotation.IntDef;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

/**
 * Created by hendry on 04/05/18.
 */

@IntDef({TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM})
public @interface ImagePickerTabTypeDef {
    int TYPE_GALLERY = 1;
    int TYPE_CAMERA = 2;
    int TYPE_INSTAGRAM = 3;
}
