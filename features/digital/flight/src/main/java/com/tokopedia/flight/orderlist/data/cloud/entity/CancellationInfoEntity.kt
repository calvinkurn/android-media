package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/08/2019
 */
class CancellationInfoEntity(@SerializedName("text")
                             @Expose
                             val text: String = "")