package com.tokopedia.digital.tokocash.entity;

import java.util.List;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public class OAuthInfoEntity {

    private List<AccountWalletListEntity> account_list;
    private String tokopedia_user_id;
    private String email;
    private String name;
    private String mobile;

    public List<AccountWalletListEntity> getAccount_list() {
        return account_list;
    }

    public void setAccount_list(List<AccountWalletListEntity> account_list) {
        this.account_list = account_list;
    }

    public String getTokopedia_user_id() {
        return tokopedia_user_id;
    }

    public void setTokopedia_user_id(String tokopedia_user_id) {
        this.tokopedia_user_id = tokopedia_user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
