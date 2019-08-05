package com.tokopedia.imagepicker.picker.main.builder;

import android.support.annotation.IntDef;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_RECORDER;

/**
 * Created by hendry on 04/05/18.
 */

@IntDef({TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM, TYPE_RECORDER})
public @interface ImagePickerTabTypeDef {
    int TYPE_GALLERY = 1;
    int TYPE_CAMERA = 2;
    int TYPE_INSTAGRAM = 3;
    int TYPE_RECORDER = 4;
}
