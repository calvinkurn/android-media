package com.tokopedia.gamification.cracktoken.model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class CrackResult {

    public static final String TYPE_BTN_INVISIBLE = "invisible";
    public static final String TYPE_BTN_DISMISS = "dismiss";
    public static final String TYPE_BTN_REDIRECT = "redirect";
    public static final String STATUS_CODE_SERVER_ERROR = "500";
    public static final String STATUS_CODE_TOKEN_HAS_BEEN_CRACKED = "42501";
    private static final String STATUS_CODE_SUCCESS = "200";
    private static final String STATUS_CODE_TOKEN_EXPIRED = "42503";
    private static final String STATUS_CODE_CAMPAIGN_EXPIRED = "42504";

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
        return resultStatus.getCode().equals(STATUS_CODE_SUCCESS);
    }

    public boolean isCrackTokenExpired() {
        return resultStatus.getCode().equals(STATUS_CODE_TOKEN_EXPIRED) ||
                resultStatus.getCode().equals(STATUS_CODE_CAMPAIGN_EXPIRED);
    }

    public boolean isTokenHasBeenCracked() {
        return resultStatus.getCode().equals(STATUS_CODE_TOKEN_HAS_BEEN_CRACKED);
    }

    public boolean isTryAgainBtn() {
        return resultStatus.getCode().equals(STATUS_CODE_SERVER_ERROR);
    }

    public boolean isCrackButtonVisible(CrackButton crackButton) {
        return !crackButton.getType().equals(TYPE_BTN_INVISIBLE);
    }

    public boolean isCrackButtonDismiss(CrackButton crackButton) {
        return crackButton.getType().equals(TYPE_BTN_DISMISS);
    }

    public boolean isCrackButtonRedirect(CrackButton crackButton) {
        return crackButton.getType().equals(TYPE_BTN_REDIRECT);
    }
}
