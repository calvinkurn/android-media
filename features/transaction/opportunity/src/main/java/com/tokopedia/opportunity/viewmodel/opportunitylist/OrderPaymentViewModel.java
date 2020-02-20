package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/21/17.
 */

public class OrderPaymentViewModel implements Parcelable{

    private String paymentProcessDueDate;
    private String paymentKomisi;
    private String paymentVerifyDate;
    private String paymentShippingDueDate;
    private String paymentProcessDayLeft;
    private int paymentGatewayId;
    private String paymentGatewayImage;
    private String paymentShippingDayLeft;
    private String paymentGatewayName;

    public OrderPaymentViewModel() {
    }

    protected OrderPaymentViewModel(Parcel in) {
        paymentProcessDueDate = in.readString();
        paymentKomisi = in.readString();
        paymentVerifyDate = in.readString();
        paymentShippingDueDate = in.readString();
        paymentProcessDayLeft = in.readString();
        paymentGatewayId = in.readInt();
        paymentGatewayImage = in.readString();
        paymentShippingDayLeft = in.readString();
        paymentGatewayName = in.readString();
    }

    public static final Creator<OrderPaymentViewModel> CREATOR = new Creator<OrderPaymentViewModel>() {
        @Override
        public OrderPaymentViewModel createFromParcel(Parcel in) {
            return new OrderPaymentViewModel(in);
        }

        @Override
        public OrderPaymentViewModel[] newArray(int size) {
            return new OrderPaymentViewModel[size];
        }
    };

    public String getPaymentProcessDueDate() {
        return paymentProcessDueDate;
    }

    public void setPaymentProcessDueDate(String paymentProcessDueDate) {
        this.paymentProcessDueDate = paymentProcessDueDate;
    }

    public String getPaymentKomisi() {
        return paymentKomisi;
    }

    public void setPaymentKomisi(String paymentKomisi) {
        this.paymentKomisi = paymentKomisi;
    }

    public String getPaymentVerifyDate() {
        return paymentVerifyDate;
    }

    public void setPaymentVerifyDate(String paymentVerifyDate) {
        this.paymentVerifyDate = paymentVerifyDate;
    }

    public String getPaymentShippingDueDate() {
        return paymentShippingDueDate;
    }

    public void setPaymentShippingDueDate(String paymentShippingDueDate) {
        this.paymentShippingDueDate = paymentShippingDueDate;
    }

    public String getPaymentProcessDayLeft() {
        return paymentProcessDayLeft;
    }

    public void setPaymentProcessDayLeft(String paymentProcessDayLeft) {
        this.paymentProcessDayLeft = paymentProcessDayLeft;
    }

    public int getPaymentGatewayId() {
        return paymentGatewayId;
    }

    public void setPaymentGatewayId(int paymentGatewayId) {
        this.paymentGatewayId = paymentGatewayId;
    }

    public String getPaymentGatewayImage() {
        return paymentGatewayImage;
    }

    public void setPaymentGatewayImage(String paymentGatewayImage) {
        this.paymentGatewayImage = paymentGatewayImage;
    }

    public String getPaymentShippingDayLeft() {
        return paymentShippingDayLeft;
    }

    public void setPaymentShippingDayLeft(String paymentShippingDayLeft) {
        this.paymentShippingDayLeft = paymentShippingDayLeft;
    }

    public String getPaymentGatewayName() {
        return paymentGatewayName;
    }

    public void setPaymentGatewayName(String paymentGatewayName) {
        this.paymentGatewayName = paymentGatewayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paymentProcessDueDate);
        dest.writeString(paymentKomisi);
        dest.writeString(paymentVerifyDate);
        dest.writeString(paymentShippingDueDate);
        dest.writeString(paymentProcessDayLeft);
        dest.writeInt(paymentGatewayId);
        dest.writeString(paymentGatewayImage);
        dest.writeString(paymentShippingDayLeft);
        dest.writeString(paymentGatewayName);
    }
}
