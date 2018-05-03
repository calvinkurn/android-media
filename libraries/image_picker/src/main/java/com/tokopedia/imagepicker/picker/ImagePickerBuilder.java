package com.tokopedia.imagepicker.picker;

import android.support.annotation.IntDef;

import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;

import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ExpectedImageRatioDef.TYPE_1_1;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ExpectedImageRatioDef.TYPE_4_5;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ExpectedImageRatioDef.TYPE_5_4;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP_ROTATE;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_ROTATE;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_WATERMARK;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageSelectionTypeDef.TYPE_MULTIPLE_NO_PREVIEW;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageSelectionTypeDef.TYPE_MULTIPLE_WITH_PREVIEW;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageSelectionTypeDef.TYPE_SINGLE;

/**
 * Created by hendry on 19/04/18.
 */

public enum ImagePickerBuilder {
    ADD_PRODUCT(new int[]{TYPE_GALLERY, TYPE_INSTAGRAM, TYPE_CAMERA},
            GalleryType.IMAGE_ONLY,
            ImageSelectionTypeDef.TYPE_SINGLE,
            300,
            TYPE_1_1,
            true,
            new int[]{TYPE_CROP_ROTATE});

    private @ImagePickerTabTypeDef
    int[] tabTypeDef;
    private @GalleryType
    int galleryType;
    private int minResolution;
    private @ExpectedImageRatioDef
    int expectedImageRatio;
    private @ImageSelectionTypeDef
    int imageSelectionType;
    private boolean continueToEditAfterPick;
    private @ImageEditActionTypeDef
    int[] imageEditActionType;

    @IntDef({TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM})
    public @interface ImagePickerTabTypeDef {
        int TYPE_GALLERY = 1;
        int TYPE_CAMERA = 2;
        int TYPE_INSTAGRAM = 3;
    }

    @IntDef({TYPE_CROP, TYPE_ROTATE, TYPE_WATERMARK, TYPE_CROP_ROTATE})
    public @interface ImageEditActionTypeDef {
        int TYPE_CROP = 1;
        int TYPE_ROTATE = 2;
        int TYPE_WATERMARK = 3;
        int TYPE_CROP_ROTATE = 4;
    }

    @IntDef({TYPE_SINGLE, TYPE_MULTIPLE_NO_PREVIEW, TYPE_MULTIPLE_WITH_PREVIEW})
    public @interface ImageSelectionTypeDef {
        int TYPE_SINGLE = 1;
        int TYPE_MULTIPLE_NO_PREVIEW = 2;
        int TYPE_MULTIPLE_WITH_PREVIEW = 3;
    }

    @IntDef({TYPE_1_1, TYPE_4_5, TYPE_5_4})
    public @interface ExpectedImageRatioDef {
        int TYPE_1_1 = 1;
        int TYPE_4_5 = 2;
        int TYPE_5_4 = 3;
    }

    private ImagePickerBuilder(@ImagePickerTabTypeDef int[] imagePickerTabTypeDef,
                               @GalleryType int galleryType,
                               @ImageSelectionTypeDef int selectionType,
                               int minResolution,
                               @ExpectedImageRatioDef int expectedImageRatio) {
        this.tabTypeDef = imagePickerTabTypeDef;
        this.galleryType = galleryType;
        this.imageSelectionType = selectionType;
        this.minResolution = minResolution;
        this.expectedImageRatio = expectedImageRatio;
        this.continueToEditAfterPick = false;
    }

    private ImagePickerBuilder(@ImagePickerTabTypeDef int[] imagePickerTabTypeDef,
                               @GalleryType int galleryType,
                               @ImageSelectionTypeDef int selectionType,
                               int minResolution,
                               @ExpectedImageRatioDef int expectedImageRatio,
                               boolean continueToEditAfterPick,
                               @ImageEditActionTypeDef int[] imageEditActionType) {
        this(imagePickerTabTypeDef, galleryType, selectionType, minResolution, expectedImageRatio);
        this.continueToEditAfterPick = continueToEditAfterPick;
        this.imageEditActionType = imageEditActionType;
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

    public int getImageSelectionType() {
        return imageSelectionType;
    }

    public boolean supportMultipleSelection() {
        return imageSelectionType != ImageSelectionTypeDef.TYPE_SINGLE;
    }

    public int getMinResolution() {
        return minResolution;
    }

    public int getExpectedImageRatio() {
        return expectedImageRatio;
    }

    public boolean isContinueToEditAfterPick() {
        return continueToEditAfterPick;
    }

    public int[] getImageEditActionType() {
        return imageEditActionType;
    }
}
