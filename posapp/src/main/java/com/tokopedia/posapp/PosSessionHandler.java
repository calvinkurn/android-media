package com.tokopedia.posapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.database.manager.BankDbManager;
import com.tokopedia.posapp.database.manager.CartDbManager;
import com.tokopedia.posapp.database.manager.EtalaseDbManager;
import com.tokopedia.posapp.database.manager.ProductDbManager;
import com.tokopedia.posapp.database.manager.base.PosDbOperation;
import com.tokopedia.posapp.view.fragment.DialogPasswordFragment;

/**
 * Created by okasurya on 9/26/17.
 */

public class PosSessionHandler extends SessionHandler {

    private static final String OUTLET_ID = "OUTLET_ID";
    private static final String OUTLET_NAME = "OUTLET_NAME";

    public PosSessionHandler(Context context) {
        super(context);
    }

    public static void clearPosUserData(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(OUTLET_ID, null);
        editor.putString(OUTLET_NAME, null);
        editor.apply();
    }

    public static void setOutletId(Context context, String id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(OUTLET_ID, id).apply();
    }

    public static String getOutletId(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(OUTLET_ID, "");
    }

    public static void setOutletName(Context context, String name) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(OUTLET_NAME, name).apply();
    }

    public static String getOutletName(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(OUTLET_NAME, "");
    }

    public void showPasswordDialog(String title, PosSessionHandler.PasswordListener listener) {
        if (getContext() != null && getContext() instanceof AppCompatActivity && getContext() instanceof onLogoutListener) {
            if (((AppCompatActivity) getContext()).getFragmentManager().findFragmentByTag(DialogPasswordFragment.FRAGMENT_TAG) == null) {
                DialogPasswordFragment dialogPasswordFragment = DialogPasswordFragment.newInstance(title);
                dialogPasswordFragment.setListener(listener);
                dialogPasswordFragment.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), DialogPasswordFragment.FRAGMENT_TAG);
            }
        }
    }

    public interface PasswordListener {
        void onSuccess();

        void onError(String message);
    }
}
