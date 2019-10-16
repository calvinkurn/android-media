package com.tokopedia.network.data.model;

/**
 * @author ricoharisin .
 */

public abstract class AbstractFingerprintModel {
    public abstract String getFingerprintHash();
    public abstract void setFingerprintHash(String fingerprintHash) ;

    public abstract String getAdsId();

    public abstract void setAdsId(String adsId);

    public abstract String getRegistrarionId();

    public abstract void setRegistrarionId(String registrarionId);

    public abstract String getTypingVelocity();

    public abstract void setTypingVelocity(String typingVelocity) ;
}
