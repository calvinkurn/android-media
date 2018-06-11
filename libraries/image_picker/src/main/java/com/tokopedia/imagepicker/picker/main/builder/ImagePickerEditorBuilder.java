package com.tokopedia.imagepicker.picker.main.builder;

import android.os.Parcel;
import android.os.Parcelable;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;

/**
 * Created by hendry on 24/05/18.
 */

public class ImagePickerEditorBuilder implements Parcelable {
    private @ImageEditActionTypeDef
    int[] imageEditActionType;
    private boolean circlePreview;

    public static ImagePickerEditorBuilder getDefaultBuilder(){
        return new ImagePickerEditorBuilder(
                new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                true);
    }

    public ImagePickerEditorBuilder(@ImageEditActionTypeDef int[] imageEditActionType,
                                    boolean circlePreview) {
        this.imageEditActionType = imageEditActionType;
        this.circlePreview = circlePreview;
    }

    public int[] getImageEditActionType() {
        return imageEditActionType;
    }

    public boolean isCirclePreview() {
        return circlePreview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.imageEditActionType);
        dest.writeByte(this.circlePreview ? (byte) 1 : (byte) 0);
    }

    protected ImagePickerEditorBuilder(Parcel in) {
        this.imageEditActionType = in.createIntArray();
        this.circlePreview = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ImagePickerEditorBuilder> CREATOR = new Parcelable.Creator<ImagePickerEditorBuilder>() {
        @Override
        public ImagePickerEditorBuilder createFromParcel(Parcel source) {
            return new ImagePickerEditorBuilder(source);
        }

        @Override
        public ImagePickerEditorBuilder[] newArray(int size) {
            return new ImagePickerEditorBuilder[size];
        }
    };
}
