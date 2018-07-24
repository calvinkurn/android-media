package com.tokopedia.train.reviewdetail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 23/07/18.
 */
public class TrainCheckVoucherEntity {
    @SerializedName("kaiCheckGala")
    @Expose
    private TrainCheckGalaEntity trainCheckGalaEntity;

    public TrainCheckGalaEntity getTrainCheckGalaEntity() {
        return trainCheckGalaEntity;
    }
}