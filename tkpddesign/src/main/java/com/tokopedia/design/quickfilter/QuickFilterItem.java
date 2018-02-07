package com.tokopedia.design.quickfilter;

/**
 * Created by nabillasabbaha on 11/22/17.
 */

public class QuickFilterItem {

    private String name;

    private String type;

    private boolean selected;

    private int colorFilter;

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

    public int getColorFilter() {
        return colorFilter;
    }

    public void setColorBorder(int colorFilter) {
        this.colorFilter = colorFilter;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
