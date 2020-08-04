package com.tokopedia.sellerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

public class SellerOnboardingPreference {

    private static final String ONBOARDING_PREF = "onboarding_preference";
    public static final String HAS_OPEN_ONBOARDING = "has_open_onboarding";

    private final SharedPreferences sharedPreferences;

    public SellerOnboardingPreference(@ApplicationContext Context context) {
        this.sharedPreferences = context.getSharedPreferences(
                ONBOARDING_PREF,
                Context.MODE_PRIVATE
        );
    }

    public void putBoolean(String key, Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }
}