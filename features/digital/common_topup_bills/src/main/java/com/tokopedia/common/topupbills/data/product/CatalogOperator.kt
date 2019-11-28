package com.tokopedia.common.topupbills.data.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 28/11/19.
 */
open class CatalogOperator(

        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("attributes")
        @Expose
        val attributes: CatalogOperatorAttributes = CatalogOperatorAttributes()

)