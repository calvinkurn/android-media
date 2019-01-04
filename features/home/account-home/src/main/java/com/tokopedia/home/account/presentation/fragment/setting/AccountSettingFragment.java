package com.tokopedia.home.account.presentation.fragment.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.BuildConfig;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.data.model.AccountSettingConfig;
import com.tokopedia.home.account.di.component.AccountSettingComponent;
import com.tokopedia.home.account.di.component.DaggerAccountSettingComponent;
import com.tokopedia.home.account.presentation.AccountSetting;

import javax.inject.Inject;

import static com.tokopedia.home.account.AccountConstants.Analytics.ADDRESS_LIST;
import static com.tokopedia.home.account.AccountConstants.Analytics.KYC;
import static com.tokopedia.home.account.AccountConstants.Analytics.PASSWORD;
import static com.tokopedia.home.account.AccountConstants.Analytics.PERSONAL_DATA;

public class AccountSettingFragment extends BaseDaggerFragment implements AccountSetting.View {

    private static final String TAG = AccountSettingFragment.class.getSimpleName();
    private static final int REQUEST_CHANGE_PASSWORD = 123;
    private static int REQUEST_ADD_PASSWORD = 1234;
    private UserSession userSession;
    private AccountAnalytics accountAnalytics;

    private View personalDataMenu;
    private View addressMenu;
    private View passwordMenu;
    private View kycSeparator;
    private View kycMenu;
    private View sampaiMenu;
    private View mainView;
    private View sampaiSeparator;
    private ProgressBar progressBar;

    @Inject
    AccountSetting.Presenter presenter;

    public static Fragment createInstance() {
        return new AccountSettingFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userSession = ((AbstractionRouter) context.getApplicationContext()).getSession();
        accountAnalytics = new AccountAnalytics(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_setting, container, false);
        personalDataMenu = view.findViewById(R.id.label_view_identity);
        addressMenu = view.findViewById(R.id.label_view_address);
        passwordMenu = view.findViewById(R.id.label_view_password);
        kycMenu = view.findViewById(R.id.label_view_kyc);
        sampaiMenu = view.findViewById(R.id.label_view_sampai);
        sampaiSeparator = view.findViewById(R.id.separator_sampai);
        mainView = view.findViewById(R.id.main_view);
        progressBar = view.findViewById(R.id.progress_bar);
        kycSeparator = view.findViewById(R.id.separator_kyc);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setMenuClickListener(view);
        getMenuToggle();
    }

    @Override
    protected void initInjector() {
        AccountSettingComponent component = DaggerAccountSettingComponent.builder()
                .baseAppComponent(
                        ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()
                ).build();

        component.inject(this);
        presenter.attachView(this);
    }

    private void getMenuToggle() {
        showLoading();
        presenter.getMenuAccountSetting();
    }

    @Override
    public void showLoading() {
        mainView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mainView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        hideLoading();
        if (getView() != null) {
            ToasterError.make(getView(), message)
                    .setAction(getString(R.string.title_try_again), view -> getMenuToggle())
                    .show();
        }
    }

    @Override
    public void showError(Throwable e) {
        showError(ErrorHandler.getErrorMessage(getContext(), e));
    }

    @Override
    public void showErroNoConnection() {
        hideLoading();
        showError(getString(R.string.error_no_internet_connection));
    }

