package com.tokopedia.home.account.presentation.fragment.setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.di.component.AccountLogoutComponent;
import com.tokopedia.home.account.di.component.DaggerAccountLogoutComponent;
import com.tokopedia.home.account.presentation.activity.AccountSettingActivity;
import com.tokopedia.home.account.presentation.activity.NotificationSettingActivity;
import com.tokopedia.home.account.presentation.activity.SettingWebViewActivity;
import com.tokopedia.home.account.presentation.activity.StoreSettingActivity;
import com.tokopedia.home.account.presentation.activity.TkpdPaySettingActivity;
import com.tokopedia.home.account.presentation.adapter.setting.GeneralSettingAdapter;
import com.tokopedia.home.account.presentation.listener.LogoutView;
import com.tokopedia.home.account.presentation.presenter.LogoutPresenter;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SwitchSettingItemViewModel;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.home.account.AccountConstants.Analytics.ACCOUNT;
import static com.tokopedia.home.account.AccountConstants.Analytics.APPLICATION_REVIEW;
import static com.tokopedia.home.account.AccountConstants.Analytics.DEVELOPER_OPTIONS;
import static com.tokopedia.home.account.AccountConstants.Analytics.HELP_CENTER;
import static com.tokopedia.home.account.AccountConstants.Analytics.LOGOUT;
import static com.tokopedia.home.account.AccountConstants.Analytics.NOTIFICATION;
import static com.tokopedia.home.account.AccountConstants.Analytics.PAYMENT_METHOD;
import static com.tokopedia.home.account.AccountConstants.Analytics.PRIVACY_POLICY;
import static com.tokopedia.home.account.AccountConstants.Analytics.SETTING;
import static com.tokopedia.home.account.AccountConstants.Analytics.SHAKE_SHAKE;
import static com.tokopedia.home.account.AccountConstants.Analytics.SHOP;
import static com.tokopedia.home.account.AccountConstants.Analytics.TERM_CONDITION;
import static com.tokopedia.home.account.constant.SettingConstant.Url.PATH_CHECKOUT_TEMPLATE;

