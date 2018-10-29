package com.tokopedia.base.list.seller.view.old;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Nisie on 19/06/15.
 */

public class RecyclerViewItem implements Serializable, Parcelable {
    int type = 0;

    public static final Creator<RecyclerViewItem> CREATOR = new Creator<RecyclerViewItem>() {
        @Override
        public RecyclerViewItem createFromParcel(Parcel in) {
            return new RecyclerViewItem(in);
        }

        @Override
        public RecyclerViewItem[] newArray(int size) {
            return new RecyclerViewItem[size];
        }
    };

    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
    }

    public RecyclerViewItem() {
    }

    protected RecyclerViewItem(Parcel in) {
        this.type = in.readInt();
    }

}