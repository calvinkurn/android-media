package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.viewpager.WrapContentViewPager;
import com.tokopedia.digital.widget.view.fragment.DigitalChannelFragment;
import com.tokopedia.digital.widget.view.fragment.DigitalWidgetFragment;
import com.tokopedia.digital.widget.view.listener.DigitalChannelFragmentInteraction;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DigitalsViewHolder extends AbstractViewHolder<DigitalsViewModel> implements DigitalChannelFragmentInteraction {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_digitals;

    private FragmentManager fragmentManager;
    private HomeCategoryListener listener;
    private WrapContentViewPager viewPager;
    private DigitalsHomePagerAdapter digitalsHomePagerAdapter;
    private TextView titleTextView;
    private TextView seeMoreTextView;
    private static final String APPLINK_DIGITAL_BROWSE_PAGE = "tokopedia://category-explore?type=2";


    public DigitalsViewHolder(HomeCategoryListener listener, FragmentManager fragmentManager, View itemView) {
        super(itemView);
        this.listener = listener;
        viewPager = itemView.findViewById(R.id.view_pager_widget);
        seeMoreTextView = itemView.findViewById(R.id.see_more);
        titleTextView = itemView.findViewById(R.id.title);
        this.fragmentManager = fragmentManager;
    }


    @Override
    public void bind(DigitalsViewModel element) {
        if (digitalsHomePagerAdapter == null) {
            digitalsHomePagerAdapter = new DigitalsHomePagerAdapter(fragmentManager, DigitalChannelFragment.Companion.newInstance(this));
            viewPager.setAdapter(digitalsHomePagerAdapter);
        }
        viewPager.setOffscreenPageLimit(1);
        digitalsHomePagerAdapter.notifyDataSetChanged();

        seeMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouteManager.route(itemView.getContext(), APPLINK_DIGITAL_BROWSE_PAGE);
                listener.onDigitalMoreClicked(getAdapterPosition());
            }
        });
    }

    @Override
    public void changeToDigitalWidget() {
        digitalsHomePagerAdapter = new DigitalsHomePagerAdapter(fragmentManager, new DigitalWidgetFragment());
        viewPager.setAdapter(digitalsHomePagerAdapter);
        digitalsHomePagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateHeaderText(int stringRes) {
        titleTextView.setText(stringRes);
    }
}
