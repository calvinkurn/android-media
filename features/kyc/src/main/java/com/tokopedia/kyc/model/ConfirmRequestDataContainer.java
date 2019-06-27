package com.tokopedia.kyc.model;

public class ConfirmRequestDataContainer {
    private int kycReqId;
    private String cardIdImage;
    private boolean flipCardIdImg;
    private String selfieIdImage;
    private boolean flipSelfieIdImg;
    private String documentType;
    private String documentNumber;
    private String mothersMaidenName;
    private int cardIdDocumentId;
    private int selfieIdDocumentId;

    public ConfirmRequestDataContainer(){}

    public int getKycReqId() {
        return kycReqId;
    }

    public void setKycReqId(int kycReqId) {
        this.kycReqId = kycReqId;
    }

    public String getCardIdImage() {
        return cardIdImage;
    }

    public void setCardIdImage(String cardIdImage) {
        this.cardIdImage = cardIdImage;
    }

    public String getSelfieIdImage() {
        return selfieIdImage;
    }

    public void setSelfieIdImage(String selfieIdImage) {
        this.selfieIdImage = selfieIdImage;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getMothersMaidenName() {
        return mothersMaidenName;
    }

    public void setMothersMaidenName(String mothersMaidenName) {
        this.mothersMaidenName = mothersMaidenName;
    }

    public int getCardIdDocumentId() {
        return cardIdDocumentId;
    }

    public void setCardIdDocumentId(int cardIdDocumentId) {
        this.cardIdDocumentId = cardIdDocumentId;
    }

    public int getSelfieIdDocumentId() {
        return selfieIdDocumentId;
    }

    public void setSelfieIdDocumentId(int selfieIdDocumentId) {
        this.selfieIdDocumentId = selfieIdDocumentId;
    }

    public boolean isFlipCardIdImg() {
        return flipCardIdImg;
    }

    public void setFlipCardIdImg(boolean flipCardIdImg) {
        this.flipCardIdImg = flipCardIdImg;
    }

    public boolean isFlipSelfieIdImg() {
        return flipSelfieIdImg;
    }

    public void setFlipSelfieIdImg(boolean flipSelfieIdImg) {
        this.flipSelfieIdImg = flipSelfieIdImg;
    }
}
