package com.tokopedia.tokocash.historytokocash.presentation.model;

/**
 * Created by nabillasabbaha on 8/22/2017.
 */

public class PeriodRangeModelData {

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

    public PeriodRangeModelData() {

    }

    public PeriodRangeModelData(long startDate, long endDate) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PeriodRangeModelData(long startDate, long endDate, String label) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.label = label;
    }
}