    private void setMenuClickListener(View view) {

        personalDataMenu.setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_ACCOUNT_PERSONAL_DATA_ID));
        addressMenu.setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_ACCOUNT_ADDRESS_ID));
        passwordMenu.setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_ACCOUNT_PASS_ID));
        kycMenu.setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_ACCOUNT_KYC_ID));
        sampaiMenu.setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_ACCOUNT_SAMPAI_ID));
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    public void onItemClicked(int settingId) {
        if (getActivity().getApplication() instanceof AccountHomeRouter) {
            AccountHomeRouter router = (AccountHomeRouter) getActivity().getApplication();
            Intent intent;
            switch (settingId) {
                case SettingConstant.SETTING_ACCOUNT_PERSONAL_DATA_ID:
                    accountAnalytics.eventClickAccountSetting(PERSONAL_DATA);
                    intent = RouteManager.getIntent(getActivity(), ApplinkConst.SETTING_PROFILE);
                    getActivity().startActivityForResult(intent, 0);
                    break;
                case SettingConstant.SETTING_ACCOUNT_PASS_ID:
                    accountAnalytics.eventClickAccountSetting(PASSWORD);
                    if (userSession.isHasPassword()) {
                        intent = RouteManager.getIntent(getActivity(), ApplinkConst
                                .CHANGE_PASSWORD);
                        getActivity().startActivityForResult(intent, REQUEST_CHANGE_PASSWORD);
                    } else {
                        intentToAddPassword();
                    }
                    break;
                case SettingConstant.SETTING_ACCOUNT_ADDRESS_ID:
                    accountAnalytics.eventClickAccountSetting(ADDRESS_LIST);
                    startActivity(router.getManageAddressIntent(getActivity()));
                    break;
                case SettingConstant.SETTING_ACCOUNT_KYC_ID:
                    onKycMenuClicked();
                    break;
                case SettingConstant.SETTING_ACCOUNT_SAMPAI_ID:
                    goToTokopediaCorner();
                default:
                    break;
            }
        }
    }

    private void goToKyc() {
        if (getActivity() != null) {
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.KYC);
            getActivity().startActivity(intent);
        }
    }

    private void goToTokopediaCorner() {
        if (getActivity() != null) {
            accountAnalytics.eventClickTokopediaCornerSetting();
            Intent intent = RouteManager.getIntent(getActivity(), AccountHomeUrl.APPLINK_TOKOPEDIA_CORNER);
            getActivity().startActivity(intent);
        }
    }

    private void intentToAddPassword() {
        if (getActivity().getApplication() instanceof AccountHomeRouter) {
            startActivityForResult(
                    ((AccountHomeRouter) getActivity().getApplicationContext())
                            .getAddPasswordIntent(getActivity()), REQUEST_CHANGE_PASSWORD);

        }
    }


    private void onKycMenuClicked() {
        accountAnalytics.eventClickKycSetting();
        if (userSession.hasShop()) {
            goToKyc();
        } else if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
            startActivity(((AccountHomeRouter) getContext().getApplicationContext()).
                    getIntentCreateShop(getContext()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSuccessGetConfig(AccountSettingConfig accountSettingConfig) {
        personalDataMenu.setVisibility(accountSettingConfig.getAccountSettingConfig()
                .isPeopleDataEnabled() ? View.VISIBLE : View.GONE);
        addressMenu.setVisibility(accountSettingConfig.getAccountSettingConfig()
                .isAddressEnabled() ? View.VISIBLE : View.GONE);
        passwordMenu.setVisibility(accountSettingConfig.getAccountSettingConfig()
                .isPasswordEnabled() ? View.VISIBLE : View.GONE);
        kycSeparator.setVisibility(accountSettingConfig.getAccountSettingConfig()
                .isIdentityEnabled() ? View.VISIBLE : View.GONE);
        kycMenu.setVisibility(accountSettingConfig.getAccountSettingConfig()
                .isIdentityEnabled() ? View.VISIBLE : View.GONE);
        sampaiMenu.setVisibility(accountSettingConfig.getAccountSettingConfig().
                isTokopediaCornerEnabled() ? View.VISIBLE : View.GONE);
        sampaiSeparator.setVisibility(accountSettingConfig.getAccountSettingConfig().
                isTokopediaCornerEnabled() ? View.VISIBLE : View.GONE);
        hideLoading();
    }

    @Override
    public void logUnknownError(Throwable e) {
        try {
            if (!BuildConfig.DEBUG) Crashlytics.logException(e);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }
}
