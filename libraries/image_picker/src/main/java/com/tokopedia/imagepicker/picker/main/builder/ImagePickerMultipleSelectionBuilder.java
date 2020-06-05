package com.tokopedia.imagepicker.picker.main.builder;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by hendry on 24/05/18.
 */

public class ImagePickerMultipleSelectionBuilder implements Parcelable {
    public static final int DEFAULT_MAXIMUM_NO_PICK = 5;

    private int primaryImageStringRes;
    private int maximumNoPick;
    private boolean canReorder;

    // to show initial selected image onlist;
    // for example: ("sdcard/image.jpg", "sdcard/image2.jpg"}
    private ArrayList<String> initialSelectedImagePathList;

    // show the placeholder when the image is not selected.
    // for ex: (R.drawable.primary_example, R.drawable.product_in_use}
    private ArrayList<Integer> placeholderImagePathResList;

    @Nullable
    private PreviewExtension previewExtension;

    public static class PreviewExtension implements Parcelable {
        // usually when the user already select the image, it will show the preview at the bottom
        // this option is to hide/show it
        boolean hideThumbnailListPreview = false;

        // show counter "1", "2" when selecting image
        boolean showCounterAtSelectedImage = false;

        // in gallery, if select true, will make the span smaller, resulting in bigger image
        boolean showBiggerPreviewWhenThumbnailHidden = true;

        // in set to true, will append the selected Images in gallery (all albums)
        // example: previously user has select "data/image1.png".
        // this image will be appended at the gallery in the first rows.
        boolean appendInitialSelectedImageInGallery = false;

        public static PreviewExtension createPreview() {
            return new PreviewExtension(false, false, true, false);
        }

        public static PreviewExtension createNoPreview() {
            return new PreviewExtension(true, true, true, true);
        }

        public PreviewExtension(boolean hideThumbnailListPreview, boolean showCounterAtSelectedImage,
                                boolean showBiggerPreviewWhenThumbnailHidden, boolean appendInitialSelectedImageInGallery) {
            this.hideThumbnailListPreview = hideThumbnailListPreview;
            this.showCounterAtSelectedImage = showCounterAtSelectedImage;
            this.showBiggerPreviewWhenThumbnailHidden = showBiggerPreviewWhenThumbnailHidden;
            this.appendInitialSelectedImageInGallery = appendInitialSelectedImageInGallery;
        }

        protected PreviewExtension(Parcel in) {
            hideThumbnailListPreview = in.readByte() != 0;
            showCounterAtSelectedImage = in.readByte() != 0;
            showBiggerPreviewWhenThumbnailHidden = in.readByte() != 0;
            appendInitialSelectedImageInGallery = in.readByte() != 0;
        }

        public static final Creator<PreviewExtension> CREATOR = new Creator<PreviewExtension>() {
            @Override
            public PreviewExtension createFromParcel(Parcel in) {
                return new PreviewExtension(in);
            }

            @Override
            public PreviewExtension[] newArray(int size) {
                return new PreviewExtension[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (hideThumbnailListPreview ? 1 : 0));
            dest.writeByte((byte) (showCounterAtSelectedImage ? 1 : 0));
            dest.writeByte((byte) (showBiggerPreviewWhenThumbnailHidden ? 1 : 0));
            dest.writeByte((byte) (appendInitialSelectedImageInGallery ? 1 : 0));
        }
    }

    public static ImagePickerMultipleSelectionBuilder getDefaultBuilder() {
        return new ImagePickerMultipleSelectionBuilder(
                null,
                null,
                0,
                DEFAULT_MAXIMUM_NO_PICK);
    }

    public ImagePickerMultipleSelectionBuilder(ArrayList<String> initialSelectedImagePathList,
                                               @DrawableRes ArrayList<Integer> placeholderImagePathResList,
                                               int primaryImageStringRes,
                                               int maximumNoPick) {
        this.maximumNoPick = maximumNoPick;
        setInitialSelectedImagePathList(initialSelectedImagePathList);
        this.placeholderImagePathResList = placeholderImagePathResList;
        this.primaryImageStringRes = primaryImageStringRes;
    }

    public ImagePickerMultipleSelectionBuilder(ArrayList<String> initialSelectedImagePathList,
                                               @DrawableRes ArrayList<Integer> placeholderImagePathResList,
                                               int primaryImageStringRes,
                                               int maximumNoPick,
                                               boolean canReorder) {
        this(initialSelectedImagePathList, placeholderImagePathResList, primaryImageStringRes, maximumNoPick);
        this.canReorder = canReorder;
    }

    public ImagePickerMultipleSelectionBuilder(ArrayList<String> initialSelectedImagePathList,
                                               @DrawableRes ArrayList<Integer> placeholderImagePathResList,
                                               int primaryImageStringRes,
                                               int maximumNoPick,
                                               boolean canReorder,
                                               PreviewExtension previewExtension) {
        this(initialSelectedImagePathList, placeholderImagePathResList, primaryImageStringRes, maximumNoPick);
        this.canReorder = canReorder;
        this.previewExtension = previewExtension;
    }

    public boolean isCanReorder() {
        return canReorder;
    }

    private void setInitialSelectedImagePathList(ArrayList<String> initialSelectedImagePathList) {
        if (initialSelectedImagePathList == null) {
            this.initialSelectedImagePathList = new ArrayList<>();
        } else {
            this.initialSelectedImagePathList = initialSelectedImagePathList;
        }
    }

    public int getMaximumNoPick() {
        return maximumNoPick;
    }

    public ArrayList<String> getInitialSelectedImagePathList() {
        return initialSelectedImagePathList;
    }

    public int getPrimaryImageStringRes() {
        return primaryImageStringRes;
    }

    public ArrayList<Integer> getPlaceholderImagePathResList() {
        return placeholderImagePathResList;
    }

    public void setMaximumNoPick(int maximumNoPick) {
        this.maximumNoPick = maximumNoPick;
    }

    @NonNull
    public PreviewExtension getPreviewExtension() {
        if (previewExtension == null) {
            previewExtension = PreviewExtension.createPreview();
        }
        return previewExtension;
    }

    protected ImagePickerMultipleSelectionBuilder(Parcel in) {
        primaryImageStringRes = in.readInt();
        maximumNoPick = in.readInt();
        canReorder = in.readByte() != 0;
        initialSelectedImagePathList = in.createStringArrayList();
        previewExtension = in.readParcelable(PreviewExtension.class.getClassLoader());
    }

    public static final Creator<ImagePickerMultipleSelectionBuilder> CREATOR = new Creator<ImagePickerMultipleSelectionBuilder>() {
        @Override
        public ImagePickerMultipleSelectionBuilder createFromParcel(Parcel source) {
            return new ImagePickerMultipleSelectionBuilder(source);
        }

        @Override
        public ImagePickerMultipleSelectionBuilder[] newArray(int size) {
            return new ImagePickerMultipleSelectionBuilder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryImageStringRes);
        dest.writeInt(maximumNoPick);
        dest.writeByte((byte) (canReorder ? 1 : 0));
        dest.writeStringList(initialSelectedImagePathList);
        dest.writeParcelable(previewExtension, flags);
    }

}
