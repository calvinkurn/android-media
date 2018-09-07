package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nisie on 2/16/16.
 */
public class ImageUpload implements Parcelable{

    String imageId;
    String fileLoc;
    String picSrc;
    String picSrcLarge;
    String picObj;
    int position;
    String description;
    boolean isSelected = false;

    public ImageUpload() {

    }

    public ImageUpload(String picSrc, String picSrcLarge, String description, String imageId) {
        this.picSrcLarge = picSrcLarge;
        this.picSrc = picSrc;
        this.description = description;
        this.imageId = imageId;
    }


    protected ImageUpload(Parcel in) {
        imageId = in.readString();
        fileLoc = in.readString();
        picSrc = in.readString();
        picSrcLarge = in.readString();
        picObj = in.readString();
        position = in.readInt();
        description = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<ImageUpload> CREATOR = new Creator<ImageUpload>() {
        @Override
        public ImageUpload createFromParcel(Parcel in) {
            return new ImageUpload(in);
        }

        @Override
        public ImageUpload[] newArray(int size) {
            return new ImageUpload[size];
        }
    };

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getFileLoc() {
        return fileLoc;
    }

    public void setFileLoc(String fileLoc) {
        this.fileLoc = fileLoc;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getPicSrcLarge() {
        return picSrcLarge;
    }

    public void setPicSrcLarge(String picSrcLarge) {
        this.picSrcLarge = picSrcLarge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageId);
        dest.writeString(fileLoc);
        dest.writeString(picSrc);
        dest.writeString(picSrcLarge);
        dest.writeString(picObj);
        dest.writeInt(position);
        dest.writeString(description);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
