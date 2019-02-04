package com.tokopedia.home.beranda.presentation.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFeedFragment;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedPagerAdapter extends FragmentPagerAdapter {

    private List<FeedTabModel> feedTabModelList = new ArrayList<>();
    private List<HomeFeedFragment> homeFeedFragmentList;

    public HomeFeedPagerAdapter(HomeEggListener homeEggListener,
                                HomeTabFeedListener homeTabFeedListener,
                                FragmentManager fm,
                                List<FeedTabModel> feedTabModelList) {
        super(fm);
        this.feedTabModelList = feedTabModelList;
        homeFeedFragmentList = new ArrayList<>();
        for (int i = 0; i < feedTabModelList.size(); i++) {
            HomeFeedFragment homeFeedFragment = HomeFeedFragment.newInstance(
                    i,
                    Integer.parseInt(feedTabModelList.get(i).getId())
            );
            homeFeedFragment.setListener(homeEggListener, homeTabFeedListener);
            homeFeedFragmentList.add(homeFeedFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return homeFeedFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return feedTabModelList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return feedTabModelList.get(position).getName();
    }

    public List<HomeFeedFragment> getHomeFeedFragmentList() {
        return homeFeedFragmentList;
    }
}
