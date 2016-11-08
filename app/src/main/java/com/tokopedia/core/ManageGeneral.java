package com.tokopedia.core;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.GCMListenerService.NotificationListener;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.fragment.AboutFragment;
import com.tokopedia.core.fragment.FragmentSettingPeople;
import com.tokopedia.core.fragment.FragmentSettingShop;
import com.tokopedia.core.fragment.SettingsFragment;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class ManageGeneral extends TkpdActivity implements NotificationListener{

    @Bind(R2.id.pager)
    ViewPager mViewPager;
    @Bind(R2.id.indicator)
    TabLayout indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_manage_general);
        drawer.setDrawerPosition(TkpdState.DrawerPosition.SETTINGS);

        ButterKnife.bind(this);
        String[] content;
        GeneralFragmentAdapter adapter = new GeneralFragmentAdapter(getFragmentManager());
        String shopId = SessionHandler.getShopID(this);
        if (shopId.equals("0") || shopId.length()==0 || shopId==null) {
            content = new String[]{getString(R.string.title_activity_manage_people),
                    getString(R.string.title_activity_manage_general_desc),
                    getString(R.string.title_activity_manage_general_about)};
            adapter.addFragment(FragmentSettingPeople.newInstance());
            adapter.addFragment(new SettingsFragment());
            adapter.addFragment(AboutFragment.newInstance());
        } else {
            content = new String[]{getString(R.string.title_activity_manage_people),
                    getString(R.string.title_activity_manage_shop),
                    getString(R.string.title_activity_manage_general_desc),
                    getString(R.string.title_activity_manage_general_about)};
            adapter.addFragment(FragmentSettingPeople.newInstance());
            adapter.addFragment(FragmentSettingShop.newInstance());
            adapter.addFragment(SettingsFragment.newInstance());
            adapter.addFragment(AboutFragment.newInstance());
        }
        for (String aCONTENT : content) {
            indicator.addTab(indicator.newTab().setText(aCONTENT));
        }

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(mViewPager));
    }

    /**
     * this is just for this class
     */
    private static class GeneralFragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();

        public GeneralFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            this.fragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    public void onGetNotif() {
        if (MainApplication.getNotificationStatus()) {
            drawer.getNotification();
        }
    }

    @Override
    public void onRefreshCart(int status) {

    }

    @Override
    protected void onResume() {
        //[START] this is for set current activity
        MainApplication.setCurrentActivity(this);
        //[END] this is for set current activity
        super.onResume();

        //[END] init the ViewPager
        sendNotifLocalyticsCallback();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        sendNotifLocalyticsCallback();
    }

    private void sendNotifLocalyticsCallback() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)){
                TrackingUtils.eventLocaNotificationCallback(getIntent());
            }
        }
    }

}

