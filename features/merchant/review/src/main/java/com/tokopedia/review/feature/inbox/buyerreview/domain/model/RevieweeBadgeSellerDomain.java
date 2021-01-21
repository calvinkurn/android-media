package com.tokopedia.review.feature.inbox.buyerreview.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class RevieweeBadgeSellerDomain {

    private String tooltip;
    private String reputationScore;
    private int score;
    private int minBadgeScore;
    private String reputationBadgeUrl;
    private ReputationBadgeDomain reputationBadge;
    private int isFavorited;

    public RevieweeBadgeSellerDomain(String tooltip, String reputationScore, int score,
                                     int minBadgeScore, String reputationBadgeUrl,
                                     ReputationBadgeDomain reputationBadge) {
        this.tooltip = tooltip;
        this.reputationScore = reputationScore;
        this.score = score;
        this.minBadgeScore = minBadgeScore;
        this.reputationBadgeUrl = reputationBadgeUrl;
        this.reputationBadge = reputationBadge;
    }

    public int getIsFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(int isFavorited) {
        this.isFavorited = isFavorited;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getReputationScore() {
        return reputationScore;
    }

    public int getScore() {
        return score;
    }

    public int getMinBadgeScore() {
        return minBadgeScore;
    }

    public String getReputationBadgeUrl() {
        return reputationBadgeUrl;
    }

    public ReputationBadgeDomain getReputationBadge() {
        return reputationBadge;
    }
}
