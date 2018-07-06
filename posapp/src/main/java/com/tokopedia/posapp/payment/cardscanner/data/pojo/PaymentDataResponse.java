package com.tokopedia.posapp.payment.cardscanner.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.posapp.bank.data.pojo.BankItemResponse;
import com.tokopedia.posapp.base.data.pojo.PosResponse;
import com.tokopedia.posapp.cart.data.pojo.CheckoutDataResponse;

/**
 * Created by okasurya on 10/9/17.
 */

public class PaymentDataResponse {
    @SerializedName("checkout_data")
    @Expose
    private PosResponse<CheckoutDataResponse> checkoutDataResponse;

    @SerializedName("selectedBankData")
    @Expose
    private BankItemResponse bankData;

    @SerializedName("selectedEmiId")
    @Expose
    private Integer selectedEmiId;

    @SerializedName("ccNum")
    @Expose
    private String ccNum;

    @SerializedName("cvv")
    @Expose
    private String cvv;

    @SerializedName("mon")
    @Expose
    private int mon;

    @SerializedName("year")
    @Expose
    private int year;

    public PosResponse<CheckoutDataResponse> getCheckoutDataResponse() {
        return checkoutDataResponse;
    }

    public void setCheckoutDataResponse(PosResponse<CheckoutDataResponse> checkoutDataResponse) {
        this.checkoutDataResponse = checkoutDataResponse;
    }

    public BankItemResponse getBankData() {
        return bankData;
    }

    public void setBankData(BankItemResponse bankData) {
        this.bankData = bankData;
    }

    public Integer getSelectedEmiId() {
        return selectedEmiId;
    }

    public void setSelectedEmiId(Integer selectedEmiId) {
        this.selectedEmiId = selectedEmiId;
    }

    public String getCcNum() {
        return ccNum;
    }

    public void setCcNum(String ccNum) {
        this.ccNum = ccNum;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
