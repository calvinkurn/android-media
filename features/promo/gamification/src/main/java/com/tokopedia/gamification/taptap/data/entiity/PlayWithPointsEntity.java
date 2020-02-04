
package com.tokopedia.gamification.taptap.data.entiity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PlayWithPointsEntity {

    @Expose
    @SerializedName("gamiPlayWithPoints")
    private GamiPlayWithPoints gamiPlayWithPoints;

    public GamiPlayWithPoints getGamiPlayWithPoints() {
        return gamiPlayWithPoints;
    }

    public void setGamiPlayWithPoints(GamiPlayWithPoints gamiPlayWithPoints) {
        this.gamiPlayWithPoints = gamiPlayWithPoints;
    }

}
