package com.tokopedia.core.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by stevenfredian on 7/25/17.
 */

public class TokenSessionHelper {


    public static void saveRefreshToken(Context context, String email, String refreshToken) {
        final Account account = new Account(email, AccountGeneral.ACCOUNT_TYPE);
        AccountManager accountManager = AccountManager.get(context);
        accountManager.addAccountExplicitly(account, refreshToken, null);
        accountManager.setAuthToken(account, AccountGeneral.ACCOUNT_TYPE, refreshToken);
    }

    public static String getExistingAccountAuthToken(Context context, String authTokenType) {
        AccountManager mAccountManager = AccountManager.get(context);
        Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        String authToken = mAccountManager.peekAuthToken(availableAccounts[0], AccountGeneral.ACCOUNT_TYPE);
        return authToken;
    }

    public static void invalidateAccountManager(Context context) {
        AccountManager mAccountManager = AccountManager.get(context);
        Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        mAccountManager.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, getExistingAccountAuthToken(context, AccountGeneral.ACCOUNT_TYPE));
    }

    public static void removeAccountManager(Context context) {
        AccountManager mAccountManager = AccountManager.get(context);
        Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        mAccountManager.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, getExistingAccountAuthToken(context, AccountGeneral.ACCOUNT_TYPE));
        mAccountManager.removeAccount(availableAccounts[0],null,null);
    }
}
