package com.tokopedia.core.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 17/11/2015.
 */
@Parcel
public class ShopRepModel {
    // shop reputation
    @SerializedName("tooltip")
    String tooltip;
    ReputationBadge reputationBadge;
    @SerializedName("reputation_score")
    String reputationScore;
    @SerializedName("min_badge_score")
    int minBadgeScore;// min_badge_score
    @SerializedName("score")
    int score;// score
    // end of shop reputation


    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public ReputationBadge getReputationBadge() {
        return reputationBadge;
    }

    public void setReputationBadge(ReputationBadge reputationBadge) {
        this.reputationBadge = reputationBadge;
    }

    public String getReputationScore() {
        return reputationScore;
    }

    public void setReputationScore(String reputationScore) {
        this.reputationScore = reputationScore;
    }

    public int getMinBadgeScore() {
        return minBadgeScore;
    }

    public void setMinBadgeScore(int minBadgeScore) {
        this.minBadgeScore = minBadgeScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Parcel
    public static class ReputationBadge{
        @SerializedName("level")
        int level;
        @SerializedName("set")
        int set;
    }
}
