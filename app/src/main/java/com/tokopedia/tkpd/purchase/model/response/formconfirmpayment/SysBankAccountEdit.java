package com.tokopedia.tkpd.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Angga.Prasetiyo
 * on 30/06/2016.
 */
public class SysBankAccountEdit {
    @SerializedName("sysbank_id_chosen")
    @Expose
    private String sysBankIdChosen;
    @SerializedName("sysbank_list")
    @Expose
    private List<SysBankAccount> sysBankList;

    public String getSysBankIdChosen() {
        return sysBankIdChosen;
    }

    public void setSysBankIdChosen(String sysBankIdChosen) {
        this.sysBankIdChosen = sysBankIdChosen;
    }

    public List<SysBankAccount> getSysBankList() {
        return sysBankList;
    }

    public void setSysBankList(List<SysBankAccount> sysBankList) {
        this.sysBankList = sysBankList;
    }
}
