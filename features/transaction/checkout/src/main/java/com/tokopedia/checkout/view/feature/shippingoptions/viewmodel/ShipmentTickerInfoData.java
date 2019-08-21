package com.tokopedia.checkout.view.feature.shippingoptions.viewmodel;

import com.tokopedia.logisticcart.shipping.model.ShipmentOptionData;

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
