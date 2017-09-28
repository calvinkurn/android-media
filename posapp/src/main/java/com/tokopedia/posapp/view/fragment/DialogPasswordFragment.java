package com.tokopedia.posapp.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.PosSessionHandler;
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

    private Button okButton;
    private TkpdProgressDialog progressDialog;
    private TextView title;
    private EditText password;

    private PosSessionHandler.PasswordListener listener;

    @Inject
    DialogPasswordPresenter presenter;

    public static DialogPasswordFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        DialogPasswordFragment dialogPasswordFragment = new DialogPasswordFragment();
        dialogPasswordFragment.setArguments(bundle);

        return dialogPasswordFragment;
    }

    public void setListener(PosSessionHandler.PasswordListener listener) {
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
                .setPositiveButton(R.string.password_action_continue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.showDialog();
                        presenter.checkPassword(password.getText().toString());
                    }
                })
                .setNegativeButton(R.string.password_action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dismiss();
                    }
                })
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
        title.setText(getArguments().getString(TITLE, "Masukkan Password"));

        return view;
    }

    @Override
    public void onCheckPasswordSuccess() {
        progressDialog.dismiss();
        listener.onSuccess();
    }

    @Override
    public void onCheckPasswordError(Throwable e) {
        progressDialog.dismiss();
        e.printStackTrace();
    }

    @Override
    public void onCheckPasswordError(String message) {
        progressDialog.dismiss();
        listener.onError(message);
    }
}
