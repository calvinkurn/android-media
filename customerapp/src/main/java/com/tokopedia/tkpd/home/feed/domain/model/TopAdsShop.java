package com.tokopedia.tkpd.home.feed.domain.model;
import java.util.List;

/**
 * @author Kulomady on 12/8/16.
 */

public class TopAdsShop extends Shop {
    private boolean goldShop;
    private String uri;
    private boolean isOwner;
    private List<Badge> badges;

    public boolean isGoldShop() {
        return goldShop;
    }

    public void setGoldShop(boolean goldShop) {
        this.goldShop = goldShop;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }


}
