package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/7/17.
 */
public class OrderCustomerViewModel implements Parcelable {
    private String customerUrl;
    private String customerId;
    private String customerName;
    private String customerImage;

    public OrderCustomerViewModel() {
    }

    protected OrderCustomerViewModel(Parcel in) {
        customerUrl = in.readString();
        customerId = in.readString();
        customerName = in.readString();
        customerImage = in.readString();
    }

    public static final Creator<OrderCustomerViewModel> CREATOR = new Creator<OrderCustomerViewModel>() {
        @Override
        public OrderCustomerViewModel createFromParcel(Parcel in) {
            return new OrderCustomerViewModel(in);
        }

        @Override
        public OrderCustomerViewModel[] newArray(int size) {
            return new OrderCustomerViewModel[size];
        }
    };

    public String getCustomerUrl() {
        return customerUrl;
    }

    public void setCustomerUrl(String customerUrl) {
        this.customerUrl = customerUrl;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerUrl);
        dest.writeString(customerId);
        dest.writeString(customerName);
        dest.writeString(customerImage);
    }
}
