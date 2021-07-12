package com.tokopedia.navigation_common.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

public class WalletPref {
    private static final String WALLET_PREF = "wallet.pref";
    private static final String DEBIT_INSTANT_URL = "debit_instant_url";

    private SharedPreferences preferences;
    private Gson gson;

    public WalletPref(Context context, Gson gson) {
        preferences = context.getSharedPreferences(WALLET_PREF, Context.MODE_PRIVATE);
        this.gson = gson;
    }

    public void saveWallet(WalletModel wallet) {
        SharedPreferences.Editor editor = preferences.edit();
        String jsonWallet = gson.toJson(wallet);
        editor.putString(WALLET_PREF, jsonWallet).apply();
    }

    public void saveDebitInstantUrl(String url) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEBIT_INSTANT_URL, url).apply();
    }

    public String retrieveDebitInstantUrl() {
        return preferences.getString(DEBIT_INSTANT_URL, "");
    }

    public WalletModel retrieveWallet() {
        try {
            String jsonWallet = preferences.getString(WALLET_PREF, null);
            if(TextUtils.isEmpty(jsonWallet)) {
                return null;
            } else {
                return gson.fromJson(jsonWallet, WalletModel.class);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
