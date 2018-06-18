package com.tokopedia.instantloan.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.tokopedia.instantloan.view.ui.HeightWrappingViewPager;
import com.tokopedia.instantloan.view.ui.InstantLoanItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachinbansal on 6/12/18.
 */

public class InstantLoanPagerAdapter extends FragmentPagerAdapter {

    private List<InstantLoanItem> searchSectionItemList = new ArrayList<>();
    private int mCurrentPosition;

    public InstantLoanPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<InstantLoanItem> searchSectionItemList) {
        this.searchSectionItemList = searchSectionItemList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        mCurrentPosition = position;
        return searchSectionItemList.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return searchSectionItemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return searchSectionItemList.get(position).getTitle();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (!(container instanceof HeightWrappingViewPager)) {
            throw new UnsupportedOperationException("ViewPager is not a WrappingViewPager");
        }

        if (position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            HeightWrappingViewPager pager = (HeightWrappingViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.onPageChanged(fragment.getView());
            }
        }
    }


}
