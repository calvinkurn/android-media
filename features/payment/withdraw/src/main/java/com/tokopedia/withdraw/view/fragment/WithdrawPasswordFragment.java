package com.tokopedia.withdraw.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.WithdrawAnalytics;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.domain.model.BankAccount;
import com.tokopedia.withdraw.view.activity.WithdrawPasswordActivity;
import com.tokopedia.withdraw.view.listener.WithdrawPasswordContract;
import com.tokopedia.withdraw.view.presenter.WithdrawPasswordPresenter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.inject.Inject;

import static com.tokopedia.abstraction.common.utils.GraphqlHelper.streamToString;

public class WithdrawPasswordFragment extends BaseDaggerFragment implements WithdrawPasswordContract.View {

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

    @Inject
    WithdrawAnalytics analytics;

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
                    .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                    .build();
            withdrawComponent.inject(this);
            presenter.attachView(this);
        }

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
        withdrawButton.setOnClickListener(v -> {
            int withdrawal = (int) StringUtils.convertToNumeric(
                    Objects.requireNonNull(getArguments()).getString(WithdrawPasswordActivity.BUNDLE_WITHDRAW)
                    , false);
            boolean isSellerWithdrawal = getArguments().getBoolean(WithdrawPasswordActivity.BUNDLE_IS_SELLER_WITHDRAWAL);
            wrapperPassword.setError(null);
            presenter.doWithdraw(withdrawal, (BankAccount) getArguments()
                            .get(WithdrawPasswordActivity.BUNDLE_BANK)
                    , passwordView.getText().toString(), isSellerWithdrawal);
        });

        forgotPassword.setOnClickListener(v -> {
            Intent intent = RouteManager.getIntent(Objects.requireNonNull(getActivity()), ApplinkConst.CHANGE_PASSWORD);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivityForResult(intent, 65);
            analytics.eventClickForgotPassword();
        });

        snackBarError = ToasterError.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content),
                "", BaseToaster.LENGTH_LONG)
                .setAction(getActivity().getString(R.string.title_close), v -> {
                    analytics.eventClickCloseErrorMessage();
                    snackBarError.dismiss();
                });

    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showError(String error) {
        analytics.eventClickWithdrawalConfirm(error);
        snackBarError.setText(error);
        snackBarError.show();
    }

    @Override
    public void showErrorPassword(String error) {
        analytics.eventClickWithdrawalConfirm(error);
        wrapperPassword.setError(error);
    }

    @Override
    public void showSuccessWithdraw() {
        analytics.eventClickWithdrawalConfirm(getString(R.string.label_analytics_success_withdraw));
        Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public String loadRawString(int resId) {
        InputStream rawResource = getResources().openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }
}
