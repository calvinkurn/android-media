package com.tokopedia.digital.tokocash.model;

/**
 * Created by nabillasabbaha on 8/30/17.
 */

public class HeaderItemColor {

    private HeaderHistory headerHistory;

    private HeaderColor headerColor;

    public HeaderItemColor(HeaderHistory headerHistory, HeaderColor headerColor) {
        this.headerHistory = headerHistory;
        this.headerColor = headerColor;
    }

    public HeaderHistory getHeaderHistory() {
        return headerHistory;
    }

    public void setHeaderHistory(HeaderHistory headerHistory) {
        this.headerHistory = headerHistory;
    }

    public HeaderColor getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(HeaderColor headerColor) {
        this.headerColor = headerColor;
    }
}
