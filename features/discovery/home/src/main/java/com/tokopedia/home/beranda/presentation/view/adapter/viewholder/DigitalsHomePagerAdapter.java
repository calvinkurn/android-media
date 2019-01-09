package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.tokopedia.design.viewpager.WrapContentViewPager;
import com.tokopedia.digital.widget.view.fragment.DigitalChannelFragment;
import com.tokopedia.digital.widget.view.fragment.DigitalWidgetFragment;
import com.tokopedia.digital.widget.view.listener.DigitalChannelFragmentInteraction;

public class DigitalsHomePagerAdapter extends FragmentStatePagerAdapter {
    private int currentPosition = -1;
    private static int DIGITAL_WIDGET_COUNT = 1;
    FragmentManager fragmentManager;
    private Fragment digitalFragment;

    public DigitalsHomePagerAdapter(FragmentManager fm, Fragment digitalFragment) {
        super(fm);
        this.fragmentManager = fm;
        this.digitalFragment = digitalFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return digitalFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
//        if (position != currentPosition) {
//            Fragment fragment = (Fragment) object;
//            WrapContentViewPager pager = (WrapContentViewPager) container;
//            if (fragment != null && fragment.getView() != null) {
//                currentPosition = position;
//                pager.measureCurrentView(fragment.getView());
//            }
//        }
    }

    @Override
    public int getCount() {
        return DIGITAL_WIDGET_COUNT;
    }

}
