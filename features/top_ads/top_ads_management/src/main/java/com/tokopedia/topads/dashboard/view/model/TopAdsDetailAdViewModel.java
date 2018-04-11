package com.tokopedia.topads.dashboard.view.model;

/**
 * Created by Nathaniel on 2/23/2017.
 */

public interface TopAdsDetailAdViewModel {

    long getId();

    long getShopId();

    int getStatus();

    void setTitle(String title);

    String getTitle();

    float getPriceBid();

    void setPriceBid(float priceBid);

    boolean isBudget();

    void setBudget(boolean budget);

    float getPriceDaily();

    void setPriceDaily(float priceDaily);

    boolean isScheduled();

    void setScheduled(boolean scheduled);

    String getStartDate();

    void setStartDate(String startDate);

    String getStartTime();

    void setStartTime(String startTime);

    String getEndDate();

    void setEndDate(String endDate);

    String getEndTime();

    void setEndTime(String endTime);

    int getStickerId();

    void setStickerId(int stickerId);
}