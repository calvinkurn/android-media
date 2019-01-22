
package com.tokopedia.pms.proof.model.getproof;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataResponseGetProof {

    @SerializedName("getProof")
    @Expose
    private GetProof getProof;

    public GetProof getGetProof() {
        return getProof;
    }

    public void setGetProof(GetProof getProof) {
        this.getProof = getProof;
    }

}
