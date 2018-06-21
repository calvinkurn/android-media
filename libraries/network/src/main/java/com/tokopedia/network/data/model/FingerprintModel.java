package com.tokopedia.network.data.model;

/**
 * @author ricoharisin .
 */

public class FingerprintModel {

    public String getFingerprintHash() {
        return fingerprintHash;
    }

    public void setFingerprintHash(String fingerprintHash) {
        this.fingerprintHash = fingerprintHash;
    }

    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }

    public String getRegistrarionId() {
        return registrarionId;
    }

    public void setRegistrarionId(String registrarionId) {
        this.registrarionId = registrarionId;
    }

    private String fingerprintHash;

    private String adsId;

    private String registrarionId;
}
