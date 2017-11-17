package com.tokopedia.digital.tokocash.entity;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class HeaderHistoryEntity {

    private String name;

    private String type;

    private boolean selected;

    public HeaderHistoryEntity() {
    }

    public HeaderHistoryEntity(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
