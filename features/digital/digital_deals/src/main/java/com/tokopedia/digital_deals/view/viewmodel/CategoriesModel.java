package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoriesModel implements Parcelable {


    private String title;
    private String name;
    private String url;
    public final static Parcelable.Creator<CategoriesModel> CREATOR = new Creator<CategoriesModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoriesModel createFromParcel(Parcel in) {
            return new CategoriesModel(in);
        }

        public CategoriesModel[] newArray(int size) {
            return (new CategoriesModel[size]);
        }

    };

    protected CategoriesModel(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CategoriesModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(name);
        dest.writeValue(url);
    }

    public int describeContents() {
        return 0;
    }

}