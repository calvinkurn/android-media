package com.tokopedia.home.beranda.domain.model;

import com.tokopedia.analytic_constant.DataLayer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Spotlight {

    @SerializedName("spotlights")
    @Expose
    private List<SpotlightItem> spotlights = new ArrayList<>();

    private String channelId = "";

    private String promoName = "";
    private String homeAttribution = "";

    public List<SpotlightItem> getSpotlights() {
        return spotlights;
    }

    public void setSpotlights(List<SpotlightItem> spotlights) {
        this.spotlights = spotlights;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setHomeAttribution(String homeAttribution) {
        this.homeAttribution = homeAttribution;
    }

    public Map<String, Object> getEnhanceImpressionSpotlightHomePage() {
        List<Object> list = convertPromoEnhanceSpotlight();
        return DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "homepage",
                "eventAction", "impression on banner spotlight",
                "eventLabel", "",
                "channelId", channelId,
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )
                )
        );
    }

    private List<Object> convertPromoEnhanceSpotlight() {
        List<Object> list = new ArrayList<>();

        if (spotlights != null) {
            for (int i = 0; i < spotlights.size(); i++) {
                SpotlightItem item = spotlights.get(i);
                list.add(
                        DataLayer.mapOf(
                                "id", String.valueOf(item.getId()),
                                "name", promoName,
                                "creative", item.getTitle(),
                                "creative_url", item.getBackgroundImageUrl(),
                                "position", String.valueOf(i + 1)
                        )
                );
            }
        }
        return list;
    }
}
