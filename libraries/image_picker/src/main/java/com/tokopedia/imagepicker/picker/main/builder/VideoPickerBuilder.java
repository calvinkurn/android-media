package com.tokopedia.imagepicker.picker.main.builder;

import androidx.annotation.Nullable;

import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

/**
 * Created by hendry on 04/09/18.
 */
public class VideoPickerBuilder extends ImagePickerBuilder {
    public VideoPickerBuilder(String title, int maxFileSizeInKB, int minResolution,
                              @Nullable ImagePickerMultipleSelectionBuilder imagePickerMultipleSelectionBuilder) {
        super(title, new int[]{TYPE_GALLERY}, GalleryType.VIDEO_ONLY, maxFileSizeInKB,
                minResolution, ImageRatioTypeDef.ORIGINAL, false,
                null, imagePickerMultipleSelectionBuilder);
    }
}
