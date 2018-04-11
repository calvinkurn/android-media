
package com.tokopedia.reputation.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReputationRecentMonth {

    @SerializedName("speed_level")
    @Expose
    private int speedLevel;
    @SerializedName("speed_level_description")
    @Expose
    private String speedLevelDescription;

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int speedLevel) {
        this.speedLevel = speedLevel;
    }

    public String getSpeedLevelDescription() {
        return speedLevelDescription;
    }

    public void setSpeedLevelDescription(String speedLevelDescription) {
        this.speedLevelDescription = speedLevelDescription;
    }

}
