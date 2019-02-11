package com.tokopedia.home.beranda.presentation.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFeedFragment;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedPagerAdapter extends FragmentPagerAdapter {

    private final HomeEggListener homeEggListener;
    private final HomeTabFeedListener homeTabFeedListener;
    private final FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private List<FeedTabModel> feedTabModelList = new ArrayList<>();
    private List<HomeFeedFragment> homeFeedFragmentList;
    private int baseId;

    public HomeFeedPagerAdapter(HomeEggListener homeEggListener,
                                HomeTabFeedListener homeTabFeedListener,
                                FragmentManager fragmentManager,
                                List<FeedTabModel> feedTabModelList) {
        super(fragmentManager);
        this.homeEggListener = homeEggListener;
        this.homeTabFeedListener = homeTabFeedListener;
        this.fragmentManager = fragmentManager;
        updateData(feedTabModelList);
    }

    public void updateData(List<FeedTabModel> feedTabModelList) {
        baseId += this.feedTabModelList.size();
        this.feedTabModelList.clear();
        this.feedTabModelList.addAll(feedTabModelList);
        homeFeedFragmentList = new ArrayList<>();
        for (int i = 0; i < feedTabModelList.size(); i++) {
            HomeFeedFragment homeFeedFragment = HomeFeedFragment.newInstance(
                    i,
                    Integer.parseInt(feedTabModelList.get(i).getId())
            );
            homeFeedFragment.setListener(homeEggListener, homeTabFeedListener);
            homeFeedFragmentList.add(homeFeedFragment);
        }
        notifyDataSetChanged();
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

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    /*
    @Override
    public long getItemId(int position) {
        return baseId + position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        if (fragmentTransaction == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        fragmentTransaction.remove((Fragment)object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        if (fragmentTransaction != null) {
            fragmentTransaction.commitNowAllowingStateLoss();
            fragmentTransaction = null;
        }
    }*/
}
