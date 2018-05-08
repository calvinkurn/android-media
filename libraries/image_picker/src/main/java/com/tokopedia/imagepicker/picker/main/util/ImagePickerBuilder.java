package com.tokopedia.imagepicker.picker.main.util;

import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;

import static com.tokopedia.imagepicker.picker.main.util.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.util.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.util.ImageEditActionTypeDef.ACTION_CROP_ROTATE;
import static com.tokopedia.imagepicker.picker.main.util.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.util.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.util.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

/**
 * Created by hendry on 19/04/18.
 */

public enum ImagePickerBuilder {
    ADD_PRODUCT(new int[]{TYPE_GALLERY, TYPE_INSTAGRAM, TYPE_CAMERA},
            GalleryType.IMAGE_ONLY,
            ImageSelectionTypeDef.TYPE_SINGLE,
            300,
            1,
            1,
            true,
            new int[]{ACTION_CROP_ROTATE, ACTION_BRIGHTNESS, ACTION_CONTRAST},
            true);

    private @ImagePickerTabTypeDef
    int[] tabTypeDef;
    private @GalleryType
    int galleryType;
    private int minResolution;
    private @ImageSelectionTypeDef
    int imageSelectionType;
    private boolean continueToEditAfterPick;
    private @ImageEditActionTypeDef
    int[] imageEditActionType;
    private boolean circlePreview;
    private int ratioX;
    private int ratioY;

    ImagePickerBuilder(@ImagePickerTabTypeDef int[] imagePickerTabTypeDef,
                       @GalleryType int galleryType,
                       @ImageSelectionTypeDef int selectionType,
                       int minResolution,
                       int ratioX, int ratioY) {
        this.tabTypeDef = imagePickerTabTypeDef;
        this.galleryType = galleryType;
        this.imageSelectionType = selectionType;
        this.minResolution = minResolution;
        this.ratioX = ratioX;
        this.ratioY = ratioY;
        this.continueToEditAfterPick = false;
    }

    ImagePickerBuilder(@ImagePickerTabTypeDef int[] imagePickerTabTypeDef,
                       @GalleryType int galleryType,
                       @ImageSelectionTypeDef int selectionType,
                       int minResolution,
                       int ratioX, int ratioY,
                       boolean continueToEditAfterPick,
                       @ImageEditActionTypeDef int[] imageEditActionType,
                       boolean circlePreview) {
        this(imagePickerTabTypeDef, galleryType, selectionType, minResolution, ratioX, ratioY);
        this.continueToEditAfterPick = continueToEditAfterPick;
        this.imageEditActionType = imageEditActionType;
        this.circlePreview = circlePreview;
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

    public int getRatioX() {
        return ratioX;
    }

    public int getRatioY() {
        return ratioY;
    }

    public boolean isContinueToEditAfterPick() {
        return continueToEditAfterPick;
    }

    public int[] getImageEditActionType() {
        return imageEditActionType;
    }

    public boolean isCirclePreview() {
        return circlePreview;
    }
}
