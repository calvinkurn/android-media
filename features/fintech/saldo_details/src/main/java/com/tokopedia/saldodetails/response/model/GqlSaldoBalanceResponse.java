package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

public class GqlSaldoBalanceResponse {

    @SerializedName("balance")
    private Saldo saldo;

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
        this.saldo = saldo;
    }

    public class Saldo {

        @SerializedName("buyer_hold")
        private float buyerHold;

        @SerializedName("buyer_hold_fmt")
        private String buyerHoldFmt;

        @SerializedName("buyer_usable")
        private float buyerUsable;

        @SerializedName("buyer_usable_fmt")
        private String buyerUsableFmt;

        @SerializedName("seller_hold")
        private float sellerHold;

        @SerializedName("seller_hold_fmt")
        private String sellerHoldFmt;

        @SerializedName("seller_usable")
        private float sellerUsable;

        @SerializedName("seller_usable_fmt")
        private String sellerUsableFmt;

        public float getBuyerHold() {
            return buyerHold;
        }

        public void setBuyerHold(float buyerHold) {
            this.buyerHold = buyerHold;
        }

        public String getBuyerHoldFmt() {
            return buyerHoldFmt;
        }

        public void setBuyerHoldFmt(String buyerHoldFmt) {
            this.buyerHoldFmt = buyerHoldFmt;
        }

        public float getBuyerUsable() {
            return buyerUsable;
        }

        public void setBuyerUsable(float buyerUsable) {
            this.buyerUsable = buyerUsable;
        }

        public String getBuyerUsableFmt() {
            return buyerUsableFmt;
        }

        public void setBuyerUsableFmt(String buyerUsableFmt) {
            this.buyerUsableFmt = buyerUsableFmt;
        }

        public float getSellerHold() {
            return sellerHold;
        }

        public void setSellerHold(float sellerHold) {
            this.sellerHold = sellerHold;
        }

        public String getSellerHoldFmt() {
            return sellerHoldFmt;
        }

        public void setSellerHoldFmt(String sellerHoldFmt) {
            this.sellerHoldFmt = sellerHoldFmt;
        }

        public float getSellerUsable() {
            return sellerUsable;
        }

        public void setSellerUsable(float sellerUsable) {
            this.sellerUsable = sellerUsable;
        }

        public String getSellerUsableFmt() {
            return sellerUsableFmt;
        }

        public void setSellerUsableFmt(String sellerUsableFmt) {
            this.sellerUsableFmt = sellerUsableFmt;
        }
    }
}
