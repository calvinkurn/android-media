package com.tokopedia.home.account.presentation.fragment.setting;

import android.app.Application;
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

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.AccountHomeRouter;
import com.tokopedia.home.account.presentation.activity.AccountSettingActivity;
import com.tokopedia.home.account.presentation.activity.NotificationSettingActivity;
import com.tokopedia.home.account.presentation.activity.SettingWebViewActivity;
import com.tokopedia.home.account.presentation.activity.StoreSettingActivity;
import com.tokopedia.home.account.presentation.activity.TkpdPaySettingActivity;
import com.tokopedia.home.account.presentation.adapter.setting.GeneralSettingAdapter;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SwitchSettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class GeneralSettingFragment extends BaseGeneralSettingFragment
        implements GeneralSettingAdapter.SwitchSettingListener {
    public static Fragment createInstance() {
        return new GeneralSettingFragment();
    }

    private static final String TAG = GeneralSettingFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_general_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter.setSwitchSettingListener(this);
        recyclerView.setNestedScrollingEnabled(false);
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
                    getString(R.string.title_shop_setting), getString(R.string.subtitle_shop_setting)));
        }
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_TKPD_PAY_ID,
                getString(R.string.title_tkpd_pay_setting), getString(R.string.subtitle_tkpd_pay_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_NOTIFICATION_ID,
                getString(R.string.title_notification_setting), getString(R.string.subtitle_notification_setting)));
        settingItems.add(new SwitchSettingItemViewModel(SettingConstant.SETTING_SHAKE_ID,
                        getString(R.string.title_shake_setting), getString(R.string.subtitle_shake_setting)));

        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_TNC_ID,
                getString(R.string.title_tnc_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_PRIVACY_ID,
                getString(R.string.title_privacy_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_HELP_CENTER_ID,
                getString(R.string.title_help_center_setting)));

        if(GlobalConfig.isAllowDebuggingTools()) {
            settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_DEV_OPTIONS,
                    getString(R.string.title_dev_options)));
        }

        SettingItemViewModel itemOut = new SettingItemViewModel(SettingConstant.SETTING_OUT_ID, getString(R.string.logout));
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
        switch (settingId){
            case SettingConstant.SETTING_ACCOUNT_ID:
                startActivity(AccountSettingActivity.createIntent(getActivity()));
                break;
            case SettingConstant.SETTING_SHOP_ID:
                startActivity(StoreSettingActivity.createIntent(getActivity()));
                break;
            case SettingConstant.SETTING_TKPD_PAY_ID:
                startActivity(TkpdPaySettingActivity.createIntent(getActivity()));
                break;
            case SettingConstant.SETTING_NOTIFICATION_ID:
                startActivity(NotificationSettingActivity.createIntent(getActivity()));
                break;
            case SettingConstant.SETTING_TNC_ID:
                gotoWebviewActivity(SettingConstant.Url.PATH_TERM_CONDITION, getString(R.string.title_tnc_setting));
                break;
            case SettingConstant.SETTING_PRIVACY_ID:
                gotoWebviewActivity(SettingConstant.Url.PATH_PRIVACY_POLICY, getString(R.string.title_privacy_setting));
                break;
            case SettingConstant.SETTING_HELP_CENTER_ID:
                Application application = getActivity().getApplication();
                if (application instanceof AccountHomeRouter){
                    ((AccountHomeRouter) application).goToHelpCenter(getActivity());
                }
                break;
            case SettingConstant.SETTING_OUT_ID:
                application = getActivity().getApplication();
                if (application instanceof AccountHomeRouter){
                    ((AccountHomeRouter) application).doLogoutAccount(getActivity());
                }
                break;
            case SettingConstant.SETTING_DEV_OPTIONS:
                if(GlobalConfig.isAllowDebuggingTools()) {
                    RouteManager.route(getActivity(), ApplinkConst.DEVELOPER_OPTIONS);
                }
                break;
            default: break;
        }
    }

    private void saveSettingValue(String key, boolean isChecked) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, isChecked);
        editor.apply();
    }

    @Override
    public boolean isSwitchSelected(int settingId) {
        switch (settingId){
            case SettingConstant.SETTING_SHAKE_ID:
                return isItemSelected(getString(R.string.pref_receive_shake));
            default:
                return false;
        }
    }

    @Override
    public void onChangeChecked(int settingId, boolean value) {
        switch (settingId){
            case SettingConstant.SETTING_SHAKE_ID:
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

    private void gotoWebviewActivity(String path, String title) {
        Intent intent;
        String url = String.format("%s%s", SettingConstant.Url.BASE_MOBILE, path);
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent = SettingWebViewActivity.createIntent(getActivity(), url,title);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
        }
        startActivity(intent);
    }
}
