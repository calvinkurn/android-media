package com.tokopedia.imagepicker.picker.main.builder;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;

import java.util.ArrayList;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerBuilder implements Parcelable {

    public static final int DEFAULT_MIN_RESOLUTION = 300;
    public static final int IMAGE_SEARCH_MIN_RESOLUTION = 200;
    public static final int DEFAULT_MAX_IMAGE_SIZE_IN_KB = 15360; // 15 * 1024KB

    private String title;
    private @ImagePickerTabTypeDef
    int[] tabTypeDef;
    private @GalleryType
    int galleryType;
    private int minResolution;

    private ImageRatioTypeDef imageRatioTypeDef;
    private boolean moveImageResultToLocal;

    private long maxFileSizeInKB;

    private ImagePickerEditorBuilder imagePickerEditorBuilder;
    private ImagePickerMultipleSelectionBuilder imagePickerMultipleSelectionBuilder;

    public static ImagePickerBuilder getDefaultBuilder(Context context) {
        return new ImagePickerBuilder(context.getString(R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM}, GalleryType.IMAGE_ONLY,
                DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                ImagePickerEditorBuilder.getDefaultBuilder(),
                ImagePickerMultipleSelectionBuilder.getDefaultBuilder());
    }

    public ImagePickerBuilder(String title,
                              @ImagePickerTabTypeDef int[] imagePickerTabTypeDef,
                              @GalleryType int galleryType,
                              int maxFileSizeInKB,
                              int minResolution,
                              ImageRatioTypeDef imageRatioTypeDef,
                              boolean moveImageResultToLocal,
                              @Nullable ImagePickerEditorBuilder imagePickerEditorBuilder,
                              @Nullable ImagePickerMultipleSelectionBuilder imagePickerMultipleSelectionBuilder) {
        this.title = title;
        this.tabTypeDef = imagePickerTabTypeDef;
        this.galleryType = galleryType;
        this.maxFileSizeInKB = maxFileSizeInKB;
        this.minResolution = minResolution;
        this.imageRatioTypeDef = imageRatioTypeDef == null ?
                ImageRatioTypeDef.ORIGINAL :
                imageRatioTypeDef;
        this.moveImageResultToLocal = moveImageResultToLocal;
        this.imagePickerEditorBuilder = imagePickerEditorBuilder;
        this.imagePickerMultipleSelectionBuilder = imagePickerMultipleSelectionBuilder;
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

    public boolean supportMultipleSelection() {
        return imagePickerMultipleSelectionBuilder!= null;
    }

    public int getMinResolution() {
        return minResolution;
    }

    public int getRatioX() {
        return imageRatioTypeDef.getRatioX();
    }

    public int getRatioY() {
        return imageRatioTypeDef.getRatioY();
    }

    public boolean isContinueToEditAfterPick() {
        return imagePickerEditorBuilder != null;
    }

    public int[] getImageEditActionType() {
        if (imagePickerEditorBuilder != null) {
            return imagePickerEditorBuilder.getImageEditActionType();
        }
        return null;
    }

    public ImageRatioTypeDef getImageRatioTypeDef() {
        return imageRatioTypeDef;
    }

    public boolean isCirclePreview() {
        return imagePickerEditorBuilder != null && imagePickerEditorBuilder.isCirclePreview();
    }

    public String getTitle() {
        return title;
    }

    public boolean isMoveImageResultToLocal() {
        return moveImageResultToLocal;
    }

    public int getMaximumNoPick() {
        if (imagePickerMultipleSelectionBuilder!= null) {
            return imagePickerMultipleSelectionBuilder.getMaximumNoPick();
        }
        return 1;
    }

    public ArrayList<String> getInitialSelectedImagePathList(){
        if (imagePickerMultipleSelectionBuilder!= null) {
            return imagePickerMultipleSelectionBuilder.getInitialSelectedImagePathList();
        }
        return new ArrayList<>();
    }

    public ImagePickerMultipleSelectionBuilder getImagePickerMultipleSelectionBuilder() {
        return imagePickerMultipleSelectionBuilder;
    }

    public @Nullable ArrayList<ImageRatioTypeDef> getRatioOptionList() {
        if (imagePickerEditorBuilder != null){
            return imagePickerEditorBuilder.getImageRatioTypeDefs();
        } else {
            return null;
        }
    }

    public long getMaxFileSizeInKB() {
        return maxFileSizeInKB;
    }

    public void setMaxFileSizeInKB(long maxFileSizeInKB) {
        this.maxFileSizeInKB = maxFileSizeInKB;
    }

    public void setImagePickerMultipleSelectionBuilder(ImagePickerMultipleSelectionBuilder imagePickerMultipleSelectionBuilder) {
        this.imagePickerMultipleSelectionBuilder = imagePickerMultipleSelectionBuilder;
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
        dest.writeInt(this.imageRatioTypeDef == null ? -1 : this.imageRatioTypeDef.ordinal());
        dest.writeByte(this.moveImageResultToLocal ? (byte) 1 : (byte) 0);
        dest.writeLong(this.maxFileSizeInKB);
        dest.writeParcelable(this.imagePickerEditorBuilder, flags);
        dest.writeParcelable(this.imagePickerMultipleSelectionBuilder, flags);
    }

    protected ImagePickerBuilder(Parcel in) {
        this.title = in.readString();
        this.tabTypeDef = in.createIntArray();
        this.galleryType = in.readInt();
        this.minResolution = in.readInt();
        int tmpImageRatioTypeDef = in.readInt();
        this.imageRatioTypeDef = tmpImageRatioTypeDef == -1 ? null : ImageRatioTypeDef.values()[tmpImageRatioTypeDef];
        this.moveImageResultToLocal = in.readByte() != 0;
        this.maxFileSizeInKB = in.readLong();
        this.imagePickerEditorBuilder = in.readParcelable(ImagePickerEditorBuilder.class.getClassLoader());
        this.imagePickerMultipleSelectionBuilder = in.readParcelable(ImagePickerMultipleSelectionBuilder.class.getClassLoader());
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
