package com.tokopedia.core.network.retrofit.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.drew.lang.annotations.NotNull;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.gcm.NotificationModHandler;

/**
 * @author ricoharisin
 */
public class DialogHockeyApp {
    private static final String TAG = DialogHockeyApp.class.getSimpleName();
    private static final String IS_DIALOG_SHOWN_STORAGE = "IS_DIALOG_HOCKEYAPP_SHOWN_STORAGE";
    private static final String IS_DIALOG_SHOWN = "IS_DIALOG_HOCKEYAPP_SHOWN";

    public static void createShow(Context context, @Nullable final ActionListener listener) {
            AlertDialog alertDialog = create(context, listener);
            alertDialog.show();
            setIsDialogShown(context, true);
    }

    public static AlertDialog create(final Context context, @NotNull final ActionListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.title_hockeyapp_download);
        dialog.setPositiveButton(context.getString(R.string.title_download),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogClicked();
                        dialog.dismiss();
                        setIsDialogShown(context, false);

                    }
                });
        dialog.setCancelable(false);
        return dialog.create();
    }

    private static void setIsDialogShown(Context context, Boolean status) {
        LocalCacheHandler cache = new LocalCacheHandler(context, IS_DIALOG_SHOWN_STORAGE);
        cache.putBoolean(IS_DIALOG_SHOWN, status);
        cache.applyEditor();
    }

    public static Boolean isDialogShown(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, IS_DIALOG_SHOWN_STORAGE);
        return cache.getBoolean(IS_DIALOG_SHOWN, false);
    }

    public interface ActionListener {
        void onDialogClicked();
    }
}
