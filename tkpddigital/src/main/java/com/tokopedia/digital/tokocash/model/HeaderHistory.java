package com.tokopedia.digital.tokocash.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class HeaderHistory implements Parcelable {

    private String name;

    private String type;

    private boolean selected;

    public HeaderHistory() {
    }

    protected HeaderHistory(Parcel in) {
        name = in.readString();
        type = in.readString();
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HeaderHistory> CREATOR = new Creator<HeaderHistory>() {
        @Override
        public HeaderHistory createFromParcel(Parcel in) {
            return new HeaderHistory(in);
        }

        @Override
        public HeaderHistory[] newArray(int size) {
            return new HeaderHistory[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
