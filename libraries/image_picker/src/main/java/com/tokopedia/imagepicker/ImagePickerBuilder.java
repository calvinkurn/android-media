package com.tokopedia.imagepicker;

import android.support.annotation.IntDef;

import com.tokopedia.imagepicker.gallery.type.GalleryType;

import static com.tokopedia.imagepicker.ImagePickerBuilder.CameraRatioDef.TYPE_1_1;
import static com.tokopedia.imagepicker.ImagePickerBuilder.CameraRatioDef.TYPE_3_4;
import static com.tokopedia.imagepicker.ImagePickerBuilder.CameraRatioDef.TYPE_4_3;
import static com.tokopedia.imagepicker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

/**
 * Created by hendry on 19/04/18.
 */

public enum ImagePickerBuilder {
    ADD_PRODUCT(new int[]{TYPE_GALLERY, TYPE_INSTAGRAM, TYPE_CAMERA},
            GalleryType.IMAGE_ONLY,
            false,
            false,
            300);

    private @ImagePickerTabTypeDef
    int[] tabTypeDef;
    private @GalleryType
    int galleryType;
    private boolean isMultipleSelection;
    private boolean hasThumbnailPreview;
    private int minResolution;

    @IntDef({TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM})
    public @interface ImagePickerTabTypeDef {
        int TYPE_GALLERY = 1;
        int TYPE_CAMERA = 2;
        int TYPE_INSTAGRAM = 3;
    }

    @IntDef({TYPE_1_1, TYPE_3_4, TYPE_4_3})
    public @interface CameraRatioDef {
        int TYPE_1_1 = 1;
        int TYPE_3_4 = 2;
        int TYPE_4_3 = 3;
    }

    private ImagePickerBuilder(@ImagePickerTabTypeDef int[] imagePickerTabTypeDef,
                               @GalleryType int galleryType,
                               boolean isMultipleSelection,
                               boolean hasThumbnailPreview,
                               int minResolution) {
        this.tabTypeDef = imagePickerTabTypeDef;
        this.galleryType = galleryType;
        this.isMultipleSelection = isMultipleSelection;
        this.hasThumbnailPreview = hasThumbnailPreview;
        this.minResolution = minResolution;
    }

    public int[] getTabTypeDef() {
        return tabTypeDef;
    }

    public int getTabTypeDef(int index) {
        return tabTypeDef[index];
    }

    public int indexTypeDef(@ImagePickerTabTypeDef int typeDefToLookFor) {
        if (tabTypeDef != null && tabTypeDef.length > 0) {
            for (int i = 0, sizei = tabTypeDef.length; i < sizei; i++) {
                int tabTypeDefItem = tabTypeDef[i];
                if (tabTypeDefItem == typeDefToLookFor) {
                    return i;
                }
            }
            return -1;
        } else {
            return -1;
        }
    }

    public boolean isTypeDef(@ImagePickerTabTypeDef int typeDefToCompare, int index) {
        return getTabTypeDef(index) == typeDefToCompare;
    }

    public int getGalleryType() {
        return galleryType;
    }

    public boolean hasThumbnailPreview() {
        return hasThumbnailPreview;
    }

    public boolean supportMultipleSelection() {
        return isMultipleSelection;
    }

    public int getMinResolution() {
        return minResolution;
    }
}
