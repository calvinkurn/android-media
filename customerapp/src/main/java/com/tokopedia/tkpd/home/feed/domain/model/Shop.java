package com.tokopedia.tkpd.home.feed.domain.model;

/**
 * @author Kulomady on 12/8/16.
 */

public class Shop {
    private String id;
    private String name;
    private String goldStatus;
    private String lucky;
    private String location;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoldStatus(String goldStatus) {
        this.goldStatus = goldStatus;
    }

    public void setLucky(String lucky) {
        this.lucky = lucky;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGoldStatus() {
        return goldStatus;
    }

    public String getLucky() {
        return lucky;
    }

    public String getLocation() {
        return location;
    }

}
