package com.tokopedia.gamification.cracktoken.presentation.model;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class CrackResult {

    private CrackResultStatus resultStatus;
    private String imageUrl;
    private String benefitType;
    private List<CrackBenefit> benefits;
    private CrackButton ctaButton;
    private CrackButton returnButton;

    public CrackResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(CrackResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBenefitType() {
        return benefitType;
    }

    public void setBenefitType(String benefitType) {
        this.benefitType = benefitType;
    }

    public List<CrackBenefit> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<CrackBenefit> benefits) {
        this.benefits = benefits;
    }

    public CrackButton getCtaButton() {
        return ctaButton;
    }

    public void setCtaButton(CrackButton ctaButton) {
        this.ctaButton = ctaButton;
    }

    public CrackButton getReturnButton() {
        return returnButton;
    }

    public void setReturnButton(CrackButton returnButton) {
        this.returnButton = returnButton;
    }
}
