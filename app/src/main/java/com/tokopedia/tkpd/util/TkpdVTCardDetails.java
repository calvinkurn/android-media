package com.tokopedia.tkpd.util;

import android.util.Log;

import id.co.veritrans.android.api.VTModel.VTCardDetails;
import id.co.veritrans.android.api.VTUtil.VTConfig;

/**
 * Created by ricoharisin on 10/21/15.
 */
public class TkpdVTCardDetails extends VTCardDetails {

    private String card_number;
    private int card_exp_month;
    private int card_exp_year;
    private String card_cvv;
    private boolean secure;
    private String bank = null;
    private String gross_amount;
    private String installment = "false";
    private String installment_term = "0";

    public TkpdVTCardDetails() {
    }

    public String getCard_number() {
        return this.card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public int getCard_exp_month() {
        return this.card_exp_month;
    }

    public void setCard_exp_month(int card_exp_month) {
        this.card_exp_month = card_exp_month;
    }

    public int getCard_exp_year() {
        return this.card_exp_year;
    }

    public void setCard_exp_year(int card_exp_year) {
        this.card_exp_year = card_exp_year;
    }

    public String getCard_cvv() {
        return this.card_cvv;
    }

    public void setCard_cvv(String card_cvv) {
        this.card_cvv = card_cvv;
    }

    public boolean isSecure() {
        return this.secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getBank() {
        return this.bank;
    }

    public void setBank(String bank) {
        this.bank = bank.toLowerCase();
    }

    public String getGross_amount() {
        return this.gross_amount;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public void setInstallment(Boolean isInstallment) {
        if (isInstallment) installment = "true";
    }

    public void setInstallmentTerm(String term) {
        this.installment_term = term;
    }

    public String getParamUrl() {
        String paramUrl = "?card_number=" + this.card_number + "&card_exp_month=" + this.card_exp_month + "&card_exp_year=" + this.card_exp_year + "&card_cvv=" + this.card_cvv + "&client_key=" + VTConfig.CLIENT_KEY + "&secure=" + Boolean.toString(this.secure) + this.getBankParam() + "&gross_amount=" + this.gross_amount + "&installment=" + this.installment + "&installment_term=" + this.installment_term;
        Log.i("Veritrans Credit Card", paramUrl);
        return paramUrl;
    }

    public String getBankParam() {
        return this.bank != null?"&bank=" + this.bank:"";
    }
}
