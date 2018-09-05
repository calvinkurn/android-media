package com.tokopedia.recentview.domain.model;

/**
 * @author by nisie on 7/3/17.
 */

public class RecentViewShopDomain {

    private String id;
    private String name;
    private String goldStatus;
    private String lucky;
    private String location;

    public RecentViewShopDomain(String id, String name,
                                String goldStatus, String lucky, String location) {
        this.id = id;
        this.name = name;
        this.goldStatus = goldStatus;
        this.lucky = lucky;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoldStatus() {
        return goldStatus;
    }

    public void setGoldStatus(String goldStatus) {
        this.goldStatus = goldStatus;
    }

    public String getLucky() {
        return lucky;
    }

    public void setLucky(String lucky) {
        this.lucky = lucky;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
