package com.tokopedia.train.reviewdetail.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 23/07/18.
 */
public class TrainCheckVoucherWrapperEntity {
    @SerializedName("kaiCheckGala")
    @Expose
    private TrainCheckVoucherEntity trainCheckVoucherEntity;

    public TrainCheckVoucherEntity getTrainCheckVoucherEntity() {
        return trainCheckVoucherEntity;
    }
}