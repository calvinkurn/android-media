package com.tokopedia.core.talkview.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stevenfredian on 5/10/16.
 */
public class TalkBaseModel implements Parcelable {
    int type;
    public static final int LOADING = 1;
    public static final int RETRY = 2;
    public static final int EMPTY = 3;
    public static final int MAIN = 0;

    public TalkBaseModel() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    protected TalkBaseModel(Parcel in) {
        type = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TalkBaseModel> CREATOR = new Parcelable.Creator<TalkBaseModel>() {
        @Override
        public TalkBaseModel createFromParcel(Parcel in) {
            return new TalkBaseModel(in);
        }

        @Override
        public TalkBaseModel[] newArray(int size) {
            return new TalkBaseModel[size];
        }
    };

    public static TalkBaseModel create(int type) {
        TalkBaseModel item= new TalkBaseModel();
        item.setType(type);
        return item;
    }
}
