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
import com.tokopedia.feedplus.view.listener.FeedPlusContainerListener;
import com.tokopedia.feedplus.view.viewmodel.FeedPlusTabItem;
import com.tokopedia.navigation_common.listener.FragmentListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 25/07/18.
 */

public class FeedPlusContainerFragment extends BaseDaggerFragment
        implements FeedPlusContainerListener, FragmentListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FeedPlusFragment feedPlusFragment;
    private ContentExploreFragment contentExploreFragment;

    public static FeedPlusContainerFragment newInstance() {
        FeedPlusContainerFragment fragment = new FeedPlusContainerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_plus_container, container, false);
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
    protected void initInjector() {

    }

    @Override
    public void showFeedPlus() {
        if (feedPlusFragment == null) {
            feedPlusFragment = FeedPlusFragment.newInstance();
        }
    }

    @Override
    public void showContentExplore() {
        if (contentExploreFragment == null) {
            contentExploreFragment = ContentExploreFragment.newInstance(getArguments());
        }
    }

    @Override
    public void onScrollToTop() {
        if (feedPlusFragment != null && feedPlusFragment.getUserVisibleHint()) {
            feedPlusFragment.scrollToTop();
        } else if (contentExploreFragment != null && contentExploreFragment.getUserVisibleHint()) {
            contentExploreFragment.scrollToTop();
        }
    }

    private void initView() {
        setAdapter();
    }

    private void setAdapter() {
        List<FeedPlusTabItem> tabItemList = new ArrayList<>();
        tabItemList.add(new FeedPlusTabItem(
                getString(R.string.tab_my_feed),
                getFeedPlusFragment())
        );
        tabItemList.add(new FeedPlusTabItem(
                getString(R.string.tab_explore),
                getContentExploreFragment())
        );
        FeedPlusTabAdapter adapter = new FeedPlusTabAdapter(getChildFragmentManager());
        adapter.setItemList(tabItemList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public FeedPlusFragment getFeedPlusFragment() {
        if (feedPlusFragment == null) {
            feedPlusFragment = FeedPlusFragment.newInstance();
        }
        return feedPlusFragment;
    }

    public ContentExploreFragment getContentExploreFragment() {
        if (contentExploreFragment == null) {
            Bundle bundle = new Bundle();
            contentExploreFragment = ContentExploreFragment.newInstance(bundle);
        }
        return contentExploreFragment;
    }
}