public class GeneralSettingFragment extends BaseGeneralSettingFragment
        implements LogoutView, GeneralSettingAdapter.SwitchSettingListener {

    @Inject
    LogoutPresenter presenter;
    @Inject
    WalletPref walletPref;

    private View loadingView;
    private View baseSettingView;

    private AccountAnalytics accountAnalytics;

    public static Fragment createInstance() {
        return new GeneralSettingFragment();
    }

    private static final String TAG = GeneralSettingFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AccountLogoutComponent component = DaggerAccountLogoutComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication())
                        .getBaseAppComponent()).build();
        component.inject(this);
        presenter.attachView(this);

        return inflater.inflate(R.layout.fragment_general_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = view.findViewById(R.id.logout_status);
        baseSettingView = view.findViewById(R.id.setting_layout);
        adapter.setSwitchSettingListener(this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        TextView appVersion = view.findViewById(R.id.text_view_app_version);
        appVersion.setText(getString(R.string.application_version_fmt, GlobalConfig.VERSION_NAME));
    }

    @Override
    protected List<SettingItemViewModel> getSettingItems() {
        List<SettingItemViewModel> settingItems = new ArrayList<>();
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_ACCOUNT_ID,
                getString(R.string.title_account_setting), getString(R.string.subtitle_account_setting)));
        if (userSession.hasShop()) {
            settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_SHOP_ID,
                    getString(R.string.account_home_title_shop_setting), getString(R.string.account_home_subtitle_shop_setting)));
        }

        WalletModel walletModel = walletPref.retrieveWallet();
        String walletName = walletModel != null ? walletModel.getText() + ", " : "";
        String settingDescTkpdPay = walletName + getString(R.string.subtitle_tkpd_pay_setting);
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_TKPD_PAY_ID,
                getString(R.string.title_tkpd_pay_setting), settingDescTkpdPay));
        if (getActivity() != null && getActivity().getApplication() instanceof AccountHomeRouter &&
                ((AccountHomeRouter) getActivity().getApplication()).getBooleanRemoteConfig(
                        RemoteConfigKey.CHECKOUT_TEMPLATE_SETTING_TOGGLE, false)) {
            settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_TEMPLATE_ID,
                    getString(R.string.title_tkpd_template_setting), getString(R.string.subtitle_template_setting)));
        }
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_NOTIFICATION_ID,
                getString(R.string.title_notification_setting), getString(R.string.subtitle_notification_setting)));
        settingItems.add(new SwitchSettingItemViewModel(SettingConstant.SETTING_SHAKE_ID,
                getString(R.string.title_shake_setting), getString(R.string.subtitle_shake_setting)));

        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_TNC_ID,
                getString(R.string.title_tnc_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_PRIVACY_ID,
                getString(R.string.title_privacy_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_APP_REVIEW_ID,
                getString(R.string.title_app_review_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_HELP_CENTER_ID,
                getString(R.string.title_help_center_setting)));

        if (GlobalConfig.isAllowDebuggingTools()) {
            settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_DEV_OPTIONS,
                    getString(R.string.title_dev_options)));
        }

        SettingItemViewModel itemOut = new SettingItemViewModel(SettingConstant.SETTING_OUT_ID, getString(R.string.account_home_button_logout));
        itemOut.setIconResource(R.drawable.ic_setting_out);
        itemOut.setHideArrow(true);
        settingItems.add(itemOut);
        return settingItems;
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    @Override
    public void onItemClicked(int settingId) {
        switch (settingId) {
            case SettingConstant.SETTING_ACCOUNT_ID:
                accountAnalytics.eventClickSetting(ACCOUNT);
                startActivity(AccountSettingActivity.createIntent(getActivity()));
                break;
            case SettingConstant.SETTING_SHOP_ID:
                accountAnalytics.eventClickSetting(String.format("%s %s", SHOP, SETTING));
                startActivity(StoreSettingActivity.createIntent(getActivity()));
                break;
            case SettingConstant.SETTING_TKPD_PAY_ID:
                accountAnalytics.eventClickSetting(PAYMENT_METHOD);
                startActivity(TkpdPaySettingActivity.createIntent(getActivity()));
                break;
            case SettingConstant.SETTING_TEMPLATE_ID:
                if (getActivity() != null) {
                    String applink = String.format("%s?url=%s", ApplinkConst.WEBVIEW, TkpdBaseURL.MOBILE_DOMAIN + PATH_CHECKOUT_TEMPLATE);
                    RouteManager.route(getActivity(), applink);
                }
                break;
            case SettingConstant.SETTING_NOTIFICATION_ID:
                accountAnalytics.eventClickSetting(NOTIFICATION);
                startActivity(NotificationSettingActivity.createIntent(getActivity()));
                break;
            case SettingConstant.SETTING_TNC_ID:
                accountAnalytics.eventClickSetting(TERM_CONDITION);
                gotoWebviewActivity(SettingConstant.Url.PATH_TERM_CONDITION, getString(R.string.title_tnc_setting));
                break;
            case SettingConstant.SETTING_PRIVACY_ID:
                accountAnalytics.eventClickSetting(PRIVACY_POLICY);
                gotoWebviewActivity(SettingConstant.Url.PATH_PRIVACY_POLICY, getString(R.string.title_privacy_setting));
                break;
            case SettingConstant.SETTING_APP_REVIEW_ID:
                accountAnalytics.eventClickSetting(APPLICATION_REVIEW);
                goToPlaystore();
                break;
            case SettingConstant.SETTING_HELP_CENTER_ID:
                accountAnalytics.eventClickSetting(HELP_CENTER);
                RouteManager.route(getActivity(), ApplinkConst.CONTACT_US_NATIVE);
                break;
            case SettingConstant.SETTING_OUT_ID:
                accountAnalytics.eventClickSetting(LOGOUT);
                showDialogLogout();
                break;
            case SettingConstant.SETTING_DEV_OPTIONS:
                if (GlobalConfig.isAllowDebuggingTools()) {
                    accountAnalytics.eventClickSetting(DEVELOPER_OPTIONS);
                    RouteManager.route(getActivity(), ApplinkConst.DEVELOPER_OPTIONS);
                }
                break;
            default:
                break;
        }
    }

    private void goToPlaystore() {

        Uri uri = Uri.parse("market://details?id=" + getActivity().getApplication().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            getActivity().startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(SettingConstant.PLAYSTORE_URL
                            + getActivity().getApplication().getPackageName())));
        }
    }

    private void showDialogLogout() {
        Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.account_home_label_logout));
        dialog.setDesc(getString(R.string.account_home_label_logout_confirmation));
        dialog.setBtnOk(getString(R.string.account_home_button_logout));
        dialog.setBtnCancel(getString(R.string.account_home_label_cancel));
        dialog.setOnOkClickListener(v -> {
            dialog.dismiss();
            doLogout();
        });
        dialog.setOnCancelClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void doLogout() {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        showLoading(true);
        presenter.doLogout();
    }

    private void showLoading(boolean isLoading) {
        int shortAnimTime = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        loadingView.animate().setDuration(shortAnimTime)
                .alpha(isLoading ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (loadingView != null) {
                            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                        }
                    }
                });

        baseSettingView.animate().setDuration(shortAnimTime)
                .alpha(isLoading ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (baseSettingView != null) {
                            baseSettingView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                        }
                    }
                });
    }

    private void saveSettingValue(String key, boolean isChecked) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, isChecked);
        editor.apply();
    }

    @Override
    public boolean isSwitchSelected(int settingId) {
        switch (settingId) {
            case SettingConstant.SETTING_SHAKE_ID:
                return isItemSelected(getString(R.string.pref_receive_shake), true);
            default:
                return false;
        }
    }

    @Override
    public void onChangeChecked(int settingId, boolean value) {
        switch (settingId) {
            case SettingConstant.SETTING_SHAKE_ID:
                accountAnalytics.eventClickSetting(SHAKE_SHAKE);
                saveSettingValue(getString(R.string.pref_receive_shake), value);
                break;
            default:
                break;
        }
    }

    private boolean isItemSelected(String key) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return settings.getBoolean(key, false);
    }

    private boolean isItemSelected(String key, boolean defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return settings.getBoolean(key, defaultValue);
    }

    private void gotoWebviewActivity(String path, String title) {
        Intent intent;
        String url = String.format("%s%s", SettingConstant.Url.BASE_MOBILE, path);
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent = SettingWebViewActivity.createIntent(getActivity(), url, title);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
        }
        startActivity(intent);
    }

    @Override
    public void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onErrorLogout(Throwable throwable) {
        showLoading(false);
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), throwable));
    }

    @Override
    public void onSuccessLogout() {
        showLoading(false);
        if (getActivity().getApplication() instanceof AccountHomeRouter) {
            ((AccountHomeRouter) getActivity().getApplication()).doLogoutAccount(getActivity());
        }
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
