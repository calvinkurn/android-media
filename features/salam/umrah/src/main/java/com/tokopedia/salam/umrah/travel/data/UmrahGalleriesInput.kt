package com.tokopedia.salam.umrah.travel.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * @author by Firman on 3/2/20
 */

data class UmrahGalleriesInput(
        @SerializedName("page")
        @Expose
        var page : Int = 0,
        @SerializedName("limit")
        @Expose
        var limit : Int = 5,
        @SerializedName("entityName")
        @Expose
        var entityName : List<String> = listOf(),
        @SerializedName("entitySlugName")
        @Expose
        var entitySlugName : String = ""
)