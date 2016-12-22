package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 12/22/16.
 */
public class Filter implements Serializable, Parcelable {
    public static final String TITLE_CATEGORY = "Kategori";


    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("search")
    @Expose
    Search search;
    @SerializedName("options")
    @Expose
    List<Option> options = new ArrayList<>();

    public Filter() {
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The search
     */
    public Search getSearch() {
        return search;
    }

    /**
     * @param search The search
     */
    public void setSearch(Search search) {
        this.search = search;
    }

    /**
     * @return The options
     */
    public List<Option> getOptions() {
        return options;
    }

    /**
     * @param options The options
     */
    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Filter createCategory() {
        Filter filter = new Filter();
        filter.setTitle(TITLE_CATEGORY);
        filter.setOptions(new ArrayList<Option>());
        Search search = new Search();
        search.setPlaceholder("");
        search.setSearchable(0);
        filter.setSearch(search);
        return filter;
    }

    protected Filter(Parcel in) {
        title = in.readString();
        search = (Search) in.readValue(Search.class.getClassLoader());
        if (in.readByte() == 0x01) {
            options = new ArrayList<Option>();
            in.readList(options, Option.class.getClassLoader());
        } else {
            options = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeValue(search);
        if (options == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(options);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}
