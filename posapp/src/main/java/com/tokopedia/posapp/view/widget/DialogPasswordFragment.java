package com.tokopedia.posapp.view.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.di.component.DaggerValidatePasswordComponent;
import com.tokopedia.posapp.view.DialogPassword;
import com.tokopedia.posapp.view.presenter.DialogPasswordPresenter;

import javax.inject.Inject;

/**
 * Created by okasurya on 9/27/17.
 */

public class DialogPasswordFragment extends DialogFragment implements DialogPassword.View {

    private  static final String TITLE = "TITLE";
    public static final String FRAGMENT_TAG = "DialogPasswordFragment";

    private TkpdProgressDialog progressDialog;
    private TextView title;
    private EditText password;
    private Button buttonContinue;
    private Button buttonCancel;

    private PasswordListener listener;

    @Inject
    DialogPasswordPresenter presenter;

    public static DialogPasswordFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        DialogPasswordFragment dialogPasswordFragment = new DialogPasswordFragment();
        dialogPasswordFragment.setArguments(bundle);

        return dialogPasswordFragment;
    }

    public void setListener(PasswordListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initInjector();
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        presenter.attachView(this);

        return new AlertDialog.Builder(getActivity())
                .setView(getDialogView())
                .create();
    }

    private void initInjector() {
        AppComponent appComponent = ((MainApplication) getContext().getApplicationContext()).getApplicationComponent();
        DaggerValidatePasswordComponent daggerValidatePasswordComponent =
                (DaggerValidatePasswordComponent) DaggerValidatePasswordComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerValidatePasswordComponent.inject(this);
    }

    private View getDialogView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_password_dialog, null);
        title = view.findViewById(R.id.text_title);
        password = view.findViewById(R.id.edit_password);
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonContinue = view.findViewById(R.id.button_continue);

        title.setText(getArguments().getString(TITLE, getString(R.string.password_dialog_message)));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.showDialog();
                presenter.checkPassword(password.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onCheckPasswordSuccess() {
        listener.onSuccess(this);
    }

    @Override
    public void onCheckPasswordError(Throwable e) {
        progressDialog.dismiss();
        e.printStackTrace();
        SnackbarManager.make(getActivity(), getActivity().getString(R.string.error_password_unknown), Snackbar.LENGTH_LONG).show();
        listener.onError(getActivity().getString(R.string.error_password_unknown));
    }

    @Override
    public void onCheckPasswordError(String message) {
        progressDialog.dismiss();
        SnackbarManager.make(getActivity(), getActivity().getString(R.string.error_password_wrong), Snackbar.LENGTH_LONG).show();
        listener.onError(getActivity().getString(R.string.error_password_wrong));
    }

    @Override
    public void dismiss() {
        if(progressDialog.isProgress()) {
            progressDialog.dismiss();
        }
        super.dismiss();
    }

    public interface PasswordListener {
        void onSuccess(DialogPasswordFragment dialogPasswordFragment);

        void onError(String message);
    }
}
