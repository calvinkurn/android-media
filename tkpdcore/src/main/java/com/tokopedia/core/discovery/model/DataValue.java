package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 12/22/16.
 */
public class DataValue implements Serializable, Parcelable {


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


    protected DataValue(Parcel in) {
        selected = in.readString();
        selectedOb = in.readString();
        if (in.readByte() == 0x01) {
            filter = new ArrayList<Filter>();
            in.readList(filter, Filter.class.getClassLoader());
        } else {
            filter = null;
        }
        if (in.readByte() == 0x01) {
            sort = new ArrayList<Sort>();
            in.readList(sort, Sort.class.getClassLoader());
        } else {
            sort = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(selected);
        dest.writeString(selectedOb);
        if (filter == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(filter);
        }
        if (sort == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(sort);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DataValue> CREATOR = new Parcelable.Creator<DataValue>() {
        @Override
        public DataValue createFromParcel(Parcel in) {
            return new DataValue(in);
        }

        @Override
        public DataValue[] newArray(int size) {
            return new DataValue[size];
        }
    };
}
