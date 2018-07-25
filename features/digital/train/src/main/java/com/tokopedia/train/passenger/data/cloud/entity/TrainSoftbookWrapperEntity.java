package com.tokopedia.train.passenger.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 24/07/18.
 */
public class TrainSoftbookWrapperEntity {

    @SerializedName("kaiPesanLunak")
    @Expose
    private TrainSoftbookEntity softbookEntity;

    public TrainSoftbookEntity getSoftbookEntity() {
        return softbookEntity;
    }
}
