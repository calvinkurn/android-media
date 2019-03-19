package com.tokopedia.gamification.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

@Entity
public class CrackResultEntity {
    public CrackResultEntity() {
    }


    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "campaignId")
    @Expose(serialize = false, deserialize = false)
    public long campaignId;

    @ColumnInfo(name = "imageUrl")
    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;

    @SerializedName("benefits")
    @Expose
    public List<CrackBenefitEntity> benefits;

    @Ignore
    @SerializedName("resultStatus")
    @Expose
    private ResultStatusEntity resultStatus;

    @Ignore
    @SerializedName("benefitType")
    @Expose
    private String benefitType;

    @Ignore
    @SerializedName("ctaButton")
    @Expose
    private CrackButtonEntity ctaButton;

    @Ignore
    @SerializedName("returnButton")
    @Expose
    private CrackButtonEntity returnButton;

    @Ignore
    @Expose(serialize = false, deserialize = false)
    private String benefitLabel;

    @Ignore
    @Expose(serialize = false, deserialize = false)
    private Bitmap imageBitmap;

    @Ignore
    @Expose(serialize = false, deserialize = false)
    public static final String TYPE_BTN_INVISIBLE = "invisible";

    @Ignore
    @Expose(serialize = false, deserialize = false)
    public static final String TYPE_BTN_DISMISS = "dismiss";

    @Ignore
    @Expose(serialize = false, deserialize = false)
    public static final String TYPE_BTN_REDIRECT = "redirect";

    @Ignore
    @Expose(serialize = false, deserialize = false)
    public static final String STATUS_CODE_SERVER_ERROR = "500";

    @Ignore
    @Expose(serialize = false, deserialize = false)
    public static final String STATUS_CODE_TOKEN_HAS_BEEN_CRACKED = "42501";

    @Ignore
    @Expose(serialize = false, deserialize = false)
    private static final String STATUS_CODE_SUCCESS = "200";

    @Ignore
    @Expose(serialize = false, deserialize = false)
    private static final String STATUS_CODE_TOKEN_USER_INVALID = "42502";

    @Ignore
    @Expose(serialize = false, deserialize = false)
    private static final String STATUS_CODE_TOKEN_EXPIRED = "42503";

    @Ignore
    @Expose(serialize = false, deserialize = false)
    private static final String STATUS_CODE_CAMPAIGN_EXPIRED = "42504";


    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public void setResultStatus(ResultStatusEntity resultStatus) {
        this.resultStatus = resultStatus;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setBenefitType(String benefitType) {
        this.benefitType = benefitType;
    }

    public void setBenefits(List<CrackBenefitEntity> benefits) {
        this.benefits = benefits;
    }

    public void setCtaButton(CrackButtonEntity ctaButton) {
        this.ctaButton = ctaButton;
    }

    public void setReturnButton(CrackButtonEntity returnButton) {
        this.returnButton = returnButton;
    }

    public ResultStatusEntity getResultStatus() {
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

    public String getBenefitLabel() {
        return benefitLabel;
    }

    public void setBenefitLabel(String benefitLabel) {
        this.benefitLabel = benefitLabel;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
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

    public boolean isTokenUserInvalid() {
        return resultStatus.getCode().equals(STATUS_CODE_TOKEN_USER_INVALID);
    }

    public boolean isTryAgainBtn() {
        return resultStatus.getCode().equals(STATUS_CODE_SERVER_ERROR);
    }

    public boolean isCrackButtonVisible(CrackButtonEntity crackButton) {
        return !crackButton.getType().equals(TYPE_BTN_INVISIBLE);
    }

    public boolean isCrackButtonDismiss(CrackButtonEntity crackButton) {
        return crackButton.getType().equals(TYPE_BTN_DISMISS);
    }

    public boolean isCrackButtonRedirect(CrackButtonEntity crackButton) {
        return crackButton.getType().equals(TYPE_BTN_REDIRECT);
    }
}
