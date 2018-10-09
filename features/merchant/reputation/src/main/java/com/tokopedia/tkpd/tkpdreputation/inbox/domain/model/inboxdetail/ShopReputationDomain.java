package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ReputationBadgeDomain;

/**
 * @author by nisie on 8/23/17.
 */

public class ShopReputationDomain {
    private String tooltip;
    private String reputationScore;
    private int score;
    private int minBadgeScore;
    private String reputationBadgeUrl;
    private ReputationBadgeDomain reputationBadge;

    public ShopReputationDomain(String tooltip, String reputationScore, int score,
                                int minBadgeScore, String reputationBadgeUrl,
                                ReputationBadgeDomain reputationBadge) {
        this.tooltip = tooltip;
        this.reputationScore = reputationScore;
        this.score = score;
        this.minBadgeScore = minBadgeScore;
        this.reputationBadgeUrl = reputationBadgeUrl;
        this.reputationBadge = reputationBadge;
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
