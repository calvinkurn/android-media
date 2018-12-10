package com.tokopedia.core.session.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportType {

    @SerializedName("report_description")
    @Expose
    private String reportDescription;
    @SerializedName("report_weight")
    @Expose
    private int reportWeight;
    @SerializedName("report_id")
    @Expose
    private int reportId;
    @SerializedName("report_response")
    @Expose
    private int reportResponse;
    @SerializedName("report_title")
    @Expose
    private String reportTitle;
    @SerializedName("report_url")
    @Expose
    private String reportUrl;

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    /**
     * 
     * @return
     *     The reportDescription
     */
    public String getReportDescription() {
        return reportDescription;
    }

    /**
     * 
     * @param reportDescription
     *     The report_description
     */
    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    /**
     * 
     * @return
     *     The reportWeight
     */
    public int getReportWeight() {
        return reportWeight;
    }

    /**
     * 
     * @param reportWeight
     *     The report_weight
     */
    public void setReportWeight(int reportWeight) {
        this.reportWeight = reportWeight;
    }

    /**
     * 
     * @return
     *     The reportId
     */
    public int getReportId() {
        return reportId;
    }

    /**
     * 
     * @param reportId
     *     The report_id
     */
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    /**
     * 
     * @return
     *     The reportResponse
     */
    public int getReportResponse() {
        return reportResponse;
    }

    /**
     * 
     * @param reportResponse
     *     The report_response
     */
    public void setReportResponse(int reportResponse) {
        this.reportResponse = reportResponse;
    }

    /**
     * 
     * @return
     *     The reportTitle
     */
    public String getReportTitle() {
        return reportTitle;
    }

    /**
     * 
     * @param reportTitle
     *     The report_title
     */
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

}
