package com.tokopedia.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.explore.view.fragment.ContentExploreFragment;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.adapter.FeedPlusTabAdapter;
import com.tokopedia.feedplus.view.viewmodel.FeedPlusTabItem;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.InboxNotificationListener;
import com.tokopedia.navigation_common.listener.NotificationListener;
import com.tokopedia.searchbar.MainToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 25/07/18.
 */

public class FeedPlusContainerFragment extends BaseDaggerFragment
        implements FragmentListener, NotificationListener, InboxNotificationListener {

    private MainToolbar mainToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FeedPlusFragment feedPlusFragment;
    private ContentExploreFragment contentExploreFragment;

    private int badgeNumber;

    public static FeedPlusContainerFragment newInstance(Bundle bundle) {
        FeedPlusContainerFragment fragment = new FeedPlusContainerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_plus_container, container, false);
        mainToolbar = view.findViewById(R.id.toolbar);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() { }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onScrollToTop() {
        if (feedPlusFragment != null && feedPlusFragment.getUserVisibleHint()) {
            feedPlusFragment.scrollToTop();
        } else if (contentExploreFragment != null && contentExploreFragment.getUserVisibleHint()) {
            contentExploreFragment.scrollToTop();
        }
    }

    @Override
    public void onNotifyBadgeNotification(int number) {
        this.badgeNumber = number;
        if (mainToolbar != null || getActivity() != null) {
            mainToolbar.setNotificationNumber(number);
        }
    }
    
    private void initView() {
        setAdapter();
        if (hasCategoryIdParam()) {
            goToExplore();
        }
        onNotifyBadgeNotification(badgeNumber); // notify badge after toolbar created
    }

    private void setAdapter() {
        List<FeedPlusTabItem> tabItemList = new ArrayList<>();

        tabItemList.add(new FeedPlusTabItem(
                getString(R.string.tab_my_feed),
                getFeedPlusFragment())
        );

        tabItemList.add(new FeedPlusTabItem(
                getString(R.string.tab_explore),
                getContentExploreFragment(getArguments() != null ? getArguments() : new Bundle()))
        );

        FeedPlusTabAdapter adapter = new FeedPlusTabAdapter(getChildFragmentManager());
        adapter.setItemList(tabItemList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private FeedPlusFragment getFeedPlusFragment() {
        if (feedPlusFragment == null) {
            feedPlusFragment = FeedPlusFragment.newInstance();
        }
        return feedPlusFragment;
    }

    private ContentExploreFragment getContentExploreFragment(Bundle bundle) {
        if (contentExploreFragment == null) {
            contentExploreFragment = ContentExploreFragment.newInstance(bundle);
        }
        return contentExploreFragment;
    }

    public void goToExplore() {
        goToExplore(false);
    }

    public void goToExplore(boolean shouldResetCategory) {
        if (shouldGoToExplore()) {
            viewPager.setCurrentItem(tabLayout.getTabCount() - 1);

            if (shouldResetCategory && contentExploreFragment != null) {
                contentExploreFragment.onCategoryReset();
            }
        }
    }

    private boolean hasCategoryIdParam() {
        return getArguments() != null
                && getArguments().getString(ContentExploreFragment.PARAM_CATEGORY_ID) != null;
    }

    private boolean shouldGoToExplore() {
        return viewPager != null
                && tabLayout != null
                && tabLayout.getTabCount() - 1 >= 0;
    }

    @Override
    public void onNotifyBadgeInboxNotification(int number) {
        if (mainToolbar != null) {
            mainToolbar.setInboxNumber(number);
        }
    }
}
