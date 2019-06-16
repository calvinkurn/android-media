package com.tokopedia.sellerapp.dashboard.view.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author by milhamj on 16/06/19.
 */
public class PowerMerchantPopUpManager {
    private static final String PREF_KEY = "dashboard_power_merchant_pop_up";
    private static final String KEY_IS_EVER_PM = "is_ever_power_merchant_%s";
    private static final String KEY_ACTIVE_PM_SHOWN = "active_power_merchant_shown_%s";
    private static final String KEY_IDLE_PM_SHOWN = "idle_power_merchant_shown_%s";
    private static final String KEY_RM_SHOWN = "regular_merchant_shown_%s";

    private SharedPreferences preferences;

    public PowerMerchantPopUpManager(Context context) {
        this.preferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

    public boolean isEverPowerMerchant(String shopId)  {
        return preferences.getBoolean(String.format(KEY_IS_EVER_PM, shopId), false);
    }

    public void setEverPowerMerchant(String shopId, boolean isEverPowerMerchant) {
        preferences.edit()
                .putBoolean(String.format(KEY_IS_EVER_PM, shopId), isEverPowerMerchant)
                .apply();
    }

    public boolean isActivePowerMerchantShown(String shopId)  {
        return preferences.getBoolean(String.format(KEY_ACTIVE_PM_SHOWN, shopId), false);
    }

    public void setActivePowerMerchantShown(String shopId, boolean isShown) {
        preferences.edit()
                .putBoolean(String.format(KEY_ACTIVE_PM_SHOWN, shopId), isShown)
                .apply();
    }

    public boolean isIdlePowerMerchantShown(String shopId)  {
        return preferences.getBoolean(String.format(KEY_IDLE_PM_SHOWN, shopId), false);
    }

    public void setIdlePowerMerchantShown(String shopId, boolean isShown) {
        preferences.edit()
                .putBoolean(String.format(KEY_IDLE_PM_SHOWN, shopId), isShown)
                .apply();
    }

    public boolean isRegularMerchantShown(String shopId)  {
        return preferences.getBoolean(String.format(KEY_RM_SHOWN, shopId), false);
    }

    public void setRegularMerchantShown(String shopId, boolean isShown) {
        preferences.edit()
                .putBoolean(String.format(KEY_RM_SHOWN, shopId), isShown)
                .apply();
    }
}
