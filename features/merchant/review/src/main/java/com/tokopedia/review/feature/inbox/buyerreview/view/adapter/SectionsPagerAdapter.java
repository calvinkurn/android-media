package com.tokopedia.review.feature.inbox.buyerreview.view.adapter;

import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * @author by Nisie on 20/01/16.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final TabLayout indicator;
    private List<Fragment> fragmentList;


    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, TabLayout indicator) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentList = fragmentList;
        this.indicator = indicator;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (indicator != null
                && indicator.getTabAt(position) != null
                && !TextUtils.isEmpty(indicator.getTabAt(position).getText()))
            return indicator.getTabAt(position).getText();
        else
            return "";
    }
}
