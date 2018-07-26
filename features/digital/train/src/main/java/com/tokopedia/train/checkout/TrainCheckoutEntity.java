package com.tokopedia.train.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 26/07/18.
 */
public class TrainCheckoutEntity {

    @SerializedName("redirectURL")
    @Expose
    private String redirectURL;

    @SerializedName("callbackURLSuccess")
    @Expose
    private String callbackURLSuccess;

    @SerializedName("callbackURLFailed")
    @Expose
    private String callbackURLFailed;

    @SerializedName("thanksURL")
    @Expose
    private String thanksURL;

    @SerializedName("queryString")
    @Expose
    private String queryString;

    @SerializedName("parameter")
    @Expose
    private TrainCheckoutParameterEntity parameter;

}