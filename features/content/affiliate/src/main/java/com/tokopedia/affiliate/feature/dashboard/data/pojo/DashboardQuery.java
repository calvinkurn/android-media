package com.tokopedia.affiliate.feature.dashboard.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardQuery {

    @SerializedName("dashboard_query")
    @Expose
    private DashboardPojo dashboard;

    public DashboardPojo getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashboardPojo dashboard) {
        this.dashboard = dashboard;
    }

}
