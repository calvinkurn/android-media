
package com.tokopedia.pms.proof.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataUploadProof {

    @SerializedName("uploadProof")
    @Expose
    private UploadProof uploadProof;

    public UploadProof getUploadProof() {
        return uploadProof;
    }

    public void setUploadProof(UploadProof uploadProof) {
        this.uploadProof = uploadProof;
    }

}
