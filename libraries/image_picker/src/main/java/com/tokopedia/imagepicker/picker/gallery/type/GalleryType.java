package com.tokopedia.imagepicker.picker.gallery.type;

import androidx.annotation.IntDef;

import static com.tokopedia.imagepicker.picker.gallery.type.GalleryType.ALL;
import static com.tokopedia.imagepicker.picker.gallery.type.GalleryType.IMAGE_ONLY;
import static com.tokopedia.imagepicker.picker.gallery.type.GalleryType.VIDEO_ONLY;

/**
 * Created by hangnadi on 5/19/17.
 */

@IntDef({ALL, IMAGE_ONLY, VIDEO_ONLY})
public @interface GalleryType {
    int ALL = 0x0001;
    int IMAGE_ONLY = 0x0002;
    int VIDEO_ONLY = 0x0003;
}
