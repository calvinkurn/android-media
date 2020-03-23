package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelListAdapterTypeFactory

/**
 * @author by firman on 20/01/19
 */

data class TravelAgent(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("logoUrl")
        @Expose
        val imageUrl: String = "",
        @SerializedName("permissionOfUmrah")
        @Expose
        val permissionOfUmrah: String = "",
        @SerializedName("pilgrimsPerYear")
        @Expose
        val pilgrimsPerYear: Int = 0,
        @SerializedName("establishedSince")
        @Expose
        val establishedSince: Int = 0,
        @SerializedName("slugName")
        @Expose
        val slugName: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("address")
        @Expose
        val address: String = "",
        @SerializedName("products")
        @Expose
        val products : List<UmrahProductModel.UmrahProduct> = emptyList(),
        @SerializedName("ui")
        @Expose
        val ui: UmrahTravelAgentUI = UmrahTravelAgentUI(),
        var isViewed : Boolean = false

): Visitable<UmrahTravelListAdapterTypeFactory> {
        override fun type(typeFactory: UmrahTravelListAdapterTypeFactory?): Int =
                typeFactory?.type(this) ?: 0
}

data class  UmrahTravelAgentUI(
        @SerializedName("establishedSince")
        @Expose
        val establishedSince: String = "",
        @SerializedName("pilgrimsPerYear")
        @Expose
        val pilgrimsPerYear: String = ""
)