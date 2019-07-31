package com.tokopedia.home.beranda.presentation.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFeedFragment;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<HomeFeedFragment> registeredFragments = new SparseArrayCompat<>();
    private final HomeEggListener homeEggListener;
    private final HomeTabFeedListener homeTabFeedListener;
    private final TrackingQueue homeTrackingQueue;
    private List<FeedTabModel> feedTabModelList = new ArrayList<>();

    public HomeFeedPagerAdapter(HomeEggListener homeEggListener,
                                HomeTabFeedListener homeTabFeedListener,
                                FragmentManager fragmentManager,
                                List<FeedTabModel> feedTabModelList,
                                TrackingQueue homeTrackingQueue) {
        super(fragmentManager);
        this.homeEggListener = homeEggListener;
        this.homeTabFeedListener = homeTabFeedListener;
        this.homeTrackingQueue = homeTrackingQueue;
        updateData(feedTabModelList);
    }

    public void updateData(List<FeedTabModel> feedTabModelList) {
        this.feedTabModelList.clear();
        this.feedTabModelList.addAll(feedTabModelList);
        registeredFragments.clear();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        HomeFeedFragment homeFeedFragment = HomeFeedFragment.newInstance(
                position,
                Integer.parseInt(feedTabModelList.get(position).getId()),
                feedTabModelList.get(position).getName()
        );
        homeFeedFragment.setListener(homeEggListener, homeTabFeedListener);
        homeFeedFragment.setHomeTrackingQueue(homeTrackingQueue);
        return homeFeedFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        HomeFeedFragment homeFeedFragment = (HomeFeedFragment) o;
        homeFeedFragment.setListener(homeEggListener, homeTabFeedListener);
        homeFeedFragment.setHomeTrackingQueue(homeTrackingQueue);
        registeredFragments.put(position, homeFeedFragment);
        return o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public HomeFeedFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return feedTabModelList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return feedTabModelList.get(position).getName();
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
