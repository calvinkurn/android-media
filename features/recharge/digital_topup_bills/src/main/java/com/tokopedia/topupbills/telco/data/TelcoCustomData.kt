package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoCustomData(
        @SerializedName("data_collections")
        @Expose
        val customDataCollections: List<TelcoCustomDataCollection>
)