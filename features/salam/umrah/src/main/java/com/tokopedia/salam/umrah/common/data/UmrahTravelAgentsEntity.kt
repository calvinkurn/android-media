package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageModel
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactory

/**
 * @author by firman on 20/01/19
 */

data class UmrahTravelAgentsEntity (
        @SerializedName("umrahTravelAgents")
        @Expose
        val umrahTravelAgents : List<TravelAgent> = emptyList()
): UmrahHomepageModel() {

    override fun type(typeFactory: UmrahHomepageFactory): Int {
        return typeFactory.type(this)
    }
}