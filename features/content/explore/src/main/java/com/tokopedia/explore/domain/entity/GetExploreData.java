
package com.tokopedia.explore.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetExploreData {

    @SerializedName("get_discovery_kol_data")
    @Expose
    private GetDiscoveryKolData getDiscoveryKolData;

    public GetDiscoveryKolData getGetDiscoveryKolData() {
        return getDiscoveryKolData;
    }

    public void setGetDiscoveryKolData(GetDiscoveryKolData getDiscoveryKolData) {
        this.getDiscoveryKolData = getDiscoveryKolData;
    }

}
