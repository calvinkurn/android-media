package com.db.williamchart.model;

/**
 * Created By @ilhamsuaib on 2020-02-14
 */
public class CustomValue {

    private String label;
    private Integer value;
    private String customValue;

    public CustomValue(String label, Integer value, String customValue) {
        this.label = label;
        this.value = value;
        this.customValue = customValue;
    }

    public String getLabel() {
        return label;
    }

    public String getCustomValue() {
        return customValue;
    }

    public Integer getValue() {
        return value;
    }
}
