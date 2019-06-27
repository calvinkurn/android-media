package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoPointStatusEntity {

    @Expose
    @SerializedName("points")
    TokoPointStatusPointsEntity points;

    public TokoPointStatusPointsEntity getPoints() {
        return points;
    }

    public void setPoints(TokoPointStatusPointsEntity points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "TokoPointStatusEntity{" +
                ", points=" + points +
                '}';
    }
}
