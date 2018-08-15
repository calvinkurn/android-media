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
            tracking();
        }
    }

    private void tracking() {
        CustomSwitchPreference purchase =  (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_purchasing));
        CustomSwitchPreference sales =  (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_sales));
        CustomSwitchPreference message =  (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_pm));
        CustomSwitchPreference discussion =  (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_talk));
        CustomSwitchPreference review =  (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_review));
        CustomSwitchPreference promo =  (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_promo));
        CustomSwitchPreference sellerInfo =  (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_seller_info));
        CustomSwitchPreference groupChat =  (CustomSwitchPreference) findPreference(getString(R.string.pref_receive_group_chat));

        setCustomPreferenceListener(purchase, PURCHASE);
        setCustomPreferenceListener(sales, SALES);
        setCustomPreferenceListener(message, MESSAGE);
        setCustomPreferenceListener(discussion, DISCUSSION);
        setCustomPreferenceListener(review, REVIEW);
        setCustomPreferenceListener(promo, PROMO);
        setCustomPreferenceListener(sellerInfo, SELLER_INFO);
        setCustomPreferenceListener(groupChat, GROUP + " " + CHAT);
    }

    private void setCustomPreferenceListener(CustomSwitchPreference customSwitchPreference, String item) {
        customSwitchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            accountAnalytics.eventClickApplicationSetting(String.format("%s %s", item, NOTIFICATION));
            return true;
        });
    }
}
