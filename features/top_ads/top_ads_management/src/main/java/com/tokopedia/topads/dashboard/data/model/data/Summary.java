package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class Summary {

    @SerializedName("click_sum")
    private int clickSum;

    @SerializedName("click_sum_fmt")
    public String clickSumFmt;

    @SerializedName("cost_sum")
    public double costSum;

    @SerializedName("cost_sum_fmt")
    public String costSumFmt;

    @SerializedName("impression_sum")
    public int impressionSum;

    @SerializedName("impression_sum_fmt")
    public String impressionSumFmt;

    @SerializedName("ctr_percentage")
    public double ctrPercentage;

    @SerializedName("ctr_percentage_fmt")
    public String ctrPercentageFmt;

    @SerializedName("conversion_sum")
    public int conversionSum;

    @SerializedName("conversion_sum_fmt")
    public String conversionSumFmt;

    @SerializedName("cost_avg")
    public double costAvg;

    @SerializedName("cost_avg_fmt")
    public String costAvgFmt;

    public int getClickSum() {
        return clickSum;
    }

    public void setClickSum(int clickSum) {
        this.clickSum = clickSum;
    }

    public String getClickSumFmt() {
        return clickSumFmt;
    }

    public void setClickSumFmt(String clickSumFmt) {
        this.clickSumFmt = clickSumFmt;
    }

    public double getCostSum() {
        return costSum;
    }

    public void setCostSum(double costSum) {
        this.costSum = costSum;
    }

    public String getCostSumFmt() {
        return costSumFmt;
    }

    public void setCostSumFmt(String costSumFmt) {
        this.costSumFmt = costSumFmt;
    }

    public int getImpressionSum() {
        return impressionSum;
    }

    public void setImpressionSum(int impressionSum) {
        this.impressionSum = impressionSum;
    }

    public String getImpressionSumFmt() {
        return impressionSumFmt;
    }

    public void setImpressionSumFmt(String impressionSumFmt) {
        this.impressionSumFmt = impressionSumFmt;
    }

    public double getCtrPercentage() {
        return ctrPercentage;
    }

    public void setCtrPercentage(double ctrPercentage) {
        this.ctrPercentage = ctrPercentage;
    }

    public String getCtrPercentageFmt() {
        return ctrPercentageFmt;
    }

    public void setCtrPercentageFmt(String ctrPercentageFmt) {
        this.ctrPercentageFmt = ctrPercentageFmt;
    }

    public int getConversionSum() {
        return conversionSum;
    }

    public void setConversionSum(int conversionSum) {
        this.conversionSum = conversionSum;
    }

    public String getConversionSumFmt() {
        return conversionSumFmt;
    }

    public void setConversionSumFmt(String conversionSumFmt) {
        this.conversionSumFmt = conversionSumFmt;
    }

    public double getCostAvg() {
        return costAvg;
    }

    public void setCostAvg(double costAvg) {
        this.costAvg = costAvg;
    }

    public String getCostAvgFmt() {
        return costAvgFmt;
    }

    public void setCostAvgFmt(String costAvgFmt) {
        this.costAvgFmt = costAvgFmt;
    }
}