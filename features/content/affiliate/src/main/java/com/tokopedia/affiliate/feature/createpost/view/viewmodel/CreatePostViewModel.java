package com.tokopedia.affiliate.feature.createpost.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;

public class CreatePostViewModel implements Parcelable {
    private static final String FILE_PREFIX = "file:";

    private String pdpImage;
    private ArrayList<String> imageList;

    public CreatePostViewModel() {
        imageList = new ArrayList<>();
    }

    public void setPdpImage(String pdpImage) {
        this.pdpImage = pdpImage;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    /*
    Get complete image list with PDP image in the end
    */
    public ArrayList<String> getCompleteList() {
        ArrayList<String> completeList = new ArrayList<>(imageList);
        completeList.add(pdpImage);
        return completeList;
    }

    /*
    Get image list without PDP image
    */
    public ArrayList<String> getImageList() {
        return imageList;
    }

    /*
    Get only locally stored image, not from server
    */
    public ArrayList<String> getFileImageList() {
        ArrayList<String> fileImageList = new ArrayList<>();
        for (String image : imageList) {
            if (urlIsFile(image)) {
                fileImageList.add(image);
            }
        }
        return fileImageList;
    }

    public static boolean urlIsFile(String input) {
        if (input.startsWith(FILE_PREFIX)) return true;
        try {
            return new File(input).exists();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pdpImage);
        dest.writeStringList(this.imageList);
    }

    protected CreatePostViewModel(Parcel in) {
        this.pdpImage = in.readString();
        this.imageList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<CreatePostViewModel> CREATOR = new Parcelable
            .Creator<CreatePostViewModel>() {
        @Override
        public CreatePostViewModel createFromParcel(Parcel source) {
            return new CreatePostViewModel(source);
        }

        @Override
        public CreatePostViewModel[] newArray(int size) {
            return new CreatePostViewModel[size];
        }
    };
}
