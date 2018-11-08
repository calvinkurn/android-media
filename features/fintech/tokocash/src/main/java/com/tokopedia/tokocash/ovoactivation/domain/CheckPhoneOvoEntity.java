package com.tokopedia.tokocash.ovoactivation.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public class CheckPhoneOvoEntity {

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("is_registered")
    @Expose
    private boolean isRegistered;
    @SerializedName("registered_applink")
    @Expose
    private String registeredApplink;
    @SerializedName("not_registered_applink")
    @Expose
    private String notRegisteredApplink;
    @SerializedName("change_msisdn_applink")
    @Expose
    private String changeMsisdnApplink;
    @SerializedName("is_allow")
    @Expose
    private boolean isAllow;
    @SerializedName("action")
    @Expose
    private PhoneActionEntity phoneActionEntity;
    @SerializedName("errors")
    @Expose
    private ErrorModelEntity errors;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public String getRegisteredApplink() {
        return registeredApplink;
    }

    public String getNotRegisteredApplink() {
        return notRegisteredApplink;
    }

    public String getChangeMsisdnApplink() {
        return changeMsisdnApplink;
    }

    public boolean isAllow() {
        return isAllow;
    }

    public PhoneActionEntity getPhoneActionEntity() {
        return phoneActionEntity;
    }

    public ErrorModelEntity getErrors() {
        return errors;
    }
}
