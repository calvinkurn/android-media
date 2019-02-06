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
    private WrapContentViewPager viewPagerChannel;
    private WrapContentViewPager viewPagerWidget;
    private DigitalsHomePagerAdapter digitalsHomePagerAdapter;
    private DigitalsHomePagerAdapter widgetHomePagerAdapter;
    private TextView titleTextView;
    private TextView seeMoreTextView;
    private boolean isDigitalWidget;
    private static final String APPLINK_DIGITAL_BROWSE_PAGE = "tokopedia://category-explore?type=2";


    public DigitalsViewHolder(HomeCategoryListener listener, FragmentManager fragmentManager, View itemView) {
        super(itemView);
        this.listener = listener;
        viewPagerChannel = itemView.findViewById(R.id.view_pager_channel);
        viewPagerWidget = itemView.findViewById(R.id.view_pager_widget);
        seeMoreTextView = itemView.findViewById(R.id.see_more);
        titleTextView = itemView.findViewById(R.id.title);
        this.fragmentManager = fragmentManager;
    }


    @Override
    public void bind(DigitalsViewModel element) {
        if (isDigitalWidget) {
            viewPagerChannel.setVisibility(View.GONE);
            viewPagerWidget.setVisibility(View.VISIBLE);
            if (widgetHomePagerAdapter == null) {
                widgetHomePagerAdapter = new DigitalsHomePagerAdapter(fragmentManager, DigitalWidgetFragment.Companion.newInstance());
                viewPagerWidget.setAdapter(widgetHomePagerAdapter);
                viewPagerWidget.setOffscreenPageLimit(1);
            }
            viewPagerWidget.setCurrentItem(0);
            widgetHomePagerAdapter.notifyDataSetChanged();
        } else {
            if (digitalsHomePagerAdapter == null) {
                digitalsHomePagerAdapter = new DigitalsHomePagerAdapter(fragmentManager, DigitalChannelFragment.Companion.newInstance(this));
                viewPagerChannel.setAdapter(digitalsHomePagerAdapter);
                viewPagerChannel.setOffscreenPageLimit(1);
            }
            viewPagerWidget.setVisibility(View.GONE);
            viewPagerChannel.setVisibility(View.VISIBLE);
            digitalsHomePagerAdapter.notifyDataSetChanged();
            viewPagerWidget.setCurrentItem(0);
        }


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
        isDigitalWidget = true;
        viewPagerChannel.setVisibility(View.GONE);
        viewPagerWidget.setVisibility(View.VISIBLE);
        if (widgetHomePagerAdapter == null) {
            widgetHomePagerAdapter = new DigitalsHomePagerAdapter(fragmentManager, DigitalWidgetFragment.Companion.newInstance());
            viewPagerWidget.setAdapter(widgetHomePagerAdapter);
        }
    }

    @Override
    public void updateHeaderText(int stringRes) {
        titleTextView.setText(stringRes);
    }
}
