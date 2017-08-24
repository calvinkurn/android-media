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
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.var.TkpdCache;

/**
 * @author ricoharisin
 */
public class DialogForceLogout {
    private static final String TAG = DialogForceLogout.class.getSimpleName();
    private static final String IS_DIALOG_SHOWN_STORAGE = "IS_DIALOG_SHOWN_STORAGE";
    private static final String IS_DIALOG_SHOWN = "IS_DIALOG_SHOWN";

    public static void createShow(Context context, @Nullable final ActionListener listener) {
            AlertDialog alertDialog = create(context, listener);
            alertDialog.show();
            setIsDialogShown(context, true);
    }

    public static AlertDialog create(final Context context, @NotNull final ActionListener listener) {
        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.CACHE_API);
        FacebookSdk.sdkInitialize(context);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.title_session_expired);
        dialog.setPositiveButton(context.getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ApiCacheRepositoryImpl.DeleteAllCache();
                        localCacheHandler.clearCache(TkpdCache.Key.VERSION_NAME_IN_CACHE);

                        listener.onDialogClicked();
                        dialog.dismiss();
                        LoginManager.getInstance().logOut();
                        setIsDialogShown(context, false);
                        NotificationModHandler.clearCacheAllNotification(context);

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
