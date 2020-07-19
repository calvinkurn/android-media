package com.tokopedia.shop.score.domain.model;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreMainDomainModel {
    private Integer badgeScore;
    private ShopScoreSummaryDomainModelData data;

    public Integer getBadgeScore() {
        return badgeScore;
    }

    public void setBadgeScore(Integer badgeScore) {
        this.badgeScore = badgeScore;
    }

    public ShopScoreSummaryDomainModelData getData() {
        return data;
    }

    public void setData(ShopScoreSummaryDomainModelData data) {
        this.data = data;
    }
}
