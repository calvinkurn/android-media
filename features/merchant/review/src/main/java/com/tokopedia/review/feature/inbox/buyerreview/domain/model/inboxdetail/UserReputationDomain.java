package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;

/**
 * @author by nisie on 8/23/17.
 */

public class UserReputationDomain {
    private int positive;
    private int neutral;
    private int negative;
    private String positivePercentage;
    private int noReputation;

    public UserReputationDomain(int positive, int neutral, int negative,
                                String positivePercentage, int noReputation) {
        this.positive = positive;
        this.neutral = neutral;
        this.negative = negative;
        this.positivePercentage = positivePercentage;
        this.noReputation = noReputation;
    }

    public int getPositive() {
        return positive;
    }

    public int getNeutral() {
        return neutral;
    }

    public int getNegative() {
        return negative;
    }

    public String getPositivePercentage() {
        return positivePercentage;
    }

    public int getNoReputation() {
        return noReputation;
    }
}
