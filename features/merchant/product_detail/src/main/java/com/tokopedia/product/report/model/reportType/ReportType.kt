package com.tokopedia.product.report.model.reportType

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportType {

    /**
     *
     * @return
     * The reportDescription
     */
    /**
     *
     * @param reportDescription
     * The report_description
     */
    @SerializedName("report_description")
    @Expose
    var reportDescription: String? = null
    /**
     *
     * @return
     * The reportWeight
     */
    /**
     *
     * @param reportWeight
     * The report_weight
     */
    @SerializedName("report_weight")
    @Expose
    var reportWeight: Int = 0
    /**
     *
     * @return
     * The reportId
     */
    /**
     *
     * @param reportId
     * The report_id
     */
    @SerializedName("report_id")
    @Expose
    var reportId: Int = 0
    /**
     *
     * @return
     * The reportResponse
     */
    /**
     *
     * @param reportResponse
     * The report_response
     */
    @SerializedName("report_response")
    @Expose
    var reportResponse: Int = 0
    /**
     *
     * @return
     * The reportTitle
     */
    /**
     *
     * @param reportTitle
     * The report_title
     */

    @SerializedName("report_title")
    @Expose
    var reportTitle: String? = null
    @SerializedName("report_url")
    @Expose
    var reportUrl: String? = null

    var useRedirectButton: Boolean = false
        get() = reportResponse == 0 && (reportUrl?.isNotEmpty() ?: false)

}
