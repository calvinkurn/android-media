package com.tokopedia.explore.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetExploreData(@SerializedName("get_discovery_kol_data")
                     @Expose
                     var getDiscoveryKolData: GetDiscoveryKolData? = null
)