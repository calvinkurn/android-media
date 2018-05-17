package com.tokopedia.imagepicker.picker.main.builder;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerBuilder implements Parcelable{

    public static final int DEFAULT_MIN_RESOLUTION = 300;
    public static final int DEFAULT_MAX_IMAGE_SIZE_IN_KB = 15360; // 15 * 1024KB

    private String title;
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
    private boolean moveImageResultToLocal;
    private boolean hasPickerPreview;
    private int maximumNoPick;
    private long maxFileSizeInKB;

    public static ImagePickerBuilder getDefaultBuilder(Context context){
        return new ImagePickerBuilder(context.getString(R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_INSTAGRAM, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImageSelectionTypeDef.TYPE_MULTIPLE,
                false, 5, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, 1, 1, true, true,
                new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                true);
    }

    public ImagePickerBuilder(String title,
                       @ImagePickerTabTypeDef int[] imagePickerTabTypeDef,
                       @GalleryType int galleryType,
                       @ImageSelectionTypeDef int selectionType,
                       boolean hasPickerPreview,
                       int maximumNoPick, int maxFileSizeInKB,
                       int minResolution,
                       int ratioX, int ratioY, boolean moveImageResultToLocal) {
        this.title = title;
        this.tabTypeDef = imagePickerTabTypeDef;
        this.galleryType = galleryType;
        this.imageSelectionType = selectionType;
        this.hasPickerPreview = hasPickerPreview;
        this.maximumNoPick = maximumNoPick;
        this.maxFileSizeInKB = maxFileSizeInKB;
        this.minResolution = minResolution;
        this.ratioX = ratioX;
        this.ratioY = ratioY;
        this.moveImageResultToLocal = moveImageResultToLocal;
        this.continueToEditAfterPick = false;
    }

    public ImagePickerBuilder(String title,
                       @ImagePickerTabTypeDef int[] imagePickerTabTypeDef,
                       @GalleryType int galleryType,
                       @ImageSelectionTypeDef int selectionType,
                       boolean hasPickerPreview,
                       int maximumNoPick,
                       int maxFileSizeInKB,
                       int minResolution,
                       int ratioX, int ratioY,
                       boolean moveImageResultToLocal,
                       boolean continueToEditAfterPick,
                       @ImageEditActionTypeDef int[] imageEditActionType,
                       boolean circlePreview) {
        this(title, imagePickerTabTypeDef, galleryType, selectionType, hasPickerPreview,
                maximumNoPick, maxFileSizeInKB,
                minResolution, ratioX, ratioY, moveImageResultToLocal);
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

    public String getTitle() {
        return title;
    }

    public boolean isMoveImageResultToLocal() {
        return moveImageResultToLocal;
    }

    public boolean isHasPickerPreview() {
        return hasPickerPreview;
    }

    public int getMaximumNoPick() {
        return maximumNoPick;
    }

    public long getMaxFileSizeInKB() {
        return maxFileSizeInKB;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeIntArray(this.tabTypeDef);
        dest.writeInt(this.galleryType);
        dest.writeInt(this.minResolution);
        dest.writeInt(this.imageSelectionType);
        dest.writeByte(this.continueToEditAfterPick ? (byte) 1 : (byte) 0);
        dest.writeIntArray(this.imageEditActionType);
        dest.writeByte(this.circlePreview ? (byte) 1 : (byte) 0);
        dest.writeInt(this.ratioX);
        dest.writeInt(this.ratioY);
        dest.writeByte(this.moveImageResultToLocal ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasPickerPreview ? (byte) 1 : (byte) 0);
        dest.writeInt(this.maximumNoPick);
        dest.writeLong(this.maxFileSizeInKB);
    }

    protected ImagePickerBuilder(Parcel in) {
        this.title = in.readString();
        this.tabTypeDef = in.createIntArray();
        this.galleryType = in.readInt();
        this.minResolution = in.readInt();
        this.imageSelectionType = in.readInt();
        this.continueToEditAfterPick = in.readByte() != 0;
        this.imageEditActionType = in.createIntArray();
        this.circlePreview = in.readByte() != 0;
        this.ratioX = in.readInt();
        this.ratioY = in.readInt();
        this.moveImageResultToLocal = in.readByte() != 0;
        this.hasPickerPreview = in.readByte() != 0;
        this.maximumNoPick = in.readInt();
        this.maxFileSizeInKB = in.readLong();
    }

    public static final Creator<ImagePickerBuilder> CREATOR = new Creator<ImagePickerBuilder>() {
        @Override
        public ImagePickerBuilder createFromParcel(Parcel source) {
            return new ImagePickerBuilder(source);
        }

        @Override
        public ImagePickerBuilder[] newArray(int size) {
            return new ImagePickerBuilder[size];
        }
    };
}
