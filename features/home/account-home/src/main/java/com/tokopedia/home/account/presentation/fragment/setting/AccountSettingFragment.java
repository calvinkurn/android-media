package com.tokopedia.home.account.presentation.fragment.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
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
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP;
import static com.tokopedia.home.account.AccountConstants.Analytics.ACCOUNT_BANK;
import static com.tokopedia.home.account.AccountConstants.Analytics.ADDRESS_LIST;
import static com.tokopedia.home.account.AccountConstants.Analytics.PASSWORD;
import static com.tokopedia.home.account.AccountConstants.Analytics.PERSONAL_DATA;

public class AccountSettingFragment extends BaseDaggerFragment implements AccountSetting.View {

    private static final String TAG = AccountSettingFragment.class.getSimpleName();
    private static final int REQUEST_CHANGE_PASSWORD = 123;
    private static int REQUEST_ADD_PASSWORD = 1234;
    private UserSessionInterface userSession;
    private AccountAnalytics accountAnalytics;

    private View personalDataMenu;
    private View addressMenu;
    private View passwordMenu;
    private View pinMenu;
    private View kycSeparator;
    private View kycMenu;
    private View sampaiMenu;
    private View bankAccount;
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
        userSession = new UserSession(getActivity());
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
        pinMenu = view.findViewById(R.id.label_view_pin);
        kycMenu = view.findViewById(R.id.label_view_kyc);
        sampaiMenu = view.findViewById(R.id.label_view_sampai);
        bankAccount = view.findViewById(R.id.label_view_account_bank);
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
    public void showErrorNoConnection() {
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
        pinMenu.setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_PIN));
        kycMenu.setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_ACCOUNT_KYC_ID));
        bankAccount.setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_BANK_ACCOUNT_ID));
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
                    if (userSession.hasPassword()) {
                        intent = RouteManager.getIntent(getActivity(), ApplinkConst
                                .CHANGE_PASSWORD);
                        getActivity().startActivityForResult(intent, REQUEST_CHANGE_PASSWORD);
                    } else {
                        intentToAddPassword();
                    }
                    break;
                case SettingConstant.SETTING_PIN:
                    accountAnalytics.eventClickPinSetting();
                    String PIN_ADDRESS = String.format("%s%s", TokopediaUrl.getInstance().getMOBILEWEB(), "user/pin");
                    RouteManager.route(getActivity(),
                            String.format("%s?url=%s", ApplinkConst.WEBVIEW, PIN_ADDRESS));
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
                case SettingConstant.SETTING_BANK_ACCOUNT_ID:
                    accountAnalytics.eventClickPaymentSetting(ACCOUNT_BANK);
                    gotoAccountBank();
                default:
                    break;
            }
        }
    }

    private void goToKyc() {
        if (getActivity() != null) {
            RouteManager.route(getContext(), ApplinkConst.KYC);
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
        if (getActivity() != null) {
            startActivityForResult(RouteManager.getIntent(getActivity(),
                    ApplinkConstInternalGlobal.ADD_PASSWORD), REQUEST_ADD_PASSWORD);
        }
    }

    private void gotoAccountBank() {
        if (getActivity() != null) {
            if (userSession.hasPassword()) {
                startActivity(RouteManager.getIntent(getActivity(), ApplinkConstInternalGlobal.SETTING_BANK));
            } else {
                showNoPasswordDialog();
            }
        }
    }

    private void onKycMenuClicked() {
        accountAnalytics.eventClickKycSetting();
        if (userSession.hasShop()) {
            goToKyc();
        } else if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
            startActivity(RouteManager.getIntent(getContext(), OPEN_SHOP));
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

    private void showNoPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.error_bank_no_password_title));
        builder.setMessage(getResources().getString(R.string.error_bank_no_password_content));
        builder.setPositiveButton(getResources().getString(R.string.error_no_password_yes), (DialogInterface dialogInterface, int i) -> {
            intentToAddPassword();
            dialogInterface.dismiss();
        });
        builder.setNegativeButton(getResources().getString(R.string.error_no_password_no), (DialogInterface dialogInterface, int i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.colorSheetTitle));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }
}
