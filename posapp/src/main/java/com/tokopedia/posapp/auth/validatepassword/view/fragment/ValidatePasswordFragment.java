package com.tokopedia.posapp.auth.validatepassword.view.fragment;

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
import com.tokopedia.posapp.auth.di.DaggerAuthComponent;
import com.tokopedia.posapp.auth.validatepassword.di.DaggerValidatePasswordComponent;
import com.tokopedia.posapp.auth.validatepassword.view.ValidatePassword;
import com.tokopedia.posapp.auth.validatepassword.view.presenter.ValidatePasswordPresenter;

import javax.inject.Inject;

/**
 * Created by okasurya on 9/27/17.
 */

public class ValidatePasswordFragment extends DialogFragment implements ValidatePassword.View {

    private  static final String TITLE = "TITLE";
    public static final String FRAGMENT_TAG = "ValidatePasswordFragment";

    private TkpdProgressDialog progressDialog;
    private TextView title;
    private EditText password;
    private Button buttonContinue;
    private Button buttonCancel;

    private PasswordListener listener;

    @Inject
    ValidatePasswordPresenter presenter;

    public static ValidatePasswordFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        ValidatePasswordFragment validatePasswordFragment = new ValidatePasswordFragment();
        validatePasswordFragment.setArguments(bundle);

        return validatePasswordFragment;
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
                        .authComponent(DaggerAuthComponent.builder().appComponent(appComponent).build())
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
        void onSuccess(ValidatePasswordFragment validatePasswordFragment);

        void onError(String message);
    }
}
