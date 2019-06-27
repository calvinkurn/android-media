
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommissionFee {

    @SerializedName("FeeId")
    @Expose
    private Integer feeId;
    @SerializedName("Percentage")
    @Expose
    private Integer percentage;

    public Integer getFeeId() {
        return feeId;
    }

    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

}
