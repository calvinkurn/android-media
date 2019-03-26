
package com.tokopedia.gamification.taptap.data.entiity;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TapTapBaseEntity {

    @SerializedName("gamiTapEggHome")
    private GamiTapEggHome mGamiTapEggHome;

    public GamiTapEggHome getGamiTapEggHome() {
        return mGamiTapEggHome;
    }

    public void setGamiTapEggHome(GamiTapEggHome gamiTapEggHome) {
        mGamiTapEggHome = gamiTapEggHome;
    }

}
