package com.tokopedia.network.data.model;

import android.text.TextUtils;
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
        if(!TextUtils.isEmpty(adsId)){
            this.adsId = trimGoogleAdId(adsId);
        }else{
            this.adsId = "";
        }
    }

    String trimGoogleAdId(String googleAdsId){
        StringBuilder sb = new StringBuilder(googleAdsId.length());//we know this is the capacity so we initialise with it:
        for (int i = 0; i < googleAdsId.length(); i++) {
            char c = googleAdsId.charAt(i);
            switch (c){
                case '\u2013':
                case '\u2014':
                case '\u2015':
                    sb.append('-');
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public String getRegistrarionId() {
        return registrarionId;
    }

    public void setRegistrarionId(String registrarionId) {
        this.registrarionId = registrarionId;
    }

    public String getTypingVelocity() {
        return typingVelocity;
    }

    public void setTypingVelocity(String typingVelocity) {
        this.typingVelocity = typingVelocity;
    }

    private String fingerprintHash;

    private String adsId;

    private String registrarionId;

    private String typingVelocity;
}
