package com.tokopedia.core.product.model.productdetail.discussion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 8/22/17.
 */

class CommentShopReputation {

    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;
    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;

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
}
