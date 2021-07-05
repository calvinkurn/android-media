package com.tokopedia.review.feature.inbox.buyerreview.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class ReputationBadgeDomain {

    private int level;
    private int set;

    public ReputationBadgeDomain(int level, int set) {
        this.level = level;
        this.set = set;
    }

    public int getLevel() {
        return level;
    }

    public int getSet() {
        return set;
    }
}
