
package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail;

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
    private int score;
    @SerializedName("min_badge_score")
    @Expose
    private int minBadgeScore;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMinBadgeScore() {
        return minBadgeScore;
    }

    public void setMinBadgeScore(int minBadgeScore) {
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
