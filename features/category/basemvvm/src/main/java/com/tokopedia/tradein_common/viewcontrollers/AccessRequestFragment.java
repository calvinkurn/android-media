package com.tokopedia.tradein_common.viewcontrollers;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.tradein_common.R;
import com.tokopedia.track.TrackApp;
import com.tokopedia.tradein_common.IAccessRequestListener;


public class AccessRequestFragment extends DialogFragment {

    public static final String TAG = "ACCESS REQUEST FRAGMENT";

    private int layoutResId = R.layout.permission_fragment;
    private String buttonAccept;
    private String buttonDeny;
    private String title;
    private String body;
    private boolean fromClickButtons;

    private IAccessRequestListener accessRequestListener;

    public static AccessRequestFragment newInstance() {
        AccessRequestFragment accessRequestFragment = new AccessRequestFragment();
        return accessRequestFragment;
    }

    public void setLayoutResId(int resId) {
        layoutResId = resId;
    }

    public void setBodyText(String bodyText) {
        body = bodyText;
    }

    public void setTitle(String titleText) {
        title = titleText;
    }

    public void setPositiveButton(String positiveButton) {
        buttonAccept = positiveButton;
    }

    public void setNegativeButton(String negativeButton) {
        buttonDeny = negativeButton;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layoutResId);
        TextView buttonAccept = dialog.findViewById(R.id.button_accept);
        TextView buttonDeny = dialog.findViewById(R.id.button_deny);
        AccessRequestClickListener clickListener = new AccessRequestClickListener();
        buttonAccept.setOnClickListener(clickListener);
        buttonDeny.setOnClickListener(clickListener);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        setCustomText(getDialog());
    }

    private void setCustomText(Dialog dialog) {
        TextView buttonAccept = dialog.findViewById(R.id.button_accept);
        if (!TextUtils.isEmpty(this.buttonAccept))
            buttonAccept.setText(this.buttonAccept);
        TextView buttonDeny = dialog.findViewById(R.id.button_deny);
        if (!TextUtils.isEmpty(this.buttonDeny))
            buttonDeny.setText(this.buttonDeny);
        else if (this.buttonDeny == null)
            buttonDeny.setVisibility(View.GONE);
        TextView tvtitle = dialog.findViewById(R.id.tv_title_access);
        if (!TextUtils.isEmpty(this.title))
            tvtitle.setText(this.title);
        TextView tvbody = dialog.findViewById(R.id.tv_description_permission);
        if (!TextUtils.isEmpty(this.body))
            tvbody.setText(this.body);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        accessRequestListener = (IAccessRequestListener) activity;
    }

    public class AccessRequestClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_accept) {
                sendGeneralEvent("setuju");
                accessRequestListener.clickAccept();
                fromClickButtons = true;
                dismiss();
            } else {
                sendGeneralEvent("batal");
                accessRequestListener.clickDeny();
                fromClickButtons = true;
                dismiss();
            }
        }

        private void sendGeneralEvent(String label) {
            TrackApp trackApp = TrackApp.getInstance();
            trackApp.getGTM().sendGeneralEvent("clickPDP",
                    "product detail page",
                    "click - asking permission trade in",
                    label);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!fromClickButtons) {
            if (getActivity() != null)
                getActivity().finish();
        }
    }
}
