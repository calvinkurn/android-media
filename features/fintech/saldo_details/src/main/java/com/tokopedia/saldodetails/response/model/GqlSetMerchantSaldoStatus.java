package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GqlSetMerchantSaldoStatus {

    @SerializedName("sp_togglesaldoprioritas")
    @Expose
    private MerchantSaldoStatus merchantSaldoStatus;


    public MerchantSaldoStatus getMerchantSaldoStatus() {
        return merchantSaldoStatus;
    }

    public void setMerchantSaldoStatus(MerchantSaldoStatus merchantSaldoStatus) {
        this.merchantSaldoStatus = merchantSaldoStatus;
    }

    public class MerchantSaldoStatus {

        @SerializedName("success")
        @Expose
        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
