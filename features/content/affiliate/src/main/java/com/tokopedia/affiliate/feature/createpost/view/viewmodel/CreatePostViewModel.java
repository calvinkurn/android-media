package com.tokopedia.affiliate.feature.createpost.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;

public class CreatePostViewModel implements Parcelable {
    private static final String FILE_PREFIX = "file:";

    private String productId = "";
    private String adId = "";
    private String postId = "";
    private String token = "";
    private int mainImageIndex = 0;
    private boolean isEdit = false;
    private ArrayList<String> imageList = new ArrayList<>();

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getMainImageIndex() {
        return mainImageIndex;
    }

    public void setMainImageIndex(int mainImageIndex) {
        this.mainImageIndex = mainImageIndex;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    /*
    Get complete image list
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
        dest.writeString(this.productId);
        dest.writeString(this.adId);
        dest.writeString(this.postId);
        dest.writeString(this.token);
        dest.writeInt(this.mainImageIndex);
        dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.imageList);
    }

    public CreatePostViewModel() {
    }

    protected CreatePostViewModel(Parcel in) {
        this.productId = in.readString();
        this.adId = in.readString();
        this.postId = in.readString();
        this.token = in.readString();
        this.mainImageIndex = in.readInt();
        this.isEdit = in.readByte() != 0;
        this.imageList = in.createStringArrayList();
    }

    public static final Creator<CreatePostViewModel> CREATOR = new Creator<CreatePostViewModel>() {
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
