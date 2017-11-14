package com.tokopedia.core.network.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/29/17.
 */

public class InterruptConfirmationMetaEntity {
    @SerializedName("tos_tokopedia")
    @Expose
    InterruptTokopediaConfirmationExceptionEntity tosTokopediaConfirmationExceptionEntity;
    @SerializedName("tos_accept_confirmation")
    @Expose
    TosAcceptConfirmationEntity tosAcceptConfirmationEntity;
    @SerializedName("surge_confirmation")
    @Expose
    SurgeConfirmationEntity surgeConfirmationEntity;
    @SerializedName("wallet_activation")
    @Expose
    InterruptMetaEntity walletActivationEntity;
    @SerializedName("wallet_topup")
    @Expose
    InterruptMetaEntity walletTopupEntity;
    @SerializedName("interrupt")
    @Expose
    InterruptMetaEntity interruptEntity;
    @SerializedName("pending_fare")
    @Expose
    InterruptMetaEntity pendingFareEntity;

    public TosAcceptConfirmationEntity getTosAcceptConfirmationEntity() {
        return tosAcceptConfirmationEntity;
    }

    public SurgeConfirmationEntity getSurgeConfirmationEntity() {
        return surgeConfirmationEntity;
    }

    public InterruptMetaEntity getWalletActivationEntity() {
        return walletActivationEntity;
    }

    public InterruptMetaEntity getWalletTopupEntity() {
        return walletTopupEntity;
    }

    public InterruptMetaEntity getInterruptEntity() {
        return interruptEntity;
    }

    public InterruptTokopediaConfirmationExceptionEntity getTosTokopediaConfirmationExceptionEntity() {
        return tosTokopediaConfirmationExceptionEntity;
    }

    public InterruptMetaEntity getPendingFareEntity() {
        return pendingFareEntity;
    }
}
