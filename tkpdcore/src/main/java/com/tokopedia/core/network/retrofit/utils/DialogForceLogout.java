package com.tokopedia.core.network.retrofit.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.Nullable;

import com.drew.lang.annotations.NotNull;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.track.TrackApp;

/**
 * @author ricoharisin
 */
public class DialogForceLogout {
    private static final String TAG = DialogForceLogout.class.getSimpleName();
    private static final String IS_DIALOG_SHOWN_STORAGE = "IS_DIALOG_SHOWN_STORAGE";
    private static final String IS_DIALOG_SHOWN = "IS_DIALOG_SHOWN";

    public static void createShow(Context context, String screenName, @Nullable final ActionListener listener) {
            AlertDialog alertDialog = create(context, listener, screenName);
            alertDialog.show();
            setIsDialogShown(context, true);
    }

    public static AlertDialog create(final Context context,
                                     @NotNull final ActionListener listener, String screenName) {
        FacebookSdk.sdkInitialize(context);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.title_session_expired);
        dialog.setPositiveButton(context.getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogClicked();
                        dialog.dismiss();
                        LoginManager.getInstance().logOut();
                        setIsDialogShown(context, false);
                        NotificationModHandler.clearCacheAllNotification(context);

                        String screen = screenName != null ? screenName : "";
                        TrackApp.getInstance().getGTM().sendGeneralEvent(
                                "clickLogout",
                                "force logout",
                                screen,
                                "get session expired pop up"
                        );

                    }
                });
        dialog.setCancelable(false);
        return dialog.create();
    }

    public static void setIsDialogShown(Context context, Boolean status) {
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
