package com.tokopedia.digital.newcart.data.entity.response.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 2/27/17.
 */

class RelationshipsCart {

    @SerializedName("category")
    @Expose
    var category: Category? = null
    @SerializedName("operator")
    @Expose
    var operator: Operator? = null
    @SerializedName("product")
    @Expose
    var product: Product? = null
}
