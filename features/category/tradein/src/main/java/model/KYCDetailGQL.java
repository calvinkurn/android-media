package model;

import com.google.gson.annotations.SerializedName;

public class KYCDetailGQL {
    @SerializedName("kycStatus")
    private KYCDetails kycDetails;

    public KYCDetails getKycDetails() {
        return kycDetails;
    }

    public void setKycDetails(KYCDetails kycDetails) {
        this.kycDetails = kycDetails;
    }
}
