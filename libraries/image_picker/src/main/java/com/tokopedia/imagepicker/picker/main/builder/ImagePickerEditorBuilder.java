package com.tokopedia.imagepicker.picker.main.builder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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
    private ArrayList<ImageRatioTypeDef> imageRatioTypeDefs;
    private String belowMinResolutionErrorMessage;
    private String imageTooLargeErrorMessage;
    private boolean recheckSizeAfterResize;


    public static ImagePickerEditorBuilder getDefaultBuilder(){
        return new ImagePickerEditorBuilder(
                new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                false,
                null);
    }

    public ImagePickerEditorBuilder(@ImageEditActionTypeDef int[] imageEditActionType,
                                    boolean circlePreview,
                                    ArrayList<ImageRatioTypeDef> imageRatioTypeDefs) {
        this.imageEditActionType = imageEditActionType;
        this.circlePreview = circlePreview;
        this.imageRatioTypeDefs = imageRatioTypeDefs;
    }

    public int[] getImageEditActionType() {
        return imageEditActionType;
    }

    public boolean isCirclePreview() {
        return circlePreview;
    }

    public ArrayList<ImageRatioTypeDef> getImageRatioTypeDefs() {
        return imageRatioTypeDefs;
    }

    public void setBelowMinResolutionErrorMessage(String errorMessage) {
        this.belowMinResolutionErrorMessage = errorMessage;
    }

    public String getBelowMinResolutionErrorMessage() {
        return this.belowMinResolutionErrorMessage;
    }

    public String getImageTooLargeErrorMessage() {
        return imageTooLargeErrorMessage;
    }

    public void setImageTooLargeErrorMessage(String imageTooLargeErrorMessage) {
        this.imageTooLargeErrorMessage = imageTooLargeErrorMessage;
    }

    public boolean isRecheckSizeAfterResize() {
        return recheckSizeAfterResize;
    }

    public void setRecheckSizeAfterResize(boolean recheckSizeAfterResize) {
        this.recheckSizeAfterResize = recheckSizeAfterResize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.imageEditActionType);
        dest.writeByte(this.circlePreview ? (byte) 1 : (byte) 0);
        dest.writeList(this.imageRatioTypeDefs);
        dest.writeString(this.belowMinResolutionErrorMessage);
        dest.writeString(this.imageTooLargeErrorMessage);
    }

    protected ImagePickerEditorBuilder(Parcel in) {
        this.imageEditActionType = in.createIntArray();
        this.circlePreview = in.readByte() != 0;
        this.imageRatioTypeDefs = new ArrayList<ImageRatioTypeDef>();
        in.readList(this.imageRatioTypeDefs, ImageRatioTypeDef.class.getClassLoader());
        this.belowMinResolutionErrorMessage = in.readString();
        this.imageTooLargeErrorMessage = in.readString();
    }

    public static final Creator<ImagePickerEditorBuilder> CREATOR = new Creator<ImagePickerEditorBuilder>() {
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
