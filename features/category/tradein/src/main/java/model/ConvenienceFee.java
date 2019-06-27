
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConvenienceFee {

    @SerializedName("FeeId")
    @Expose
    private Integer feeId;
    @SerializedName("TotalFee")
    @Expose
    private Integer totalFee;

    public Integer getFeeId() {
        return feeId;
    }

    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

}
