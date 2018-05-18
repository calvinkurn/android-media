package com.tokopedia.travelcalendar.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by nabillasabbaha on 15/05/18.
 */
public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private List<String> titleMonths;

    public CalendarPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titleMonths) {
        super(fm);
        this.fragments = fragments;
        this.titleMonths = titleMonths;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleMonths.get(position);
    }
}
