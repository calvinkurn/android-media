package com.tokopedia.travelcalendar.view.model;

import java.util.Date;

/**
 * Created by nabillasabbaha on 11/05/18.
 */
public class CellDate {

    private Date date;
    private boolean isSelected;

    public CellDate() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
