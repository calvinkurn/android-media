package com.tokopedia.home.account.presentation.fragment.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.BuildConfig;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.data.model.AccountSettingConfig;
import com.tokopedia.home.account.di.component.AccountSettingComponent;
import com.tokopedia.home.account.di.component.DaggerAccountSettingComponent;
import com.tokopedia.home.account.presentation.AccountSetting;
import com.tokopedia.home.account.presentation.util.AccountHomeErrorHandler;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import kotlin.Unit;

import static com.tokopedia.home.account.AccountConstants.Analytics.ACCOUNT_BANK;
import static com.tokopedia.home.account.AccountConstants.Analytics.ADDRESS_LIST;
import static com.tokopedia.home.account.AccountConstants.Analytics.PASSWORD;
import static com.tokopedia.home.account.AccountConstants.Analytics.PERSONAL_DATA;

public class AccountSettingFragment extends BaseDaggerFragment implements AccountSetting.View {

    private static final String TAG = AccountSettingFragment.class.getSimpleName();

    private static final String REMOTE_CONFIG_SETTING_OTP_PUSH_NOTIF = "android_user_setting_otp_push_notif";
    private static final int REQUEST_CHANGE_PASSWORD = 123;
    private static int REQUEST_ADD_PASSWORD = 1234;
    private UserSessionInterface userSession;
    private AccountAnalytics accountAnalytics;
    private Integer PROJECT_ID = 7;

    private View personalDataMenu;
    private View addressMenu;
    private View passwordMenu;
    private View pinMenu;
    private View pushNotifMenu;
    private View kycSeparator;
    private View kycMenu;
    private View sampaiMenu;
    private View bankAccount;
    private View mainView;
    private View sampaiSeparator;
    private ProgressBar progressBar;

    private boolean isFromNewAccount = false;

    private LinearLayout accountSection;

