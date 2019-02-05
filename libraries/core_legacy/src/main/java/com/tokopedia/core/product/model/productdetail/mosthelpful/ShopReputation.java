
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopReputation {

    @SerializedName("tooltip")
    @Expose
    private String tooltip;
    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("min_badge_score")
    @Expose
    private Integer minBadgeScore;
    @SerializedName("reputation_badge_url")
    @Expose
    private String reputationBadgeUrl;
    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getReputationScore() {
        return reputationScore;
    }

    public void setReputationScore(String reputationScore) {
        this.reputationScore = reputationScore;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMinBadgeScore() {
        return minBadgeScore;
    }

    public void setMinBadgeScore(Integer minBadgeScore) {
        this.minBadgeScore = minBadgeScore;
    }

    public String getReputationBadgeUrl() {
        return reputationBadgeUrl;
    }

    public void setReputationBadgeUrl(String reputationBadgeUrl) {
        this.reputationBadgeUrl = reputationBadgeUrl;
    }

    public ReputationBadge getReputationBadge() {
        return reputationBadge;
    }

    public void setReputationBadge(ReputationBadge reputationBadge) {
        this.reputationBadge = reputationBadge;
    }

}
