package com.tokopedia.tkpd.beranda.domain.model.toppicks;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class TopPicksGroupsModel {

    @SerializedName("name")
    private String name;
    @SerializedName("toppicks")
    private List<TopPicksModel> toppicks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TopPicksModel> getToppicks() {
        return toppicks;
    }

    public void setToppicks(List<TopPicksModel> toppicks) {
        this.toppicks = toppicks;
    }

}
