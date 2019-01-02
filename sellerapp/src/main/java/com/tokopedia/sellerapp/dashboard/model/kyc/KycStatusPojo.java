package com.tokopedia.sellerapp.dashboard.model.kyc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by nisie on 15/11/18.
 */
public class KycStatusPojo {

    @Expose
    @SerializedName("Message")
    private List<String> message;

    @Expose
    @SerializedName("Detail")
    private KycStatusDetailPojo kycStatusDetailPojo;

    public List<String> getMessage() {
        return message;
    }

    public KycStatusDetailPojo getKycStatusDetailPojo() {
        return kycStatusDetailPojo;
    }
}
