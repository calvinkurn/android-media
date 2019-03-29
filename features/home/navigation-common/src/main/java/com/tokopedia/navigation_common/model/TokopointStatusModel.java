package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class TokopointStatusModel {
    @SerializedName("tier")
    @Expose
    private TokopointTierModel tier = new TokopointTierModel();
    @SerializedName("points")
    @Expose
    private PointsModel points = new PointsModel();

    public TokopointTierModel getTier() {
        return tier;
    }

    public void setTier(TokopointTierModel tier) {
        this.tier = tier;
    }

    public PointsModel getPoints() {
        return points;
    }

    public void setPoints(PointsModel points) {
        this.points = points;
    }
}
