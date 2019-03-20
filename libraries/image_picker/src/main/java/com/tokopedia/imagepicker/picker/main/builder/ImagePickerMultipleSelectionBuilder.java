package com.tokopedia.imagepicker.picker.main.builder;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> initialSelectedImagePathList;

    // show the placeholder when the image is not selected.
    // for ex: (R.drawable.primary_example, R.drawable.product_in_use}
    private List<Integer> placeholderImagePathResList;

    public static ImagePickerMultipleSelectionBuilder getDefaultBuilder(){
        return new ImagePickerMultipleSelectionBuilder(
                null,
                null,
                0,
                DEFAULT_MAXIMUM_NO_PICK);
    }

    public ImagePickerMultipleSelectionBuilder(List<String> initialSelectedImagePathList,
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

    public boolean isCanReorder() {
        return canReorder;
    }

    private void setInitialSelectedImagePathList(List<String> initialSelectedImagePathList){
        if (initialSelectedImagePathList == null) {
            this.initialSelectedImagePathList = new ArrayList<>();
        } else {
            this.initialSelectedImagePathList = initialSelectedImagePathList;
        }
    }

    public int getMaximumNoPick() {
        return maximumNoPick;
    }

    public List<String> getInitialSelectedImagePathList() {
        return initialSelectedImagePathList;
    }

    public int getPrimaryImageStringRes() {
        return primaryImageStringRes;
    }

    public List<Integer> getPlaceholderImagePathResList() {
        return placeholderImagePathResList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.primaryImageStringRes);
        dest.writeInt(this.maximumNoPick);
        dest.writeByte(this.canReorder ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.initialSelectedImagePathList);
        dest.writeList(this.placeholderImagePathResList);
    }

    protected ImagePickerMultipleSelectionBuilder(Parcel in) {
        this.primaryImageStringRes = in.readInt();
        this.maximumNoPick = in.readInt();
        this.canReorder = in.readByte() != 0;
        this.initialSelectedImagePathList = in.createStringArrayList();
        this.placeholderImagePathResList = new ArrayList<Integer>();
        in.readList(this.placeholderImagePathResList, Integer.class.getClassLoader());
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
}
