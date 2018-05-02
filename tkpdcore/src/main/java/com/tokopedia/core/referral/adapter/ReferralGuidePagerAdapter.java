package com.tokopedia.core.referral.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.R;

/**
 * Created by ashwanityagi on 02/05/18.
 */

public class ReferralGuidePagerAdapter extends PagerAdapter {

    private int mLayouts[] = new int[]

    {
        R.layout.referral_how_it_works, R.layout.referral_terms
    }

    ;
    private LayoutInflater mInflater;
    private final int HOW_TO_USE_POSITION = 0;
    private final int TERMS_POSITION = 1;

    public ReferralGuidePagerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return mLayouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View layout = mInflater.inflate(mLayouts[position], view, false);
        if (position == HOW_TO_USE_POSITION) {

        } else {

        }
        view.addView(layout);
        return layout;
    }
}
