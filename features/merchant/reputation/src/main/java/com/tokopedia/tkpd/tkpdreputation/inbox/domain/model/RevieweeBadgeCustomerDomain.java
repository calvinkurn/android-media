package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/16/17.
 */

public class RevieweeBadgeCustomerDomain {

    private int positive;
    private int neutral;
    private int negative;
    private String positivePercentage;
    private int noReputation;

    public RevieweeBadgeCustomerDomain(int positive, int neutral,
                                       int negative, String positivePercentage,
                                       int noReputation) {
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
