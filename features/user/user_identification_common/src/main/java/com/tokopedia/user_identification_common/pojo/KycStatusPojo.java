package com.tokopedia.user_identification_common.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 15/11/18.
 */
public class KycStatusPojo {

    @Expose
    @SerializedName("Message")
    private List<String> message = new ArrayList<>();

    @Expose
    @SerializedName("Detail")
    private KycStatusDetailPojo kycStatusDetailPojo = new KycStatusDetailPojo();

    public List<String> getMessage() {
        return message;
    }

    public KycStatusDetailPojo getKycStatusDetailPojo() {
        return kycStatusDetailPojo;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public void setKycStatusDetailPojo(KycStatusDetailPojo kycStatusDetailPojo) {
        this.kycStatusDetailPojo = kycStatusDetailPojo;
    }
}
