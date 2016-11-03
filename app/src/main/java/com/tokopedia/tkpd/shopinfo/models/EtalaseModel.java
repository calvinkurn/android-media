package com.tokopedia.tkpd.shopinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tkpd_Eka on 10/13/2015.
 */
public class EtalaseModel implements Parcelable{
    public String id;
    public String name;
    public String numberProd;
    public String totalProd;

    public EtalaseModel(){

    }

    protected EtalaseModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        numberProd = in.readString();
        totalProd = in.readString();
    }

    public static final Creator<EtalaseModel> CREATOR = new Creator<EtalaseModel>() {
        @Override
        public EtalaseModel createFromParcel(Parcel in) {
            return new EtalaseModel(in);
        }

        @Override
        public EtalaseModel[] newArray(int size) {
            return new EtalaseModel[size];
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
        parcel.writeString(numberProd);
        parcel.writeString(totalProd);
    }
}
