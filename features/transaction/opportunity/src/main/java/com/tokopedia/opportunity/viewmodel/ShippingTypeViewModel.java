package com.tokopedia.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/6/17.
 */

public class ShippingTypeViewModel implements Parcelable{
    int shippingTypeId;
    String shippingTypeName;
    private boolean isSelected;

    public ShippingTypeViewModel() {
    }

    public ShippingTypeViewModel(String shippingTypeName, int shippingTypeId) {
        this.shippingTypeName = shippingTypeName;
        this.shippingTypeId = shippingTypeId;
    }


    protected ShippingTypeViewModel(Parcel in) {
        shippingTypeId = in.readInt();
        shippingTypeName = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<ShippingTypeViewModel> CREATOR = new Creator<ShippingTypeViewModel>() {
        @Override
        public ShippingTypeViewModel createFromParcel(Parcel in) {
            return new ShippingTypeViewModel(in);
        }

        @Override
        public ShippingTypeViewModel[] newArray(int size) {
            return new ShippingTypeViewModel[size];
        }
    };

    public int getShippingTypeId() {
        return shippingTypeId;
    }

    public void setShippingTypeId(int shippingTypeId) {
        this.shippingTypeId = shippingTypeId;
    }

    public String getShippingTypeName() {
        return shippingTypeName;
    }

    public void setShippingTypeName(String shippingTypeName) {
        this.shippingTypeName = shippingTypeName;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shippingTypeId);
        dest.writeString(shippingTypeName);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
