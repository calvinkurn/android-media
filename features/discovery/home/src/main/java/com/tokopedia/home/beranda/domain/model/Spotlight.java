package com.tokopedia.home.beranda.domain.model;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Spotlight {

    @SerializedName("spotlights")
    @Expose
    private List<SpotlightItem> spotlights = new ArrayList<>();

    private String promoName = "";
    private String homeAttribution = "";

    public List<SpotlightItem> getSpotlights() {
        return spotlights;
    }

    public void setSpotlights(List<SpotlightItem> spotlights) {
        this.spotlights = spotlights;
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

    public Map<String, Object> getEnhanceImpressionSpotlightHomePage(int position) {
        List<Object> list = convertPromoEnhanceSpotlight();
        return DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "homepage",
                "eventAction", "impression on banner spotlight",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )
                ),
                "attribution", getHomeAttribution(position + 1, "")
        );
    }

    private String getHomeAttribution(int position, String creativeName) {
        if (homeAttribution != null)
            return homeAttribution.replace("$1", Integer.toString(position)).replace("$2", (creativeName != null) ? creativeName : "");
        return "";
    }

    private List<Object> convertPromoEnhanceSpotlight() {
        List<Object> list = new ArrayList<>();

        if (spotlights != null) {
            for (int i = 0; i < spotlights.length; i++) {
                SpotlightItem item = spotlights.get(i);
                list.add(
                        DataLayer.mapOf(
                                "id", item.getId(),
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
