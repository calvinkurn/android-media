package com.tokopedia.core.manage.people.bank.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class BcaOneClickUserModel {

    @SerializedName("token_id")
    String tokenId;

    @SerializedName("credential_type")
    String credentialType;

    @SerializedName("credential_no")
    String credentialNo;

    @SerializedName("max_limit")
    String maxLimit;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialNo() {
        return credentialNo;
    }

    public void setCredentialNo(String credentialNo) {
        this.credentialNo = credentialNo;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }
}
