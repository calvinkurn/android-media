package com.tokopedia.tkpd.talkview.method;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.tokopedia.tkpd.R;

/**
 * Created by stevenfredian on 5/12/16.
 */
public class DeleteTalkDialog extends DialogFragment{

    public static final String FRAGMENT_TAG= "DeleteTalk";
    Button okButton;
    DeleteTalkListener listener;
    boolean isDetail;

    public interface DeleteTalkListener{
        void deleteTalk();
    }

    public static DeleteTalkDialog createInstance(DeleteTalkListener listener) {
        DeleteTalkDialog dialog = new DeleteTalkDialog();
        dialog.listener = listener;
        dialog.isDetail = false;
        return dialog;
    }

    public static DeleteTalkDialog createInstance(DeleteTalkListener listener, boolean isDetail) {
        DeleteTalkDialog dialog = new DeleteTalkDialog();
        dialog.listener = listener;
        dialog.isDetail = isDetail;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String temp = getString(R.string.talk);
        if(isDetail) temp = getString(R.string.title_comment);
        return new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.title_dialog_delete).replace("tkpd",temp))
                .setPositiveButton(R.string.title_yes, null)
                .setNegativeButton(R.string.title_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dismiss();
                            }
                        }).create();
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
                listener.deleteTalk();
                okButton.setClickable(false);
                dismiss();
            }
        });
    }
}
