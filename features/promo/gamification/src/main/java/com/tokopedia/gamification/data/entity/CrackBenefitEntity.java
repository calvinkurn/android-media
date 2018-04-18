package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class CrackBenefitEntity {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("size")
    @Expose
    private String size;

    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }
}
