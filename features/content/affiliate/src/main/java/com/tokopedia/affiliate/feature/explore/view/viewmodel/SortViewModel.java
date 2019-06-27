package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/01/19.
 */
public class SortViewModel implements Parcelable {

    private String key;
    private boolean asc;
    private String text;
    private boolean isSelected;

    public SortViewModel(String key, boolean asc, String text, boolean isSelected) {
        this.key = key;
        this.asc = asc;
        this.text = text;
        this.isSelected = isSelected;
    }

    public SortViewModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        dest.writeString(this.key);
        dest.writeByte(this.asc ? (byte) 1 : (byte) 0);
        dest.writeString(this.text);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected SortViewModel(Parcel in) {
        this.key = in.readString();
        this.asc = in.readByte() != 0;
        this.text = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SortViewModel> CREATOR = new Parcelable.Creator<SortViewModel>() {
        @Override
        public SortViewModel createFromParcel(Parcel source) {
            return new SortViewModel(source);
        }

        @Override
        public SortViewModel[] newArray(int size) {
            return new SortViewModel[size];
        }
    };
}
