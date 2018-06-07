package com.tokopedia.digital.product.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.compoundview.DigitalWrapContentViewPager;
import com.tokopedia.digital.product.view.fragment.DigitalPanduanFragment;
import com.tokopedia.digital.product.view.fragment.DigitalPromoFragment;

/**
 * @author by furqan on 07/06/18.
 */

public class PromoPanduanPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private int currentPosition = -1;

    public PromoPanduanPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return context.getString(R.string.promo_tab_title);
            case 1 :
                return context.getString(R.string.panduan_tab_title);
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != currentPosition) {
            Fragment fragment = (Fragment) object;
            DigitalWrapContentViewPager pager = (DigitalWrapContentViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                currentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return DigitalPromoFragment.createInstance();
            case 1 :
                return DigitalPanduanFragment.createInstance();
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
