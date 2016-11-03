package com.tokopedia.tkpd.shopinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tkpd_Eka on 11/9/2015.
 */
public class GetShopProductParam implements Parcelable{
    public int page = 1;
    public String keyword = "";
    public String etalaseId = "etalase";
    public String orderBy = "";
    public int selectedEtalase = 0;
    public int listState = 3;
    public int per_page = 8;

    public GetShopProductParam(){

    }

    protected GetShopProductParam(Parcel in) {
        page = in.readInt();
        keyword = in.readString();
        etalaseId = in.readString();
        orderBy = in.readString();
        selectedEtalase = in.readInt();
        listState = in.readInt();
        per_page = in.readInt();
    }

    public static final Creator<GetShopProductParam> CREATOR = new Creator<GetShopProductParam>() {
        @Override
        public GetShopProductParam createFromParcel(Parcel in) {
            return new GetShopProductParam(in);
        }

        @Override
        public GetShopProductParam[] newArray(int size) {
            return new GetShopProductParam[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(page);
        parcel.writeString(keyword);
        parcel.writeString(etalaseId);
        parcel.writeString(orderBy);
        parcel.writeInt(selectedEtalase);
        parcel.writeInt(listState);
        parcel.writeInt(per_page);
    }
}
