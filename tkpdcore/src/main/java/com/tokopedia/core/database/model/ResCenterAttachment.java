package com.tokopedia.core.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.tokopedia.core.database.DbMetadata;

@Entity(tableName = DbMetadata.resCenterTableName)
public class ResCenterAttachment implements Parcelable {

    @Ignore
    public static final String MODULE_CREATE_RESCENTER = "0";

    @Ignore
    public static final String MODULE_REJECT_ADMIN_SOLUTION = "1";

    @Ignore
    public static final String MODULE_DETAIL_RESCENTER = "2";

    @Ignore
    public static final String MODULE_EDIT_RESCENTER = "3";

    @Ignore
    public static final String MODULE_SHIPPING_RESCENTER = "4";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private String resolutionId;
    private String orderId;
    private String imagePath;
    private String imageUrl;
    private String moduleName;
    private String imageUuid;
    private String picSrc;
    private String picObj;

    public ResCenterAttachment() {
    }

    public ResCenterAttachment(Parcel parcel) {
        this.id = parcel.readLong();
        this.resolutionId = parcel.readString();
        this.orderId = parcel.readString();
        this.imagePath = parcel.readString();
        this.imageUrl = parcel.readString();
        this.moduleName = parcel.readString();
        this.imageUuid = parcel.readString();
        this.picSrc = parcel.readString();
        this.picObj = parcel.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(String resolutionId) {
        this.resolutionId = resolutionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getImageUuid() {
        return imageUuid;
    }

    public void setImageUuid(String imageUuid) {
        this.imageUuid = imageUuid;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.id);
        parcel.writeString(this.resolutionId);
        parcel.writeString(this.orderId);
        parcel.writeString(this.imagePath);
        parcel.writeString(this.imageUrl);
        parcel.writeString(this.moduleName);
        parcel.writeString(this.imageUuid);
        parcel.writeString(this.picSrc);
        parcel.writeString(this.picObj);
    }
}
