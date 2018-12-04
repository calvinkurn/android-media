package com.tokopedia.browse.common.data;

/**
 * @author by furqan on 21/09/18.
 */

public class DigitalBrowsePopularAnalyticsModel {
    private long bannerId;
    private String brandName;
    private int position;

    public long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
