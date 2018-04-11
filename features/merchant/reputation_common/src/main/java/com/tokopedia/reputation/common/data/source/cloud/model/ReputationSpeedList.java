
package com.tokopedia.reputation.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReputationSpeedList {

    @SerializedName("speed")
    @Expose
    private ReputationSpeed speed;

    public ReputationSpeed getSpeed() {
        return speed;
    }

    public void setSpeed(ReputationSpeed speed) {
        this.speed = speed;
    }

}
