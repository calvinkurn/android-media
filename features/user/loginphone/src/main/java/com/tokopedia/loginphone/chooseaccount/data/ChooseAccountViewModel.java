package com.tokopedia.loginphone.chooseaccount.data;

/**
 * @author by nisie on 12/5/17.
 */

public class ChooseAccountViewModel {

    private AccountList accountList;
    private String phoneNumber;
    private String accessToken;
    private String loginType;

    public ChooseAccountViewModel(String phoneNumber,
                                  String accessToken,
                                  String loginType) {
        this.accountList = new AccountList();
        this.phoneNumber = phoneNumber;
        this.accessToken = accessToken;
        this.loginType = loginType;
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

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
