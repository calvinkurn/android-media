package com.tokopedia.navigation_common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class WalletPref {
    private static final String WALLET_PREF = "wallet.pref";

    private SharedPreferences preferences;
    private Gson gson;

    public WalletPref(Context context, Gson gson) {
        preferences = context.getSharedPreferences(WALLET_PREF, Context.MODE_PRIVATE);
        this.gson = gson;
    }

    public void saveWallet(WalletModel wallet){
        SharedPreferences.Editor editor = preferences.edit();
        String jsonWallet = gson.toJson(wallet);
        editor.putString(WALLET_PREF, jsonWallet).apply();
    }

    public WalletModel retrieveWallet(){
        String jsonWallet = preferences.getString(WALLET_PREF, null);
        return gson.fromJson(jsonWallet, WalletModel.class);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
