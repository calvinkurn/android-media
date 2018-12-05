package com.tokopedia.checkout.view.feature.shippingoptions.viewmodel;

import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShipmentOptionData;

/**
 * @author Irfan Khoirul on 25/05/18.
 */

public class ShipmentTickerInfoData implements ShipmentOptionData {

    private String tickerInfo;

    public String getTickerInfo() {
        return tickerInfo;
    }

    public void setTickerInfo(String tickerInfo) {
        this.tickerInfo = tickerInfo;
    }
}
