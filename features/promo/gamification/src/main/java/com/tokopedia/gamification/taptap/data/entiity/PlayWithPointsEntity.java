
package com.tokopedia.gamification.taptap.data.entiity;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class PlayWithPointsEntity {

    @Expose
    private GamiPlayWithPoints gamiPlayWithPoints;

    public GamiPlayWithPoints getGamiPlayWithPoints() {
        return gamiPlayWithPoints;
    }

    public void setGamiPlayWithPoints(GamiPlayWithPoints gamiPlayWithPoints) {
        this.gamiPlayWithPoints = gamiPlayWithPoints;
    }

}
