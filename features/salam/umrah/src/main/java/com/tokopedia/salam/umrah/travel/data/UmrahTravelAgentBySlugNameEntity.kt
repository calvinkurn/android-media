package com.tokopedia.salam.umrah.travel.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.common.data.TravelAgent

/**
 * @author by Firman on 22/1/20
 */


data class UmrahTravelAgentBySlugNameEntity (
        @SerializedName("umrahTravelAgentBySlug")
        @Expose
        val umrahTravelAgentBySlug : TravelAgent = TravelAgent()
)