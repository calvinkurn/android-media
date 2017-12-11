package com.tokopedia.tkpd.home.customview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customView.WrapContentViewPager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.digital.widget.model.category.Category;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.recharge.adapter.RechargeViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 10/2/17.
 */

public class DigitalWidgetView extends BaseCustomView {

    private CardView container;
    private TabLayout tabLayout;
    private WrapContentViewPager viewPager;
    private TextView seeAllProduct;
    private LocalCacheHandler cacheHandler;
    private ActionListener listener;
    private RechargeViewPagerAdapter rechargeViewPagerAdapter;
    private View pulsaPlaceHolder;

    public DigitalWidgetView(@NonNull Context context) {
        super(context);
        init();
    }

    public DigitalWidgetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DigitalWidgetView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    private void init() {
        View view = inflate(getContext(), R.layout.digital_widget_view, this);
        container = (CardView) view.findViewById(R.id.container_widget);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_widget);
        viewPager = (WrapContentViewPager) view.findViewById(R.id.view_pager_widget);
        seeAllProduct = (TextView) view.findViewById(R.id.see_all_product);
        pulsaPlaceHolder = view.findViewById(R.id.pulsa_place_holders);
        cacheHandler = new LocalCacheHandler(
                getContext(), TkpdCache.CACHE_RECHARGE_WIDGET_TAB_SELECTION);

        seeAllProduct.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onClickSeeAllProduct();
                }
            }
        });
    }

    public void renderDataWidget(List<Category> rechargeCategory, boolean useCache, FragmentManager fragmentManager) {
        List<Integer> newRechargePositions = new ArrayList<>();

        if (rechargeCategory.size() == 0) {
            return;
        }

        showDigitalWidget();
        tabLayout.removeAllTabs();
        addChildTablayout(rechargeCategory, newRechargePositions);
        getPositionFlagNewRecharge(newRechargePositions);
        setModeScrollerWidget(rechargeCategory.size());

        if (rechargeViewPagerAdapter == null) {
            rechargeViewPagerAdapter = new RechargeViewPagerAdapter(fragmentManager, rechargeCategory, useCache);
            viewPager.setAdapter(rechargeViewPagerAdapter);
        } else {
            rechargeViewPagerAdapter.addFragments(rechargeCategory);
        }
        addTablayoutListener(rechargeViewPagerAdapter);
//        viewPager.setOffscreenPageLimit(rechargeCategory.size());
        viewPager.setOffscreenPageLimit(1);
        setTabSelected(rechargeCategory.size());
    }

    public void hideDigitalWidget() {
        container.setVisibility(View.GONE);
        ((LinearLayout) tabLayout.getParent()).setVisibility(View.GONE);
    }

    private void showDigitalWidget() {
        container.setVisibility(View.VISIBLE);
        ((LinearLayout) tabLayout.getParent()).setVisibility(View.VISIBLE);
    }

    private void setModeScrollerWidget(int categorySize) {
        if (categorySize == 1)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        else {
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }

    private void addChildTablayout(List<Category> rechargeCategory, List<Integer> newRechargePositions) {
        for (int i = 0; i < rechargeCategory.size(); i++) {
            pulsaPlaceHolder.setVisibility(View.GONE);
            Category category = rechargeCategory.get(i);
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(category.getAttributes().getName());
            tabLayout.addTab(tab);
            if (category.getAttributes().isNew()) {
                newRechargePositions.add(i);

            }
        }
    }

    private void getPositionFlagNewRecharge(List<Integer> newRechargePositions) {
        for (int positionRecharge : newRechargePositions) {
            TextView tv = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0))
                    .getChildAt(positionRecharge)).getChildAt(1));
            if (tv != null) tv.setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    ResourcesCompat.getDrawable(getResources(), R.drawable.recharge_circle, null)
                    , null
            );
        }
    }

    private void addTablayoutListener(final RechargeViewPagerAdapter rechargeViewPagerAdapter) {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
                rechargeViewPagerAdapter.notifyDataSetChanged();
                if (tab.getText() != null) {
                    UnifyTracking.eventHomeRechargeTab(tab.getText().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabSelected(int categorySize) {
        final int positionTab = cacheHandler.getInt(TkpdCache.Key.WIDGET_RECHARGE_TAB_LAST_SELECTED);
        if (positionTab != -1 && positionTab < categorySize) {
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(positionTab);
                }
            }, 300);
            tabLayout.getTabAt(positionTab).select();
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    public int getPosition() {
        return viewPager.getCurrentItem();
    }

    public interface ActionListener {
        void onClickSeeAllProduct();
    }
}