package com.tokopedia.core.drawer2.viewmodel;

/**
 * Created by nisie on 1/24/17.
 */

public class DrawerTopPoints {
    private String topPoints;
    private String topPointsUrl;

    public DrawerTopPoints() {
        this.topPoints = "";
        this.topPointsUrl = "";
    }

    public String getTopPointsUrl() {
        return topPointsUrl;
    }

    public String getTopPoints() {
        return topPoints;
    }

    public void setTopPoints(String topPoints) {
        this.topPoints = topPoints;
    }

    public void setTopPointsUrl(String topPointsUrl) {
        this.topPointsUrl = topPointsUrl;
    }


}
