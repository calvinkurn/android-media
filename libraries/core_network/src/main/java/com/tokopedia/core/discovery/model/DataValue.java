package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 12/22/16.
 */

@Deprecated
public class DataValue implements Parcelable {


    String selected;
    String selectedOb;

    @SerializedName("filter")
    @Expose
    List<Filter> filter = new ArrayList<>();
    @SerializedName("sort")
    @Expose
    List<Sort> sort = new ArrayList<>();

    /**
     * @return The filter
     */
    public List<Filter> getFilter() {
        return filter;
    }

    /**
     * @param filter The filter
     */
    public void setFilter(List<Filter> filter) {
        this.filter = filter;
    }

    /**
     * @return The sort
     */
    public List<Sort> getSort() {
        return sort;
    }

    /**
     * @param sort The sort
     */
    public void setSort(List<Sort> sort) {
        this.sort = sort;
    }

    public String getSelectedOb() {
        return selectedOb;
    }

    public void setSelectedOb(String selectedOb) {
        this.selectedOb = selectedOb;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.selected);
        dest.writeString(this.selectedOb);
        dest.writeTypedList(this.filter);
        dest.writeTypedList(this.sort);
    }

    public DataValue() {
    }

    protected DataValue(Parcel in) {
        this.selected = in.readString();
        this.selectedOb = in.readString();
        this.filter = in.createTypedArrayList(Filter.CREATOR);
        this.sort = in.createTypedArrayList(Sort.CREATOR);
    }

    public static final Creator<DataValue> CREATOR = new Creator<DataValue>() {
        @Override
        public DataValue createFromParcel(Parcel source) {
            return new DataValue(source);
        }

        @Override
        public DataValue[] newArray(int size) {
            return new DataValue[size];
        }
    };
}
