package com.tokopedia.home.account.presentation.fragment.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.activity.AppNotificationSettingActivity;
import com.tokopedia.home.account.presentation.activity.EmailNotificationSettingActivity;
import com.tokopedia.home.account.presentation.viewmodel.SettingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationSettingFragment extends BaseGeneralSettingFragment {
    private static final String TAG = NotificationSettingFragment.class.getSimpleName();

    public static Fragment createInstance() {
        return new NotificationSettingFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
    }

    @Override
    protected List<SettingItemViewModel> getSettingItems() {
        List<SettingItemViewModel> settingItems = new ArrayList<>();

        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_APP_NOTIF_ID,
                getString(R.string.title_app_notif_setting)));
        settingItems.add(new SettingItemViewModel(SettingConstant.SETTING_EMAIL_NOTIF_ID,
                getString(R.string.title_email_notif_setting)));

        return settingItems;
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    @Override
    public void onItemClicked(int settingId) {
        switch (settingId){
            case SettingConstant.SETTING_APP_NOTIF_ID:
                startActivity(AppNotificationSettingActivity.createIntent(getActivity()));
                break;
            case SettingConstant.SETTING_EMAIL_NOTIF_ID:
                startActivity(EmailNotificationSettingActivity.createIntent(getActivity()));
                break;
            default:
                break;
        }
    }
}
