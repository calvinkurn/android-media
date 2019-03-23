package com.tokopedia.gamification.floating.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloatingButtonResponseEntity {

    @SerializedName("gamiFloating")
    @Expose
    private GamiFloatingButtonEntity gamiFloatingButtonEntity;

    public GamiFloatingButtonEntity getGamiFloatingButtonEntity() {
        return gamiFloatingButtonEntity;
    }

    public void setGamiFloatingButtonEntity(GamiFloatingButtonEntity gamiFloatingButtonEntity) {
        this.gamiFloatingButtonEntity = gamiFloatingButtonEntity;
    }

}