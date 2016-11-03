package com.tokopedia.tkpd.talkview.method;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.tkpd.R;

/**
 * Created by stevenfredian on 5/13/16.
 */
public class ReportTalkDialog extends DialogFragment{

    public static final String FRAGMENT_TAG= "ReportTalk";
    Button okButton;
    ReportTalkListener listener;
    EditText userInput;
    TextInputLayout wrapper;

    public interface ReportTalkListener{
        void reportTalk(String s);
    }

    public static ReportTalkDialog createInstance(ReportTalkListener listener) {
        ReportTalkDialog dialog = new ReportTalkDialog();
        dialog.listener = listener;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View promptsView = LayoutInflater.from(getActivity())
                .inflate(R.layout.prompt_dialog_report, null);
        userInput = (EditText) promptsView.findViewById(R.id.reason);
        wrapper = (TextInputLayout) promptsView.findViewById(R.id.wrapper);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setView(promptsView)
                .setPositiveButton(R.string.action_report, null)
                .setNegativeButton(R.string.title_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dismiss();
                            }
                        });
        return alertDialogBuilder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(validate(userInput.getText().toString())){
                    wrapper.setError(null);
                    listener.reportTalk(userInput.getText().toString());
                    KeyboardHandler.DropKeyboard(userInput.getContext(),userInput);
                    dismiss();
                }else {
                    wrapper.setError(getString(R.string.error_field_required));
                }
            }
        });
    }

    private boolean validate(String s) {
        return s.replaceAll("\\s", "").length() > 0;
    }
}
