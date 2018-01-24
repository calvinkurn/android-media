package com.tokopedia.abstraction.common.utils.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

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

    public static AlertDialog create(final Context context, final ActionListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.title_session_expired);
        dialog.setPositiveButton(context.getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogClicked();
                        dialog.dismiss();
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
