package com.tokopedia.withdraw.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.changepassword.view.activity.ChangePasswordActivity;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.di.DaggerDoWithdrawComponent;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.view.activity.WithdrawPasswordActivity;
import com.tokopedia.withdraw.view.listener.WithdrawPasswordContract;
import com.tokopedia.withdraw.view.presenter.WithdrawPasswordPresenter;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import javax.inject.Inject;

public class WithdrawPasswordFragment extends BaseDaggerFragment implements WithdrawPasswordContract.View{


    private View withdrawButton;
    private View forgotPassword;
    private EditText passwordView;
    private Snackbar snackBarError;
    private TkpdHintTextInputLayout wrapperPassword;

    @Override
    protected String getScreenName() {
        return null;
    }
    
    @Inject
    WithdrawPasswordPresenter presenter;

    @Override
    protected void initInjector() {
        WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();

        DaggerDoWithdrawComponent.builder().withdrawComponent(withdrawComponent)
                .build().inject(this);

        presenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new WithdrawPasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_confirm_password, container, false);
        withdrawButton = view.findViewById(R.id.withdraw_button);
        passwordView = view.findViewById(R.id.password);
        forgotPassword = view.findViewById(R.id.forgot_pass);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int withdrawal = (int) StringUtils.convertToNumeric(
                        getArguments().getString(WithdrawPasswordActivity.BUNDLE_WITHDRAW)
                        ,false);
                wrapperPassword.setError(null);
                presenter.doWithdraw(withdrawal, (BankAccountViewModel) getArguments()
                        .get(WithdrawPasswordActivity.BUNDLE_BANK)
                        , passwordView.getText().toString());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 65);
            }
        });

        snackBarError = ToasterError.make(getActivity().findViewById(android.R.id.content),
                "", BaseToaster.LENGTH_LONG)
                .setAction(getActivity().getString(R.string.title_close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackBarError.dismiss();
                    }
                });

    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showError(String error) {
        snackBarError.setText(error);
        snackBarError.show();
    }

    @Override
    public void showErrorPassword(String error) {
        wrapperPassword.setError(error);
    }

    @Override
    public void showSuccessWithdraw() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.alert_success_withdraw_title))
                .setMessage(getActivity().getString(R.string.alert_success_withdraw_body))
                .setPositiveButton(getActivity().getString(R.string.alert_success_withdraw_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                })
                .show();
    }
}