    @Inject
    AccountSetting.Presenter presenter;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new AccountSettingFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        pushNotifMenu = view.findViewById(R.id.label_view_push_notif);
        kycMenu = view.findViewById(R.id.label_view_kyc);
        sampaiMenu = view.findViewById(R.id.label_view_sampai);
        bankAccount = view.findViewById(R.id.label_view_account_bank);
        sampaiSeparator = view.findViewById(R.id.separator_sampai);
        mainView = view.findViewById(R.id.main_view);
        progressBar = view.findViewById(R.id.progress_bar);
        kycSeparator = view.findViewById(R.id.separator_kyc);
        accountSection = view.findViewById(R.id.fragment_account_setting_account_section);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setMenuClickListener(view);
        getMenuToggle();
        showSignInNotif();
        checkForNewAccount();
    }

    private void checkForNewAccount(){
        if(getArguments() != null){
            if(getArguments().containsKey(ApplinkConstInternalGlobal.PARAM_NEW_HOME_ACCOUNT)) {
                isFromNewAccount = true;
                accountSection.setVisibility(View.GONE);
            }
        }
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
            Toaster.INSTANCE.make(getView(), message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(R.string.title_try_again), view -> getMenuToggle());
        }
    }

    @Override
    public void showError(Throwable e, String errorCode) {
        String message = String.format("%s (%s)", ErrorHandler.getErrorMessage(getActivity(), e), errorCode);
        showError(message);

        AccountHomeErrorHandler.logExceptionToCrashlytics(
                e,
                userSession.getUserId(),
                userSession.getEmail(),
                errorCode
        );
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
        pushNotifMenu.setOnClickListener(view1 ->
                onItemClicked(SettingConstant.SETTING_PUSH_NOTIF));
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

        Intent intent;
        switch (settingId) {
            case SettingConstant.SETTING_ACCOUNT_PERSONAL_DATA_ID:
                accountAnalytics.eventClickAccountSetting(PERSONAL_DATA);
                intent = RouteManager.getIntent(getActivity(), ApplinkConst.SETTING_PROFILE);
                getActivity().startActivityForResult(intent, 0);
                break;
            case SettingConstant.SETTING_ACCOUNT_PASS_ID:
                accountAnalytics.eventClickAccountSetting(PASSWORD);
                accountAnalytics.eventClickAccountPassword();
                intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalGlobal.HAS_PASSWORD);
                startActivity(intent);
                break;
            case SettingConstant.SETTING_PIN:
                accountAnalytics.eventClickPinSetting();
                onPinMenuClicked();
                break;
            case SettingConstant.SETTING_PUSH_NOTIF:
                accountAnalytics.eventClickSignInByPushNotifSetting();
                onPushNotifClicked();
                break;
            case SettingConstant.SETTING_ACCOUNT_ADDRESS_ID:
                accountAnalytics.eventClickAccountSetting(ADDRESS_LIST);
                intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalLogistic.MANAGE_ADDRESS);
                startActivity(intent);
                break;
            case SettingConstant.SETTING_ACCOUNT_KYC_ID:
                onKycMenuClicked();
                break;
            case SettingConstant.SETTING_ACCOUNT_SAMPAI_ID:
                goToTokopediaCorner();
                break;
            case SettingConstant.SETTING_BANK_ACCOUNT_ID:
                accountAnalytics.eventClickPaymentSetting(ACCOUNT_BANK);
                gotoAccountBank();
                break;
            default:
                break;

        }
    }

    private void goToKyc() {
        if (getActivity() != null) {
            Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO, String.valueOf(PROJECT_ID));
            startActivity(intent);
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
        goToKyc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSuccessGetConfig(AccountSettingConfig accountSettingConfig) {
        if(isFromNewAccount){
            kycMenu.setVisibility(accountSettingConfig.getAccountSettingConfig()
                    .isIdentityEnabled() ? View.VISIBLE : View.GONE);
            kycSeparator.setVisibility(accountSettingConfig.getAccountSettingConfig()
                    .isIdentityEnabled() ? View.VISIBLE : View.GONE);
        }
        else {
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

            showSignInNotif();
        }
        hideLoading();
    }

    private void showSignInNotif() {
        FirebaseRemoteConfigImpl firebaseRemoteConfig = new FirebaseRemoteConfigImpl(getContext());
        boolean isShowSignInNotif = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_SETTING_OTP_PUSH_NOTIF, false);
        pushNotifMenu.setVisibility(isShowSignInNotif ? View.VISIBLE : View.GONE);
    }

    @Override
    public void logUnknownError(Throwable e) {
        try {
            if (!BuildConfig.DEBUG) FirebaseCrashlytics.getInstance().recordException(e);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    private void onPinMenuClicked(){
        if(userSession.isMsisdnVerified()) {
            goToPinOnboarding();
        }else {
            showAddPhoneDialog();
        }
    }

    private void goToPinOnboarding(){
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true);
        startActivity(intent);
    }

    private void showAddPhoneDialog(){
        if(getActivity() != null) {
            DialogUnify dialog = new DialogUnify(getActivity(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE);
            dialog.setTitle(getString(R.string.account_home_add_phone_title));
            dialog.setDescription(getString(R.string.account_home_add_phone_message));
            dialog.setPrimaryCTAText(getString(R.string.account_home_add_phone_title));
            dialog.setSecondaryCTAText(getString(com.tokopedia.resources.common.R.string.general_label_cancel));

            dialog.setPrimaryCTAClickListener(() -> {
                goToPinOnboarding();
                dialog.dismiss();
                return Unit.INSTANCE;
            });

            dialog.setSecondaryCTAClickListener(() -> {
                dialog.dismiss();
                return Unit.INSTANCE;
            });

            dialog.show();
        }
    }

    private void onPushNotifClicked() {
        RouteManager.route(getContext(), ApplinkConstInternalGlobal.OTP_PUSH_NOTIF_SETTING);
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
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }
}
