package com.tokopedia.core.shopinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tkpd_Eka on 10/15/2015.
 */

@Deprecated
public class NoteModel implements Parcelable{
    public String title;
    public String content;
    public String update;
    public String id;
    public String status;

    public NoteModel(){}

    protected NoteModel(Parcel in) {
        title = in.readString();
        content = in.readString();
        update = in.readString();
        id = in.readString();
        status = in.readString();
    }

    public static final Creator<NoteModel> CREATOR = new Creator<NoteModel>() {
        @Override
        public NoteModel createFromParcel(Parcel in) {
            return new NoteModel(in);
        }

        @Override
        public NoteModel[] newArray(int size) {
            return new NoteModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(update);
        parcel.writeString(id);
        parcel.writeString(status);
    }
}
