package com.tokopedia.home.account.presentation.fragment.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.presentation.widget.CustomSwitchPreference;

import static com.tokopedia.home.account.AccountConstants.Analytics.*;

public class AppNotificationSettingFragment extends PreferenceFragmentCompat {

    private AccountAnalytics accountAnalytics;
    private CustomSwitchPreference purchase;
    private CustomSwitchPreference sales;
    private CustomSwitchPreference message;
    private CustomSwitchPreference discussion;
    private CustomSwitchPreference review;
    private CustomSwitchPreference promo;
    private CustomSwitchPreference sellerInfo;
    private CustomSwitchPreference groupChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
    }

    public static Fragment createInstance() {
        return new AppNotificationSettingFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_app_notification_setting);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null)
            view.setBackgroundColor(getResources().getColor(android.R.color.white));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            setDivider(AppCompatResources.getDrawable(getActivity(), R.drawable.bg_list_separator));
            initView();
            tracking();
        }
    }

    private void initView() {
        purchase = (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_purchasing));
        sales = (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_sales));
        message = (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_pm));
        discussion = (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_talk));
        review = (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_review));
        promo = (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_promo));
        sellerInfo = (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_seller_info));
        groupChat = (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_group_chat));
    }

    private void tracking() {
        setDefaultClickListener(purchase, PURCHASE);
        setDefaultClickListener(sales, SALES);
        setDefaultClickListener(message, MESSAGE);
        setDefaultClickListener(discussion, DISCUSSION);
        setDefaultClickListener(review, REVIEW);
        setDefaultClickListener(sellerInfo, SELLER_INFO);
        setDefaultClickListener(groupChat, GROUP + " " + CHAT);
        promo.setOnPreferenceChangeListener((preference, newValue) -> {
            accountAnalytics.eventClickApplicationSetting(String.format("%s %s", PROMO, NOTIFICATION));
            if (!preference.getKey().equals("notification_receive_promo")) {
                preference.setSummary(newValue.toString());
            }

            if(newValue instanceof Boolean) {
                accountAnalytics.setPromoPushPreference((Boolean) newValue);
            }
            return true;
        });
    }

    private void setDefaultClickListener(CustomSwitchPreference customSwitchPreference, String item) {
        customSwitchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            accountAnalytics.eventClickApplicationSetting(String.format("%s %s", item, NOTIFICATION));
            return true;
        });
    }
}
