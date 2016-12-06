
package com.tokopedia.core.shipping.model.openshopshipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentOption implements Parcelable {

    @SerializedName("payment_info")
    @Expose
    private String paymentInfo;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("payment_default_status")
    @Expose
    private String paymentDefaultStatus;
    @SerializedName("payment_image")
    @Expose
    private String paymentImage;
    @SerializedName("payment_name")
    @Expose
    private String paymentName;

    protected PaymentOption(Parcel in) {
        paymentInfo = in.readString();
        paymentId = in.readString();
        paymentDefaultStatus = in.readString();
        paymentImage = in.readString();
        paymentName = in.readString();
    }

    public static final Creator<PaymentOption> CREATOR = new Creator<PaymentOption>() {
        @Override
        public PaymentOption createFromParcel(Parcel in) {
            return new PaymentOption(in);
        }

        @Override
        public PaymentOption[] newArray(int size) {
            return new PaymentOption[size];
        }
    };

    /**
     * 
     * @return
     *     The paymentInfo
     */
    public String getPaymentInfo() {
        return paymentInfo;
    }

    /**
     * 
     * @param paymentInfo
     *     The payment_info
     */
    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    /**
     * 
     * @return
     *     The paymentId
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * 
     * @param paymentId
     *     The payment_id
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * 
     * @return
     *     The paymentDefaultStatus
     */
    public String getPaymentDefaultStatus() {
        return paymentDefaultStatus;
    }

    /**
     * 
     * @param paymentDefaultStatus
     *     The payment_default_status
     */
    public void setPaymentDefaultStatus(String paymentDefaultStatus) {
        this.paymentDefaultStatus = paymentDefaultStatus;
    }

    /**
     * 
     * @return
     *     The paymentImage
     */
    public String getPaymentImage() {
        return paymentImage;
    }

    /**
     * 
     * @param paymentImage
     *     The payment_image
     */
    public void setPaymentImage(String paymentImage) {
        this.paymentImage = paymentImage;
    }

    /**
     * 
     * @return
     *     The paymentName
     */
    public String getPaymentName() {
        return paymentName;
    }

    /**
     * 
     * @param paymentName
     *     The payment_name
     */
    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paymentInfo);
        dest.writeString(paymentId);
        dest.writeString(paymentDefaultStatus);
        dest.writeString(paymentImage);
        dest.writeString(paymentName);
    }
}
