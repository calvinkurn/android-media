package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 28/12/18.
 */
public class FilterViewModel implements Parcelable {

    private String name;
    private String image;
    private List<Integer> ids;
    private boolean isSelected;

    public FilterViewModel(String name, String image, List<Integer> ids, boolean isSelected) {
        this.name = name;
        this.image = image;
        this.ids = ids;
        this.isSelected = isSelected;
    }

    public FilterViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeList(this.ids);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected FilterViewModel(Parcel in) {
        this.name = in.readString();
        this.image = in.readString();
        this.ids = new ArrayList<Integer>();
        in.readList(this.ids, Integer.class.getClassLoader());
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<FilterViewModel> CREATOR = new Parcelable.Creator<FilterViewModel>() {
        @Override
        public FilterViewModel createFromParcel(Parcel source) {
            return new FilterViewModel(source);
        }

        @Override
        public FilterViewModel[] newArray(int size) {
            return new FilterViewModel[size];
        }
    };
}
