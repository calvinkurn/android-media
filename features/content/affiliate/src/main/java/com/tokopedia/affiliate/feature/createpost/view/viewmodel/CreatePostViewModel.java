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
    private int maxImage = 5;
    private boolean isEdit = false;
    private ArrayList<String> fileImageList = new ArrayList<>();
    private ArrayList<String> urlImageList = new ArrayList<>();

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

    public int getMaxImage() {
        return maxImage;
    }

    public void setMaxImage(int maxImage) {
        this.maxImage = maxImage;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public ArrayList<String> getFileImageList() {
        return fileImageList;
    }

    public void setFileImageList(ArrayList<String> fileImageList) {
        this.fileImageList = fileImageList;
    }

    public ArrayList<String> getUrlImageList() {
        return urlImageList;
    }

    public void setUrlImageList(ArrayList<String> urlImageList) {
        this.urlImageList = urlImageList;
    }

    public ArrayList<String> getCompleteImageList() {
        ArrayList<String> completeImageList = new ArrayList<>();
        completeImageList.addAll(fileImageList);
        completeImageList.addAll(urlImageList);
        return completeImageList;
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
        dest.writeInt(this.maxImage);
        dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.fileImageList);
        dest.writeStringList(this.urlImageList);
    }

    public CreatePostViewModel() {
    }

    protected CreatePostViewModel(Parcel in) {
        this.productId = in.readString();
        this.adId = in.readString();
        this.postId = in.readString();
        this.token = in.readString();
        this.mainImageIndex = in.readInt();
        this.maxImage = in.readInt();
        this.isEdit = in.readByte() != 0;
        this.fileImageList = in.createStringArrayList();
        this.urlImageList = in.createStringArrayList();
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
