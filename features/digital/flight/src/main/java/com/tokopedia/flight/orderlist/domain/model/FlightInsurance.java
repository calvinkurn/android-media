package com.tokopedia.flight.orderlist.domain.model;

public class FlightInsurance {
    private String id;
    private String paidAmount;
    private long paidAmountNumeric;
    private String title;
    private String tagline;

    public FlightInsurance() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public long getPaidAmountNumeric() {
        return paidAmountNumeric;
    }

    public void setPaidAmountNumeric(long paidAmountNumeric) {
        this.paidAmountNumeric = paidAmountNumeric;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
}
