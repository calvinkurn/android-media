package com.tokopedia.home.beranda.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Spotlight {

    @SerializedName("spotlights")
    @Expose
    private List<SpotlightItem> spotlights = new ArrayList<>();

    public List<SpotlightItem> getSpotlights() {
        return spotlights;
    }

    public void setSpotlights(List<SpotlightItem> spotlights) {
        this.spotlights = spotlights;
    }
}
