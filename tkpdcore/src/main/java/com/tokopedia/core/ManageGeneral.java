package com.tokopedia.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.legacy.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.fragment.AboutFragment;
import com.tokopedia.core.fragment.FragmentSettingPeople;
import com.tokopedia.core.fragment.SettingsFragment;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core2.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Moved to com.tokopedia.home.account.presentation.activity.GeneralSettingActivity
 */
@SuppressLint("ValidFragment")
@Deprecated
public class ManageGeneral extends BaseActivity implements NotificationReceivedListener {
    private static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    public final static int TAB_POSITION_MANAGE_PROFILE = 0;
    public final static int TAB_POSITION_MANAGE_SHOP = 1;
    public final static int TAB_POSITION_MANAGE_APP = 2;
    public final static int TAB_POSITION_ABOUT_US = 3;

    private Toolbar toolbar;
    private ViewPager mViewPager;
    private TabLayout indicator;

    public static Intent getCallingIntent(Activity activity, int position) {
        Intent intent = new Intent(activity, ManageGeneral.class);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, position);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_MANAGE_GENERAL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_general);

        toolbar = findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] content;
        GeneralFragmentAdapter adapter = new GeneralFragmentAdapter(getFragmentManager());
        if (isUserDoesntHaveShop()) {
            content = new String[]{getString(R.string.title_activity_manage_people).toUpperCase(),
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
            Fragment fragmentShopSettings = SellerRouter.getFragmentShopSettings(this);
            adapter.addFragment(fragmentShopSettings);
            adapter.addFragment(SettingsFragment.newInstance());
            adapter.addFragment(AboutFragment.newInstance());
        }
        for (String aCONTENT : content) {
            indicator.addTab(indicator.newTab().setText(aCONTENT));
        }

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(mViewPager));
        actionSelectedTabWhenReserve();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isUserDoesntHaveShop() {
        return !SessionHandler.isUserHasShop(this);
    }

    private void actionSelectedTabWhenReserve() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_STATE_TAB_POSITION)) {
                if (isUserDoesntHaveShop()) {
                    switch (getIntent().getIntExtra(EXTRA_STATE_TAB_POSITION, 0)) {
                        case TAB_POSITION_MANAGE_APP:
                        case TAB_POSITION_ABOUT_US:
                            int position = getIntent().getIntExtra(EXTRA_STATE_TAB_POSITION, 1);
                            mViewPager.setCurrentItem(position - 1, true);
                            break;
                        default:
                            mViewPager.setCurrentItem(getIntent().getIntExtra(EXTRA_STATE_TAB_POSITION, 0), true);
                    }
                } else {
                    mViewPager.setCurrentItem(getIntent().getIntExtra(EXTRA_STATE_TAB_POSITION, 0), true);
                }
            }
        }
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

    }

    @Override
    public void onRefreshCart(int status) {

    }

    @Override
    public void onGetNotif(Bundle data) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

}

