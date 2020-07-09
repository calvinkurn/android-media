package com.tokopedia.flight.detail.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvarisi on 11/21/17.
 */

public class SimpleModel implements Parcelable {

    private String label;
    private String description;

    public SimpleModel() {
    }

    public SimpleModel(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeString(this.description);
    }

    protected SimpleModel(Parcel in) {
        this.label = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<SimpleModel> CREATOR = new Parcelable.Creator<SimpleModel>() {
        @Override
        public SimpleModel createFromParcel(Parcel source) {
            return new SimpleModel(source);
        }

        @Override
        public SimpleModel[] newArray(int size) {
            return new SimpleModel[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SimpleModel && ((SimpleModel) obj).getLabel().equalsIgnoreCase(label);
    }
}
