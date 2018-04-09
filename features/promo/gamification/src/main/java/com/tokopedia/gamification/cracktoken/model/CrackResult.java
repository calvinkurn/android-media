package com.tokopedia.gamification.cracktoken.model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class CrackResult {

    private CrackResultStatus resultStatus;
    private String imageUrl;
    private Bitmap imageBitmap;
    private String benefitType;
    private List<CrackBenefit> benefits;
    private CrackButton ctaButton;
    private CrackButton returnButton;
    private String benefitLabel;

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

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public void setBenefitLabel(String benefitLabel) {
        this.benefitLabel = benefitLabel;
    }

    public String getBenefitLabel() {
        return benefitLabel;
    }

    public boolean isCrackTokenSuccess() {
        return resultStatus.getCode().equals("200");
    }

    public boolean isCrackTokenExpired() {
        return resultStatus.getCode().equals("42503") || resultStatus.getCode().equals("42504");
    }

    public boolean isTryAgainBtn() {
        return resultStatus.getCode().equals("500");
    }

    public boolean isCrackButtonDismiss(CrackButton crackButton) {
        return crackButton.getType().equals("dismiss");
    }

    public boolean isCrackButtonRedirect(CrackButton crackButton) {
        return crackButton.getType().equals("redirect");
    }
}
