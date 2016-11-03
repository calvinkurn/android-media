package com.tokopedia.tkpd.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderButton implements Parcelable {
    private static final String TAG = OrderButton.class.getSimpleName();

    @SerializedName("button_ask_seller")
    @Expose
    private String buttonAskSeller;
    @SerializedName("button_open_dispute")
    @Expose
    private String buttonOpenDispute;
    @SerializedName("button_res_center_url")
    @Expose
    private String buttonResCenterUrl;
    @SerializedName("button_open_time_left")
    @Expose
    private String buttonOpenTimeLeft;
    @SerializedName("button_res_center_go_to")
    @Expose
    private String buttonResCenterGoTo;
    @SerializedName("button_upload_proof")
    @Expose
    private String buttonUploadProof;
    @SerializedName("button_open_complaint_received")
    @Expose
    private String buttonComplaintReceived;

    public String getButtonOpenDispute() {
        return buttonOpenDispute;
    }

    public void setButtonOpenDispute(String buttonOpenDispute) {
        this.buttonOpenDispute = buttonOpenDispute;
    }

    public String getButtonResCenterUrl() {
        return buttonResCenterUrl;
    }

    public void setButtonResCenterUrl(String buttonResCenterUrl) {
        this.buttonResCenterUrl = buttonResCenterUrl;
    }

    public String getButtonOpenTimeLeft() {
        return buttonOpenTimeLeft;
    }

    public void setButtonOpenTimeLeft(String buttonOpenTimeLeft) {
        this.buttonOpenTimeLeft = buttonOpenTimeLeft;
    }

    public String getButtonResCenterGoTo() {
        return buttonResCenterGoTo;
    }

    public void setButtonResCenterGoTo(String buttonResCenterGoTo) {
        this.buttonResCenterGoTo = buttonResCenterGoTo;
    }

    public String getButtonUploadProof() {
        return buttonUploadProof;
    }

    public void setButtonUploadProof(String buttonUploadProof) {
        this.buttonUploadProof = buttonUploadProof;
    }

    public String getButtonAskSeller() {
        return buttonAskSeller;
    }

    public void setButtonAskSeller(String buttonAskSeller) {
        this.buttonAskSeller = buttonAskSeller;
    }

    public String getButtonComplaintReceived() {
        return buttonComplaintReceived;
    }

    public void setButtonComplaintReceived(String buttonComplaintReceived) {
        this.buttonComplaintReceived = buttonComplaintReceived;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.buttonAskSeller);
        dest.writeString(this.buttonOpenDispute);
        dest.writeString(this.buttonResCenterUrl);
        dest.writeString(this.buttonOpenTimeLeft);
        dest.writeString(this.buttonResCenterGoTo);
        dest.writeString(this.buttonUploadProof);
        dest.writeString(this.buttonComplaintReceived);
    }

    public OrderButton() {
    }

    protected OrderButton(Parcel in) {
        this.buttonAskSeller = in.readString();
        this.buttonOpenDispute = in.readString();
        this.buttonResCenterUrl = in.readString();
        this.buttonOpenTimeLeft = in.readString();
        this.buttonResCenterGoTo = in.readString();
        this.buttonUploadProof = in.readString();
        this.buttonComplaintReceived = in.readString();
    }

    public static final Creator<OrderButton> CREATOR = new Creator<OrderButton>() {
        @Override
        public OrderButton createFromParcel(Parcel source) {
            return new OrderButton(source);
        }

        @Override
        public OrderButton[] newArray(int size) {
            return new OrderButton[size];
        }
    };
}
