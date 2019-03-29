package com.tokopedia.navigation_common.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class WalletPref {
    private static final String WALLET_PREF = "wallet.pref";
    public static final String VCC_PREF = "vcc.pref";
    private static final String TOKOSWIPE_URL = "tokoswipe_url";

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

    public void saveVccUserStatus(VccUserStatus vccUserStatus) {
        SharedPreferences.Editor editor = preferences.edit();
        String jsonVccStatus = gson.toJson(vccUserStatus);
        editor.putString(VCC_PREF, jsonVccStatus).apply();
    }

    public WalletModel retrieveWallet() {
        String jsonWallet = preferences.getString(WALLET_PREF, null);
        return gson.fromJson(jsonWallet, WalletModel.class);
    }

    public VccUserStatus retrieveVccUserStatus() {
        String jsonWallet = preferences.getString(VCC_PREF, null);
        return gson.fromJson(jsonWallet, VccUserStatus.class);
    }

    public void setTokoSwipeUrl(String url) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKOSWIPE_URL, url).apply();
    }

    public String getTokoSwipeUrl() {
        return preferences.getString(TOKOSWIPE_URL, null);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
