package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;

import java.util.List;

/**
 * @author by Nisie on 20/01/16.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final TabLayout indicator;
    private List<Fragment> fragmentList;


    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, TabLayout indicator) {
        super(fm);
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
