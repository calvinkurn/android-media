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
 * Created by stevenfredian on 5/13/16.
 */
public class FollowTalkDialog extends DialogFragment{

    public static final String FRAGMENT_TAG= "FollowTalk";
    Button okButton;
    FollowTalkListener listener;
    int action;
    String act;

    public interface FollowTalkListener{
        void followTalk();
    }

    public static FollowTalkDialog createInstance(FollowTalkListener listener, int isFollow) {
        FollowTalkDialog dialog = new FollowTalkDialog();
        dialog.listener = listener;
        dialog.action = isFollow;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(action==1) act = getString(R.string.title_unfollow);
        else act = getString(R.string.title_follow);

        String message = getString(R.string.title_dialog_follow)
                .replace("action",act);
        message = message.replace("tkpd",getString(R.string.talk));

        return new AlertDialog.Builder(getActivity())
                .setMessage(message)
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
                listener.followTalk();
                okButton.setClickable(false);
                dismiss();
            }
        });
    }
}
