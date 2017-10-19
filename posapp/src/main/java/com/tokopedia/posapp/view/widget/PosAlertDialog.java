package com.tokopedia.posapp.view.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.posapp.R;

/**
 * Created by okasurya on 10/19/17.
 */

public class PosAlertDialog {
    private Context context;
    private String title = "";
    private String message = "";
    private String positiveButtonLabel = "OK";
    private String negativeButtonLabel = "Cancel";
    private boolean cancelable = true;
    private OnClickListener positiveButtonListener;
    private OnClickListener negativeButtonListener;

    private TextView textTitle;
    private TextView textMessage;
    private Button buttonPositive;
    private Button buttonNegative;

    public PosAlertDialog(Context context) {
        this.context = context;
    }

    public PosAlertDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public PosAlertDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public PosAlertDialog setPositiveButton(String label, OnClickListener listener) {
        this.positiveButtonLabel = label;
        this.positiveButtonListener = listener;

        return this;
    }

    public PosAlertDialog setNegativeButton(String label, OnClickListener listener) {
        this.negativeButtonLabel = label;
        this.negativeButtonListener = listener;

        return this;
    }

    public PosAlertDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;

        return this;
    }

    public AlertDialog create() {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(getView(alertDialog));
        alertDialog.setCancelable(cancelable);

        return alertDialog;
    }

    private View getView(final AlertDialog alertDialog) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_pos_dialog, null);
        textTitle = view.findViewById(R.id.text_title);
        textMessage = view.findViewById(R.id.text_message);
        buttonNegative = view.findViewById(R.id.button_negative);
        buttonPositive = view.findViewById(R.id.button_positive);

        textTitle.setText(title);
        textMessage.setText(message);
        buttonPositive.setText(positiveButtonLabel);
        buttonNegative.setText(negativeButtonLabel);

        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positiveButtonListener.onClick(alertDialog);
            }
        });

        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                negativeButtonListener.onClick(alertDialog);
            }
        });

        return view;
    }

    public interface OnClickListener {
        void onClick(DialogInterface dialogInterface);
    }
}
