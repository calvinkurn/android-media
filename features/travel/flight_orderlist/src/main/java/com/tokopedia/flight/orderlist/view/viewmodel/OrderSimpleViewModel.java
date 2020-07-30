package com.tokopedia.flight.orderlist.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvarisi on 11/21/17.
 */

public class OrderSimpleViewModel implements Parcelable {

    private String label;
    private String description;

    public OrderSimpleViewModel() {
    }

    public OrderSimpleViewModel(String label, String description) {
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

    protected OrderSimpleViewModel(Parcel in) {
        this.label = in.readString();
        this.description = in.readString();
    }

    public static final Creator<OrderSimpleViewModel> CREATOR = new Creator<OrderSimpleViewModel>() {
        @Override
        public OrderSimpleViewModel createFromParcel(Parcel source) {
            return new OrderSimpleViewModel(source);
        }

        @Override
        public OrderSimpleViewModel[] newArray(int size) {
            return new OrderSimpleViewModel[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OrderSimpleViewModel && ((OrderSimpleViewModel) obj).getLabel().equalsIgnoreCase(label);
    }
}
