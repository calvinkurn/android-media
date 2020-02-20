package com.db.williamchart.model;

import androidx.annotation.NonNull;

/**
 * Created by zulfikarrahman on 5/22/17.
 */

public class TooltipModel {
    private int position;
    private String title;
    private String value;
    private String customValue = "";

    public TooltipModel(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public TooltipModel(String title, String value, @NonNull String customValue) {
        this.title = title;
        this.value = value;
        this.customValue = customValue;
    }

    public TooltipModel() {

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCustomValue() {
        return customValue;
    }

    public Boolean hasCustomValue() {
        return customValue.length() > 0;
    }
}
