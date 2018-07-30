package com.tokopedia.home.account.presentation.fragment.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import com.tokopedia.home.account.R;

public class AppNotificationSettingFragment extends PreferenceFragmentCompat{

    public static Fragment createInstance() {
        return new AppNotificationSettingFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_app_notification_setting);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            setDivider(AppCompatResources.getDrawable(getActivity(), R.drawable.bg_list_separator));
        }
    }
}
