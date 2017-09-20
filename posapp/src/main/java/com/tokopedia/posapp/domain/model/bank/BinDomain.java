package com.tokopedia.posapp.domain.model.bank;

/**
 * Created by okasurya on 9/19/17.
 */

public class BinDomain {
    private int bankId;
    private String bin;

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }
}
