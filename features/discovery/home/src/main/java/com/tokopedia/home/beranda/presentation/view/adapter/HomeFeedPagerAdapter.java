package com.tokopedia.home.beranda.presentation.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFeedFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedPagerAdapter extends FragmentPagerAdapter {

    int tabCount;
    List<HomeFeedFragment> homeFeedFragmentList;

    public HomeFeedPagerAdapter(HomeEggListener homeEggListener, FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        homeFeedFragmentList = new ArrayList<>();
        for (int i = 0; i < tabCount; i++) {
            HomeFeedFragment homeFeedFragment = HomeFeedFragment.newInstance(i);
            homeFeedFragment.setListener(homeEggListener);
            homeFeedFragmentList.add(homeFeedFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return homeFeedFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.format("Tab %d", position);
    }
}
