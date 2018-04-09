package com.tokopedia.topads.dashboard.view.model;

/**
 * Created by Nathaniel on 1/27/2017.
 */

public class RadioButtonItem {

    private String value;
    private String name;
    private int position;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}