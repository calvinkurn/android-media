
package com.tokopedia.sellerapp.home.model.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Payment {

    @SerializedName("payment_image")
    @Expose
    public String paymentImage;
    @SerializedName("payment_id")
    @Expose
    public String paymentId;
    @SerializedName("payment_name")
    @Expose
    public String paymentName;
    @SerializedName("payment_info")
    @Expose
    public int paymentInfo;
    @SerializedName("payment_default_status")
    @Expose
    public String paymentDefaultStatus;

}
