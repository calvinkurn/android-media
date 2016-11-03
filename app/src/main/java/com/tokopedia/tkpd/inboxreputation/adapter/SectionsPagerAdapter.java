package com.tokopedia.tkpd.inboxreputation.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.MainApplication;

import java.util.List;

/**
 * Created by Nisie on 20/01/16.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;


    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
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
        switch (position){
            case 0 : return MainApplication.getAppContext().getString(R.string.title_menu_all);
            case 1 : return MainApplication.getAppContext().getString(R.string.title_my_product);
            case 2 : return MainApplication.getAppContext().getString(R.string.title_my_review);
            default : return MainApplication.getAppContext().getString(R.string.title_menu_all);
        }
    }
}
