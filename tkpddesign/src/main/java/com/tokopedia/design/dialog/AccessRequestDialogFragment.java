package com.tokopedia.design.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.tokopedia.design.R;

public class AccessRequestDialogFragment extends DialogFragment {

    public static final String TAG = "ACCESS REQUEST FRAGMENT";

    private int layoutResId = R.layout.permission_fragment;
    private String buttonAccept;
    private String buttonDeny;
    private String title;
    private String body;
    private boolean fromClickButtons;

    private IAccessRequestListener accessRequestListener;

    public static AccessRequestDialogFragment newInstance() {
        return new AccessRequestDialogFragment();
    }

    public void setLayoutResId(int resId) {
        if (resId != 0)
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
        if(activity!=null) {
            super.onAttach(activity);
            accessRequestListener = (IAccessRequestListener) activity;
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

    public void onAttachFragment(Fragment fragment) {
        accessRequestListener = (IAccessRequestListener) fragment;
    }

    public class AccessRequestClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_accept) {
                accessRequestListener.clickAccept();
                fromClickButtons = true;
                dismiss();
            } else {
                accessRequestListener.clickDeny();
                fromClickButtons = true;
                dismiss();
            }
        }
    }
}
