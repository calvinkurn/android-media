package com.tokopedia.core.product.model.report;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stevenfredian on 7/5/16.
 */
public class ReportProductPass implements Parcelable{

    public static String TAG = "ReportProductPass";

    private static final String PARAM_REPORT_TYPE = "report_type";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_TEXT_MESSAGE = "text_message";

    String productID;
    String reportType;
    String desc;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productID);
        dest.writeString(this.reportType);
        dest.writeString(this.desc);
    }

    public ReportProductPass() {
    }

    protected ReportProductPass(Parcel in) {
        this.productID = in.readString();
        this.reportType = in.readString();
        this.desc = in.readString();
    }

    public static final Creator<ReportProductPass> CREATOR = new Creator<ReportProductPass>() {
        @Override
        public ReportProductPass createFromParcel(Parcel source) {
            return new ReportProductPass(source);
        }

        @Override
        public ReportProductPass[] newArray(int size) {
            return new ReportProductPass[size];
        }
    };

    public Map<String, String> getParamReport() {
        Map<String, String> paramReport = new HashMap<>();
        paramReport.put(PARAM_REPORT_TYPE, reportType);
        paramReport.put(PARAM_PRODUCT_ID, productID);
        paramReport.put(PARAM_TEXT_MESSAGE, desc);
        return paramReport;
    }
}
