package com.tokopedia.topads.dashboard.data.model.data;

import com.tokopedia.topads.common.data.model.DataDeposit;

/**
 * Created by hadi.putra on 18/05/18.
 */

public class DashboardPopulateResponse {
    private TotalAd totalAd;
    private DataDeposit dataDeposit;

    public DashboardPopulateResponse(TotalAd totalAd, DataDeposit dataDeposit) {
        this.totalAd = totalAd;
        this.dataDeposit = dataDeposit;
    }

    public TotalAd getTotalAd() {
        return totalAd;
    }

    public void setTotalAd(TotalAd totalAd) {
        this.totalAd = totalAd;
    }

    public DataDeposit getDataDeposit() {
        return dataDeposit;
    }

    public void setDataDeposit(DataDeposit dataDeposit) {
        this.dataDeposit = dataDeposit;
    }
}
