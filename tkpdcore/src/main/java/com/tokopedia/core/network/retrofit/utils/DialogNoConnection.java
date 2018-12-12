package com.tokopedia.core.network.retrofit.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;

/**
 * Created by Angga.Prasetiyo on 23/02/2016.
 */
public class DialogNoConnection {
    private static final String TAG = DialogNoConnection.class.getSimpleName();

    public static void createShow(Context context, @Nullable final ActionListener listener) {
        AlertDialog alertDialog = create(context, listener);
        alertDialog.show();
    }

    public static AlertDialog create(Context context, @Nullable final ActionListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        String noConnection = context.getResources().getString(R.string.msg_no_connection) + ".\n"
                + context.getResources().getString(R.string.error_no_connection2) + ".";
        msg.setText(noConnection);
        dialog.setView(promptsView);
        if (listener != null) {
            dialog.setPositiveButton(context.getString(R.string.title_try_again),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onRetryClicked();
                            dialog.dismiss();
                        }
                    });
        } else {
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        return dialog.create();
    }

    public static AlertDialog create(Context context, String Message, @Nullable final ActionListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        String noConnection = Message;
        msg.setText(noConnection);
        dialog.setView(promptsView);
        if (listener != null) {
            dialog.setPositiveButton(context.getString(R.string.title_try_again),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onRetryClicked();
                            dialog.dismiss();
                        }
                    });
        } else {
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        return dialog.create();
    }

    public interface ActionListener {
        void onRetryClicked();
    }
}
