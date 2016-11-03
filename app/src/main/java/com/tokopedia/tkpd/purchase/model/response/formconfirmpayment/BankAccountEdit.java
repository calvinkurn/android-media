package com.tokopedia.tkpd.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo
 * on 30/06/2016.
 */
public class BankAccountEdit {

    @SerializedName("bank_account_list")
    @Expose
    private List<BankAccount> bankAccountList = new ArrayList<>();
    @SerializedName("bank_account_id_chosen")
    @Expose
    private String bankAccountIdChosen;

    public List<BankAccount> getBankAccountList() {
        return bankAccountList;
    }

    public void setBankAccountList(List<BankAccount> bankAccountList) {
        this.bankAccountList = bankAccountList;
    }

    public String getBankAccountIdChosen() {
        return bankAccountIdChosen;
    }

    public void setBankAccountIdChosen(String bankAccountIdChosen) {
        this.bankAccountIdChosen = bankAccountIdChosen;
    }
}
