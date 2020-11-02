
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model;

public class TotalLikeDislikeDomain {
    private int totalLike;
    private int totalDislike;

    public TotalLikeDislikeDomain(int totalLike, int totalDislike) {
        this.totalLike = totalLike;
        this.totalDislike = totalDislike;
    }

    public TotalLikeDislikeDomain() {
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalDislike() {
        return totalDislike;
    }

    public void setTotalDislike(int totalDislike) {
        this.totalDislike = totalDislike;
    }

}
