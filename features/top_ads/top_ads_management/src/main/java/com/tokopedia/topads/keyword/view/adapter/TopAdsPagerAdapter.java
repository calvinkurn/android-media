package com.tokopedia.topads.keyword.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.collection.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNegativeListFragment;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordPositiveListFragment;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsPagerAdapter extends FragmentStatePagerAdapter {

    private static final int POSITIVE = 0;
    private static final int NEGATIVE = 1;
    private static final int PAGE_SIZE = 2;
    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<Fragment>();
    private String[] title;

    public TopAdsPagerAdapter(FragmentManager fm, String[] title) {
        super(fm);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITIVE:
                return new TopAdsKeywordPositiveListFragment();
            default:
            case NEGATIVE:
                return new TopAdsKeywordNegativeListFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case POSITIVE:
                return title[POSITIVE];
            default:
            case NEGATIVE:
                return title[NEGATIVE];
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        registeredFragments.put(position, (Fragment) o);
        return o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_SIZE;
    }
}