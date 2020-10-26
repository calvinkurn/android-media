package com.tokopedia.changephonenumber.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.changephonenumber.ChangePhoneNumberInstance;
import com.tokopedia.changephonenumber.R;
import com.tokopedia.changephonenumber.analytics.ChangePhoneNumberAnalytics;
import com.tokopedia.changephonenumber.di.warning.ChangePhoneNumberWarningComponent;
import com.tokopedia.changephonenumber.di.warning.ChangePhoneNumberWarningModule;
import com.tokopedia.changephonenumber.di.warning.DaggerChangePhoneNumberWarningComponent;
import com.tokopedia.changephonenumber.view.activity.ChangePhoneNumberInputActivity;
import com.tokopedia.changephonenumber.view.adapter.WarningListAdapter;
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.changephonenumber.view.uimodel.WarningUIModel;
import com.tokopedia.otp.verification.domain.data.OtpConstant;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.changephonenumber.view.uimodel.WarningUIModel.ACTION_OTP;
import static com.tokopedia.changephonenumber.view.uimodel.WarningUIModel.BALANCE_THRESHOLD_FOR_WARNING;

/**
 * Created by milhamj on 18/12/17.
 */

public class ChangePhoneNumberWarningFragment extends BaseDaggerFragment
        implements ChangePhoneNumberWarningFragmentListener.View {
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    private static final int REQUEST_CHANGE_PHONE_NUMBER = 1;
    private static final int REQUEST_WITHDRAW_TOKOPEDIA_BALANCE = 99;
    private static final int VERIFY_USER_CHANGE_PHONE_NUMBER = 2;
    public static final String BUNDLE_TOTAL_BALANCE = "total_balance";
    public static final String BUNDLE_TOTAL_BALANCE_INT = "total_balance_int";
    public static final String SCREEN_CHANGE_PHONE_NUMBER_WARNING = "Warning";

    @Inject
    ChangePhoneNumberWarningFragmentListener.Presenter presenter;
    @Inject
    WarningListAdapter adapter;
    @Inject
    UserSession userSession;
    @Inject
    ChangePhoneNumberAnalytics changePhoneNumberAnalytics;
    private View tokopediaBalanceLayout;
    private View tokocashLayout;
    private TextView tokopediaBalanceValue;
    private TextView tokocashValue;
    private RecyclerView warningRecyclerView;
    private TextView nextButton;
    private TextView withdrawButton;
    private WarningUIModel viewModel;
    private String email;
    private String phoneNumber;
    private View mainView;
    private View loadingView;

    public static ChangePhoneNumberWarningFragment newInstance(String email, String phoneNumber) {
        ChangePhoneNumberWarningFragment fragment = new ChangePhoneNumberWarningFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_EMAIL, email);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_phone_number_warning,
                container,
                false);
        tokopediaBalanceLayout = view.findViewById(R.id.tokopedia_balance_layout);
        tokocashLayout = view.findViewById(R.id.tokocash_layout);
        tokopediaBalanceValue = view.findViewById(R.id.tokopedia_balance_value);
        tokocashValue = view.findViewById(R.id.tokocash_value);
        warningRecyclerView = view.findViewById(R.id.warning_rv);
        nextButton = view.findViewById(R.id.next_button);
        withdrawButton = view.findViewById(R.id.withdraw_button);
        mainView = view.findViewById(R.id.main_view);
        loadingView = view.findViewById(R.id.loading_view);
        warningRecyclerView.setFocusable(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewListener();
        presenter.initView();
    }

    private void initVar() {
        email = getArguments().getString(PARAM_EMAIL);
        phoneNumber = getArguments().getString(PARAM_PHONE_NUMBER);
    }

    private void setViewListener() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextActivity();
            }
        });

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    boolean isSeller = userSession.hasShop() || userSession.isAffiliate();
                    Intent intent = RouteManager.getIntent(getActivity(),
                            ApplinkConstInternalGlobal.WITHDRAW);
                    Bundle bundle = new Bundle();
                    bundle.putString(BUNDLE_TOTAL_BALANCE,
                            viewModel.getTokopediaBalance());
                    bundle.putString(BUNDLE_TOTAL_BALANCE_INT,
                            viewModel.getTokopediaBalance().replaceAll("[^\\d]", ""));
                    intent.putExtras(bundle);
                    changePhoneNumberAnalytics.getEventWarningPageClickOnWithdraw();
                    startActivityForResult(intent, REQUEST_WITHDRAW_TOKOPEDIA_BALANCE);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return SCREEN_CHANGE_PHONE_NUMBER_WARNING;
    }

    @Override
    protected void initInjector() {
        ChangePhoneNumberWarningComponent changePhoneNumberWarningComponent =
                DaggerChangePhoneNumberWarningComponent.builder()
                        .changePhoneNumberComponent(ChangePhoneNumberInstance
                                .getChangePhoneNumberComponent(this.getActivity().getApplication()))
                        .changePhoneNumberWarningModule(new ChangePhoneNumberWarningModule())
                        .build();
        changePhoneNumberWarningComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showLoading() {
        mainView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        loadingView.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetWarningSuccess(WarningUIModel warningUIModel) {
        this.viewModel = warningUIModel;
        presenter.validateOtpStatus(getUserId());
    }

    @Override
    public void onGetWarningError(String message) {
        showWarningEmptyState(message);
        dismissLoading();
    }

    @Override
    public void onGetValidateOtpStatusSuccess(Boolean isValid) {
        if (isValid) {
            startActivityForResult(
                    ChangePhoneNumberInputActivity.newInstance(
                            getContext(),
                            phoneNumber,
                            email,
                            viewModel.getWarningList() != null ?
                                    new ArrayList<>(viewModel.getWarningList()) :
                                    null
                    ),
                    REQUEST_CHANGE_PHONE_NUMBER);
        } else {
            loadWarningPage();
        }
    }

    @Override
    public void onGetValidateOtpStatusError() {
        loadWarningPage();
    }

    private void loadWarningPage() {
        if (viewModel.getTokocashNumber() <= 0
                && viewModel.getTokopediaBalanceNumber() < BALANCE_THRESHOLD_FOR_WARNING) {
            goToNextActivity();
        } else {
            loadDataToView();
            showOrHideWithdrawButton();
            dismissLoading();
            setEventTracking();
        }
    }

    private void showOrHideWithdrawButton() {
        if (viewModel.getAction().equalsIgnoreCase(ACTION_OTP)
                && viewModel.getTokopediaBalanceNumber() >= BALANCE_THRESHOLD_FOR_WARNING
                && viewModel.isHasBankAccount()) {
            withdrawButton.setVisibility(View.VISIBLE);
        } else {
            withdrawButton.setVisibility(View.GONE);
        }
    }

    private void loadDataToView() {
        if (viewModel != null) {
            if (viewModel.getTokopediaBalanceNumber() < BALANCE_THRESHOLD_FOR_WARNING) {
                tokopediaBalanceLayout.setVisibility(View.GONE);
            } else {
                tokopediaBalanceLayout.setVisibility(View.VISIBLE);
                tokopediaBalanceValue.setText(viewModel.getTokopediaBalance());
            }

            if (viewModel.getTokocashNumber() <= 0) {
                tokocashLayout.setVisibility(View.GONE);
            } else {
                tokocashLayout.setVisibility(View.VISIBLE);
                tokocashValue.setText(viewModel.getTokocash());
            }

            populateRecyclerView();
        }
    }

    private void setEventTracking() {
        if (viewModel != null) {
            if (viewModel.getTokopediaBalanceNumber() < BALANCE_THRESHOLD_FOR_WARNING
                    && viewModel.getTokocashNumber() > 0) {
                changePhoneNumberAnalytics.getEventViewWarningMessageTokocash();
            } else if (viewModel.getTokopediaBalanceNumber() >= BALANCE_THRESHOLD_FOR_WARNING
                    && viewModel.getTokocashNumber() <= 0) {
                changePhoneNumberAnalytics.getEventViewWarningMessageSaldo();
            } else if (viewModel.getTokopediaBalanceNumber() >= BALANCE_THRESHOLD_FOR_WARNING
                    && viewModel.getTokocashNumber() > 0) {
                changePhoneNumberAnalytics.getEventViewWarningMessageBoth();
            }
        }
    }

    private void goToNextActivity() {
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.COTP);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OtpConstant.OtpType.VERIFY_USER_CHANGE_PHONE_NUMBER);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true);
        startActivityForResult(intent, VERIFY_USER_CHANGE_PHONE_NUMBER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case VERIFY_USER_CHANGE_PHONE_NUMBER:
                if (resultCode == Activity.RESULT_OK) {
                    startActivityForResult(
                            ChangePhoneNumberInputActivity.newInstance(
                                    getContext(),
                                    phoneNumber,
                                    email,
                                    viewModel != null && viewModel.getWarningList() != null ?
                                            new ArrayList<>(viewModel.getWarningList()) : null
                            ),
                            REQUEST_CHANGE_PHONE_NUMBER);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    getActivity().finish();
                }
                break;
            case REQUEST_CHANGE_PHONE_NUMBER:
                getActivity().setResult(resultCode);
                getActivity().finish();
                break;
            case REQUEST_WITHDRAW_TOKOPEDIA_BALANCE:
                break;
        }
    }

    private void populateRecyclerView() {
        if (viewModel != null) {
            if (viewModel.getWarningList() != null && viewModel.getWarningList().size() > 0) {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL, false);
                warningRecyclerView.setLayoutManager(mLayoutManager);
                adapter.addData(viewModel.getWarningList());
                warningRecyclerView.setAdapter(adapter);
            }
        }
    }

    private void showWarningEmptyState(String message) {
        if (message == null || message.isEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getWarning();
                        }
                    });
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    message,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getWarning();
                        }
                    });
        }
    }

    @Override
    public void goToOvoWebView(String url) {
        RouteManager.route(getContext(),
                String.format("%s?url=%s", ApplinkConst.WEBVIEW, url));
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    public Integer getUserId() {
        return Integer.parseInt(userSession.getUserId());
    }
}
