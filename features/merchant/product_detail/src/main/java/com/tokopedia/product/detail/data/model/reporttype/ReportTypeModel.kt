package com.tokopedia.product.detail.data.model.reporttype

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class ReportTypeModel {
    /**
     *
     * @return
     * The productId
     */
    /**
     *
     * @param productId
     * The product_id
     */
    @SerializedName("product_id")
    @Expose
    var productId: String? = null
    /**
     *
     * @return
     * The reportType
     */
    /**
     *
     * @param reportType
     * The report_type
     */
    @SerializedName("list")
    @Expose
    var reportType: List<ReportType> = listOf()
}
