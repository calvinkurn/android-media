package com.tokopedia.core.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 4/4/16.
 */
public class ResCenterInboxItem implements Parcelable {

    public static final int TYPE_MAIN = 0;
    public static final int TYPE_NO_RESULT = 1;
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_LOADING = 3;
    public static final int TYPE_RETRY = 4;

    private int itemType;

    public static final Creator<ResCenterInboxItem> CREATOR = new Creator<ResCenterInboxItem>() {
        @Override
        public ResCenterInboxItem createFromParcel(Parcel in) {
            return new ResCenterInboxItem(in);
        }

        @Override
        public ResCenterInboxItem[] newArray(int size) {
            return new ResCenterInboxItem[size];
        }
    };

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemType);
    }

    public ResCenterInboxItem() {
    }

    public ResCenterInboxItem(int itemType) {
        this.itemType = itemType;
    }

    protected ResCenterInboxItem(Parcel in) {
        this.itemType = in.readInt();
    }

    public static String getItemTypeString(int itemType) {
        switch (itemType) {
            case ResCenterInboxItem.TYPE_MAIN:
                return "TYPE_MAIN";
            case ResCenterInboxItem.TYPE_NO_RESULT:
                return "TYPE_NO_RESULT";
            case ResCenterInboxItem.TYPE_HEADER:
                return "TYPE_HEADER";
            case ResCenterInboxItem.TYPE_LOADING:
                return "TYPE_LOADING";
            case ResCenterInboxItem.TYPE_RETRY:
                return "TYPE_RETRY";
            default:
                throw new RuntimeException("UnSupported View Type : " + itemType);
        }
    }
}
