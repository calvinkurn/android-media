package com.tokopedia.train.checkout.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 26/07/18.
 */
public class TrainCheckoutWrapperEntity {

    @SerializedName("kaiCheckout")
    @Expose
    private TrainCheckoutEntity trainCheckoutEntity;

    public TrainCheckoutEntity getTrainCheckoutEntity() {
        return trainCheckoutEntity;
    }
}
