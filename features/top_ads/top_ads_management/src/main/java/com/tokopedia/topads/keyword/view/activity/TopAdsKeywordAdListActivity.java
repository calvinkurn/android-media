package com.tokopedia.topads.keyword.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.topads.R;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAdListFragment;
import com.tokopedia.topads.keyword.view.adapter.TopAdsPagerAdapter;
import com.tokopedia.topads.keyword.view.fragment.TopAdsOldKeywordListFragment;
import com.tokopedia.topads.keyword.view.listener.AdListMenuListener;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordAdListActivity extends BaseTabActivity implements HasComponent<TopAdsComponent>,
        TopAdsAdListFragment.OnAdListFragmentListener, TopAdsOldKeywordListFragment.GroupTopAdsListener {

    public static final int OFFSCREEN_PAGE_LIMIT = 2;
    private static final String TAG = TopAdsKeywordAdListActivity.class.getName();
    private static final int DELAY_SHOW_CASE_THREAD = 300;//ms
    boolean isShowingShowCase = false;
    private ShowCaseDialog showCaseDialog;
    private int totalGroupAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalGroupAd = getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_TOTAL_GROUP_ADS, 0);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_keyword_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_keyword_title_negative));
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        String[] titles = {
                getString(R.string.top_ads_keyword_title),
                getString(R.string.top_ads_keyword_title_negative)
        };
        return new TopAdsPagerAdapter(getSupportFragmentManager(), titles);
    }

    @Override
    protected int getPageLimit() {
        return OFFSCREEN_PAGE_LIMIT;
    }

    @Override
    public TopAdsComponent getComponent() {
        return TopAdsComponentInstance.getComponent(getApplication());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_top_ads_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.menu_add) {
            if (getTopAdsBaseKeywordListFragment() != null) {
                getTopAdsBaseKeywordListFragment().onCreateAd();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AdListMenuListener getTopAdsBaseKeywordListFragment() {
        Fragment registeredFragment = getCurrentFragment();
        if (registeredFragment != null && registeredFragment.isVisible()) {
            if (registeredFragment instanceof AdListMenuListener) {
                return ((AdListMenuListener) registeredFragment);
            }
        }
        return null;
    }

    protected Fragment getCurrentFragment() {
        if (getViewPagerAdapter() == null) {
            return null;
        }
        return (Fragment) getViewPagerAdapter().instantiateItem(viewPager, tabLayout.getSelectedTabPosition());
    }

    @Override
    public int getGroupTopAdsSize() {
        return totalGroupAd;
    }

    @Override
    public void setGroupTopAdsSize(int size) {
        totalGroupAd = size;
    }

    @Override
    public void startShowCase() {

    }
}
