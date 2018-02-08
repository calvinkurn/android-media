package com.tokopedia.core.router.digitalmodule.sellermodule;

/**
 * Created by nabillasabbaha on 8/22/2017.
 */

public class PeriodRangeModelCore {

    private long startDate;
    private long endDate;
    private String label;

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PeriodRangeModelCore() {

    }

    public PeriodRangeModelCore(long startDate, long endDate) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PeriodRangeModelCore(long startDate, long endDate, String label) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.label = label;
    }
}
