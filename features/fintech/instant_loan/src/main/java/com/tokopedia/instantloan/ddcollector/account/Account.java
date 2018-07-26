//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector.account;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.tokopedia.instantloan.ddcollector.BaseCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account extends BaseCollector {

    public static final String DD_ACCOUNT = "account";
    public static final String NAME = "account";
    public static final String TYPE = "type";

    private AccountManager mAccountManager;

    public Account(@NonNull AccountManager accountManager) {
        this.mAccountManager = accountManager;
    }

    public String getType() {
        return DD_ACCOUNT;
    }

    @SuppressLint({"MissingPermission"})
    public Object getData() {
        List<Map<String, String>> accounts = new ArrayList<>();
        Map<String, String> accountMap;
        for (android.accounts.Account account : mAccountManager.getAccounts()) {
            accountMap = new HashMap();
            accountMap.put(TYPE, account.type);
            accountMap.put(NAME, account.name);
            accounts.add(accountMap);
        }

        return accounts;
    }
}
