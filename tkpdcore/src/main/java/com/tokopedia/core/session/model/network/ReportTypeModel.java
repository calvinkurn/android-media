
package com.tokopedia.core.session.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReportTypeModel {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("list")
    @Expose
    private List<ReportType> reportType = new ArrayList<>();

    /**
     *
     * @return
     *     The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     *     The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     *     The reportType
     */
    public List<ReportType> getReportType() {
        return reportType;
    }

    /**
     *
     * @param reportType
     *     The report_type
     */
    public void setReportType(List<ReportType> reportType) {
        this.reportType = reportType;
    }
}
