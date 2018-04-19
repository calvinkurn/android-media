package com.tokopedia.imagepicker;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import static com.tokopedia.imagepicker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

/**
 * Created by hendry on 19/04/18.
 */

public enum  ImagePickerBuilder {
    ADD_PRODUCT (new int[]{TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM});

    private @ImagePickerTabTypeDef int[] tabTypeDef;

    @IntDef({TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM})
    public @interface ImagePickerTabTypeDef {
        int TYPE_GALLERY = 1;
        int TYPE_CAMERA = 2;
        int TYPE_INSTAGRAM = 3;
    }

    private ImagePickerBuilder(@ImagePickerTabTypeDef int[] imagePickerTabTypeDef) {
        this.tabTypeDef = imagePickerTabTypeDef;
    }

    public int[] getTabTypeDef() {
        return tabTypeDef;
    }

    public int getTabTypeDef(int index) {
        return tabTypeDef[index];
    }

    public void setTabTypeDef(int[] tabTypeDef) {
        this.tabTypeDef = tabTypeDef;
    }

}
