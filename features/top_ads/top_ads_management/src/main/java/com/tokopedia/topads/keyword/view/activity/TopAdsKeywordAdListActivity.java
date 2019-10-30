package com.tokopedia.topads.keyword.view.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.topads.R;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.keyword.view.adapter.TopAdsPagerAdapter;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordAdListFragment;

import java.util.ArrayList;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordAdListActivity extends BaseTabActivity implements HasComponent<TopAdsComponent>,
        TopAdsBaseListFragment.OnAdListFragmentListener, TopAdsKeywordAdListFragment.GroupTopAdsListener {

    public static final int OFFSCREEN_PAGE_LIMIT = 2;
    private static final int TAB_POSITIVE = 0;
    private static final int TAB_NEGATIVE = 1;
    private static final String TAG = TopAdsKeywordAdListActivity.class.getName();
    private static final String DEFAULT_POSITIVE_PATH = "positive";
    private static final int DELAY_SHOW_CASE_THREAD = 300;//ms
    boolean isShowingShowCase = false;
    private ShowCaseDialog showCaseDialog;
    private int totalGroupAd;
    private int tabPosition = TAB_POSITIVE;
    private TopAdsPagerAdapter topAdsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalGroupAd = getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_TOTAL_GROUP_ADS, 0);

        Uri data = getIntent().getData();
        if (data != null) {
            tabPosition = data.getLastPathSegment().equalsIgnoreCase(DEFAULT_POSITIVE_PATH) ? TAB_POSITIVE : TAB_NEGATIVE;
        }
        viewPager.setCurrentItem(tabPosition);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_keyword_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_keyword_title_negative));
    }

    @Override
    protected TopAdsPagerAdapter getViewPagerAdapter() {
        if (topAdsPagerAdapter == null) {
            String[] titles = {
                    getString(R.string.top_ads_keyword_title),
                    getString(R.string.top_ads_keyword_title_negative)
            };
            topAdsPagerAdapter = new TopAdsPagerAdapter(getSupportFragmentManager(), titles);
        }
        return topAdsPagerAdapter;
    }

    @Override
    protected int getPageLimit() {
        return OFFSCREEN_PAGE_LIMIT;
    }

    @Override
    public TopAdsComponent getComponent() {
        return TopAdsComponentInstance.getComponent(getApplication());
    }

    private TopAdsKeywordAdListFragment getTopAdsBaseKeywordListFragment() {
        Fragment registeredFragment = getCurrentFragment();
        if (registeredFragment != null && registeredFragment.isVisible()) {
            if (registeredFragment instanceof TopAdsKeywordAdListFragment) {
                return ((TopAdsKeywordAdListFragment) registeredFragment);
            }
        }
        return null;
    }

    protected Fragment getCurrentFragment() {
        if (topAdsPagerAdapter == null) {
            return null;
        }
        return (Fragment) topAdsPagerAdapter.instantiateItem(viewPager, tabLayout.getSelectedTabPosition());
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

        viewPager.setCurrentItem(0);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                displayShowCase();
            }
        });
    }

    private void displayShowCase() {
        final TopAdsKeywordAdListFragment topAdsKeywordListFragment = (TopAdsKeywordAdListFragment) getCurrentFragment();
        if (topAdsKeywordListFragment == null || topAdsKeywordListFragment.getView() == null) {
            return;
        }
        final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();

        View searchView = topAdsKeywordListFragment.getSearchView();
        if (searchView == null) {
            return;
        }

        if (searchView.getVisibility() == View.VISIBLE) {
            // Pencarian
            showCaseList.add(
                    new ShowCaseObject(
                            searchView,
                            getString(R.string.topads_showcase_keyword_list_title_1),
                            getString(R.string.topads_showcase_keyword_list_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE));
        }

        if (topAdsKeywordListFragment.getFilterView() != null &&
                topAdsKeywordListFragment.getFilterView().getVisibility() == View.VISIBLE) {
            // Filter
            showCaseList.add(
                    new ShowCaseObject(
                            topAdsKeywordListFragment.getFilterView(),
                            getString(R.string.topads_showcase_keyword_list_title_2),
                            getString(R.string.topads_showcase_keyword_list_desc_2),
                            ShowCaseContentPosition.UNDEFINED));
        }

        RecyclerView recyclerView = topAdsKeywordListFragment.getRecyclerView();
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (topAdsKeywordListFragment.getView() == null) {
                    return;
                }
                View dateView = topAdsKeywordListFragment.getDateView();
                if (dateView != null) {
                    dateView.setVisibility(View.VISIBLE);
                    showCaseList.add(
                            new ShowCaseObject(
                                    dateView,
                                    getString(R.string.topads_showcase_keyword_list_title_3),
                                    getString(R.string.topads_showcase_keyword_list_desc_3)));
                }

                View itemView = topAdsKeywordListFragment.getItemRecyclerView();
                if (itemView != null) {
                    showCaseList.add(
                            new ShowCaseObject(
                                    itemView,
                                    getString(R.string.topads_showcase_keyword_list_title_4),
                                    getString(R.string.topads_showcase_keyword_list_desc_4),
                                    ShowCaseContentPosition.UNDEFINED,
                                    Color.WHITE));
                }

                View fabView = topAdsKeywordListFragment.getFab();
                if (fabView != null) {
                    showCaseList.add(
                            new ShowCaseObject(
                                    fabView,
                                    getString(R.string.topads_showcase_keyword_list_title_5),
                                    getString(R.string.topads_showcase_keyword_list_desc_5)));
                }
                showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
                showCaseDialog.show(TopAdsKeywordAdListActivity.this, TAG, showCaseList);
            }
        }, DELAY_SHOW_CASE_THREAD);
    }

}
