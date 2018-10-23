package com.tokopedia.kelontongapp.firebase;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.kelontongapp.KelontongMainActivity;
import com.tokopedia.kelontongapp.KelontongMainApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by meta on 16/10/18.
 */
public class Preference {

    private static SharedPreferences sharedPrefences(Context context) {
        return context.getSharedPreferences(KelontongMainApplication.class.getName(), MODE_PRIVATE);
    }

    public static void saveFcmToken(Context context, String token) {
        SharedPreferences.Editor editor = sharedPrefences(context).edit();
        editor.putString(FirebaseIDService.class.getName(), token);
        editor.apply();
    }

    public static String getFcmToken(Context context) {
        SharedPreferences preferences = sharedPrefences(context);
        return preferences.getString(FirebaseIDService.class.getName(), "");
    }

    public static void saveFirstTime(Context context) {
        SharedPreferences.Editor editor = sharedPrefences(context).edit();
        editor.putString(KelontongMainActivity.class.getName(), "1");
        editor.apply();
    }

    public static boolean isFirstTime(Context context) {
        SharedPreferences preferences = sharedPrefences(context);
        String isUserFirstTime = preferences.getString(KelontongMainActivity.class.getName(), "");
        return isUserFirstTime.isEmpty();
    }
}
