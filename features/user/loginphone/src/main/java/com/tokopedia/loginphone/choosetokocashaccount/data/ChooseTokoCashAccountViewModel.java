package com.tokopedia.loginphone.choosetokocashaccount.data;

import com.tokopedia.loginphone.choosetokocashaccount.AccountList;
import com.tokopedia.loginphone.choosetokocashaccount.UserDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 12/5/17.
 */

public class ChooseTokoCashAccountViewModel {

    public static final String ARGS_DATA = "data";

    private AccountList accountList;
    private String phoneNumber;
    private String accessToken;

    public ChooseTokoCashAccountViewModel(String phoneNumber,
                                          String accessToken) {
        this.accountList = new AccountList();
        this.phoneNumber = phoneNumber;
        this.accessToken = accessToken;
    }

    public AccountList getAccountList() {
        return accountList;
    }

    public void setAccountList(AccountList accountList) {
        this.accountList = accountList;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    //UUID
    public String getAccessToken() {
        return accessToken;
    }
}
