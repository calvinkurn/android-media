package com.tokopedia.analytics.debugger.domain.model;

/**
 * @author okasurya on 5/16/18.
 */
public class AnalyticsLogData {
    private String data;
    private String name;
    private String category;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
