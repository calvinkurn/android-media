package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class CrackResultEntity {
    
    @SerializedName("resultStatus")
    @Expose
    private CrackResultStatusEntity resultStatus;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("benefitType")
    @Expose
    private String benefitType;

    @SerializedName("benefits")
    @Expose
    private List<CrackBenefitEntity> benefits;

    @SerializedName("ctaButton")
    @Expose
    private CrackButtonEntity ctaButton;

    @SerializedName("returnButton")
    @Expose
    private CrackButtonEntity returnButton;

    public CrackResultStatusEntity getResultStatus() {
        return resultStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBenefitType() {
        return benefitType;
    }

    public List<CrackBenefitEntity> getBenefits() {
        return benefits;
    }

    public CrackButtonEntity getCtaButton() {
        return ctaButton;
    }

    public CrackButtonEntity getReturnButton() {
        return returnButton;
    }
}
