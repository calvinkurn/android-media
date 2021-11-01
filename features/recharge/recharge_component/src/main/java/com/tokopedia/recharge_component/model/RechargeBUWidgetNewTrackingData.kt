package com.tokopedia.recharge_component.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author: astidhiyaa on 18/10/21.
 */
data class RechargeBUWidgetNewTrackingData(
    @SerializedName("product_id")
    @Expose
    val productId: String = "",
    @SerializedName("operator_id")
    @Expose
    val operatorId: String = "",
    @SerializedName("business_unit")
    @Expose
    val bussinessUnit: String = "",
    @SerializedName("item_type")
    @Expose
    val itemType: String = "",
    @SerializedName("item_label")
    @Expose
    val itemLabel: String = "",
    @SerializedName("client_number")
    @Expose
    val clientNumber: String = "",
    @SerializedName("category_id")
    @Expose
    val categoryId: String = "",
    @SerializedName("category_name")
    @Expose
    val categoryName: String = "",
)
