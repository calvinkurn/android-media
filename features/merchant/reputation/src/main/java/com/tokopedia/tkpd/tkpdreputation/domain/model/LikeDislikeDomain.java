package com.tokopedia.tkpd.tkpdreputation.domain.model;

/**
 * @author by nisie on 9/29/17.
 */

public class LikeDislikeDomain {

    private int totalLike;
    private int totalDislike;
    private int likeStatus;

    public LikeDislikeDomain(int totalLike, int totalDislike, int likeStatus) {
        this.totalLike = totalLike;
        this.totalDislike = totalDislike;
        this.likeStatus = likeStatus;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public int getTotalDislike() {
        return totalDislike;
    }

    public int getLikeStatus() {
        return likeStatus;
    }
}
