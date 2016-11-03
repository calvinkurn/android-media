package com.tokopedia.tkpd.shopinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tkpd_Eka on 10/13/2015.
 */
public class SimpleProductModel implements Parcelable{
    public String id;
    public String name;
    public String price;
    public String imageThumbs;
    public String imageLarge;
    public String isGold;
    public int talk;
    public int preview;

    public SimpleProductModel(){

    }

    protected SimpleProductModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        imageThumbs = in.readString();
        imageLarge = in.readString();
        isGold = in.readString();
        talk = in.readInt();
        preview = in.readInt();
    }

    public static final Creator<SimpleProductModel> CREATOR = new Creator<SimpleProductModel>() {
        @Override
        public SimpleProductModel createFromParcel(Parcel in) {
            return new SimpleProductModel(in);
        }

        @Override
        public SimpleProductModel[] newArray(int size) {
            return new SimpleProductModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(imageThumbs);
        parcel.writeString(imageLarge);
        parcel.writeString(isGold);
        parcel.writeInt(talk);
        parcel.writeInt(preview);
    }
}
