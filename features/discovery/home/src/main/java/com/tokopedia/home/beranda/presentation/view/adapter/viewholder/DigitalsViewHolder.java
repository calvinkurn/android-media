package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.viewpager.WrapContentViewPager;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DigitalsViewHolder extends AbstractViewHolder<DigitalsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_digitals;

    private FragmentManager fragmentManager;
    private WrapContentViewPager viewPager;
    private DigitalsHomePagerAdater digitalsHomePagerAdater;

    public DigitalsViewHolder(FragmentManager fragmentManager, View itemView) {
        super(itemView);
        viewPager = itemView.findViewById(R.id.view_pager_widget);
        this.fragmentManager = fragmentManager;
    }


    @Override
    public void bind(DigitalsViewModel element) {
        if (digitalsHomePagerAdater == null) {
            digitalsHomePagerAdater = new DigitalsHomePagerAdater(fragmentManager);
            viewPager.setAdapter(digitalsHomePagerAdater);
        }
        viewPager.setOffscreenPageLimit(1);
        digitalsHomePagerAdater.notifyDataSetChanged();
    }
}