package com.tokopedia.core.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * Created on 4/20/16.
 */
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class AttachmentResCenterDB extends BaseModel implements Parcelable {

    public static final String MODULE_CREATE_RESCENTER = "0";
    public static final String MODULE_REJECT_ADMIN_SOLUTION = "1";
    public static final String MODULE_DETAIL_RESCENTER = "2";
    public static final String MODULE_EDIT_RESCENTER = "3";
    public static final String MODULE_SHIPPING_RESCENTER = "4";

    @PrimaryKey(autoincrement = true)
    @Column
    long id;

    @Column
    public String resolutionID;

    @Column
    public String orderID;

    @Column
    public String imagePath;

    @Column
    public String imageUrl;

    @Column
    public String modulName;

    // start --------------------> New Upload Attachment
    @Column
    public String imageUUID;

    @Column
    public String picSrc;

    @Column
    public String picObj;
    // end   --------------------> New Upload Attachment

    public long getId() {
        return id;
    }

    public AttachmentResCenterDB() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.resolutionID);
        dest.writeString(this.orderID);
        dest.writeString(this.imagePath);
        dest.writeString(this.imageUrl);
        dest.writeString(this.modulName);
        dest.writeString(this.imageUUID);
    }

    protected AttachmentResCenterDB(Parcel in) {
        this.id = in.readLong();
        this.resolutionID = in.readString();
        this.orderID = in.readString();
        this.imagePath = in.readString();
        this.imageUrl = in.readString();
        this.modulName = in.readString();
        this.imageUUID = in.readString();
    }

    public static final Creator<AttachmentResCenterDB> CREATOR = new Creator<AttachmentResCenterDB>() {
        @Override
        public AttachmentResCenterDB createFromParcel(Parcel source) {
            return new AttachmentResCenterDB(source);
        }

        @Override
        public AttachmentResCenterDB[] newArray(int size) {
            return new AttachmentResCenterDB[size];
        }
    };
}