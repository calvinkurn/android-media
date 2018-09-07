package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.filter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/21/17.
 */

public class OptionViewModel implements Parcelable {
    private String name;
    private String key;
    private String value;
    private boolean isSelected;
    private boolean isActive;
    private int position;

    public OptionViewModel(String name, String key, String value, int position) {
        this.name = name;
        this.key = key;
        this.value = value;
        this.isSelected = false;
        this.isActive = false;
        this.position = position;
    }

    public OptionViewModel(String name) {
        this.name = name;
        this.key = "";
        this.value = "";
        this.isSelected = false;
        this.isActive = false;
        this.position = 0;
    }

    protected OptionViewModel(Parcel in) {
        name = in.readString();
        key = in.readString();
        value = in.readString();
        isSelected = in.readByte() != 0;
        isActive = in.readByte() != 0;
        position = in.readInt();
    }

    public static final Creator<OptionViewModel> CREATOR = new Creator<OptionViewModel>() {
        @Override
        public OptionViewModel createFromParcel(Parcel in) {
            return new OptionViewModel(in);
        }

        @Override
        public OptionViewModel[] newArray(int size) {
            return new OptionViewModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(value);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeInt(position);
    }
}
