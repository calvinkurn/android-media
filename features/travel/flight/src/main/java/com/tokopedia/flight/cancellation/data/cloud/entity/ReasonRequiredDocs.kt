package com.tokopedia.flight.cancellation.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 15/06/2020
 */
class ReasonRequiredDocs(@SerializedName("id")
                         @Expose
                         val docId: Int = 0,
                         @SerializedName("title")
                         @Expose
                         val title: String = "")