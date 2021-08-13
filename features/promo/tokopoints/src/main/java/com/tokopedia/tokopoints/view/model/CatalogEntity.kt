package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.tokopoints.view.model.section.CountDownInfo
import java.io.Serializable
import java.util.ArrayList

data class CatalogEntity(

        @Expose
        @SerializedName("catalogs")
        var catalogs: ArrayList<CatalogsValueEntity>? = null,

        @SerializedName("pageInfo")
        var paging: TokopointPaging? = null,

        @SerializedName("countdownInfo")
        val countDownInfo: CountDownInfo? = null
)