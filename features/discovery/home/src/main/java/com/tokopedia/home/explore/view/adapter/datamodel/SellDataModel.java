package com.tokopedia.home.explore.view.adapter.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.explore.view.adapter.TypeFactory;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class SellDataModel implements Visitable<TypeFactory>, Parcelable {

    private String title;
    private String subtitle;
    private String btn_title;

    public SellDataModel() {
    }

    public SellDataModel(String title, String subtitle, String btn_title) {
        this.title = title;
        this.subtitle = subtitle;
        this.btn_title = btn_title;
    }

    protected SellDataModel(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        btn_title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(btn_title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SellDataModel> CREATOR = new Creator<SellDataModel>() {
        @Override
        public SellDataModel createFromParcel(Parcel in) {
            return new SellDataModel(in);
        }

        @Override
        public SellDataModel[] newArray(int size) {
            return new SellDataModel[size];
        }
    };

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBtn_title() {
        return btn_title;
    }

    public void setBtn_title(String btn_title) {
        this.btn_title = btn_title;
    }
}
