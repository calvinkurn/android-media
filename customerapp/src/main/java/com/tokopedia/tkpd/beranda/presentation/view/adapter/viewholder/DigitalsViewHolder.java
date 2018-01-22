package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.customView.WrapContentViewPager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.widget.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.domain.interactor.DigitalWidgetUseCase;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.home.recharge.adapter.RechargeViewPagerAdapter;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeNetworkInteractorImpl;
import com.tokopedia.tkpd.home.recharge.presenter.RechargeCategoryPresenter;
import com.tokopedia.tkpd.home.recharge.presenter.RechargeCategoryPresenterImpl;
import com.tokopedia.tkpd.home.recharge.view.RechargeCategoryView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DigitalsViewHolder extends AbstractViewHolder<DigitalsViewModel> implements RechargeCategoryView {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_digitals;
    @BindView(R.id.title)
    TextView titleTxt;
    @BindView(R.id.tab_layout_widget)
    TabLayout tabLayout;
    @BindView(R.id.view_pager_widget)
    WrapContentViewPager viewPager;
    @BindView(R.id.pulsa_place_holders)
    View pulsaPlaceHolder;
    @BindView(R.id.container)
    LinearLayout container;

    private LocalCacheHandler cacheHandler;
    private RechargeViewPagerAdapter rechargeViewPagerAdapter;
    private Context context;
    private HomeCategoryListener listener;
    private RechargeCategoryPresenter rechargeCategoryPresenter;
    private FragmentManager fragmentManager;
    private List<Category> mRechargeCategory;

    public DigitalsViewHolder(FragmentManager fragmentManager, View itemView, HomeCategoryListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        this.context = itemView.getContext();
        this.fragmentManager = fragmentManager;
        this.mRechargeCategory = new ArrayList<>();
        cacheHandler = new LocalCacheHandler(context, TkpdCache.CACHE_RECHARGE_WIDGET_TAB_SELECTION);

        DigitalWidgetRepository digitalWidgetRepository = new DigitalWidgetRepository(
                new DigitalEndpointService(), new FavoriteNumberListDataMapper());

        DigitalWidgetUseCase digitalWidgetUseCase = new DigitalWidgetUseCase(context,
                digitalWidgetRepository, new StatusMapper(), new CategoryMapper());

        rechargeCategoryPresenter = new RechargeCategoryPresenterImpl(context, this,
                digitalWidgetUseCase);

//        rechargeCategoryPresenter.fetchDataRechargeCategory();
    }

    @Override
    public void renderDataRechargeCategory(List<Category> rechargeCategory) {
        this.mRechargeCategory = rechargeCategory;
        List<Integer> newRechargePositions = new ArrayList<>();
        if (mRechargeCategory.size() == 0) {
            return;
        }

        showDigitalWidget();
        tabLayout.removeAllTabs();
        addChildTablayout(mRechargeCategory, newRechargePositions);
        getPositionFlagNewRecharge(newRechargePositions);
        setModeScrollerWidget(mRechargeCategory.size());

        if (rechargeViewPagerAdapter == null) {
            rechargeViewPagerAdapter = new RechargeViewPagerAdapter(fragmentManager, mRechargeCategory);
            viewPager.setAdapter(rechargeViewPagerAdapter);
        } else {
            rechargeViewPagerAdapter.addFragments(mRechargeCategory);
        }
        addTablayoutListener(rechargeViewPagerAdapter);
        viewPager.setOffscreenPageLimit(mRechargeCategory.size());
        setTabSelected(mRechargeCategory.size());
        rechargeViewPagerAdapter.notifyDataSetChanged();
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
                    ResourcesCompat.getDrawable(context.getResources(), R.drawable.recharge_circle, null)
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

    @Override
    public void failedRenderDataRechargeCategory() {
        hideDigitalWidget();
    }

    @Override
    public void renderErrorNetwork() {
        listener.showNetworkError(context.getString(R.string.msg_network_error));
    }

    @Override
    public void renderErrorMessage() {

    }

    @Override
    public void bind(DigitalsViewModel element) {
        titleTxt.setText(element.getTitle());
        if (mRechargeCategory.isEmpty()) {
            rechargeCategoryPresenter.fetchDataRechargeCategory();
        }
    }

    @OnClick(R.id.see_more)
    void onSeeMore() {
        listener.onDigitalMoreClicked(getAdapterPosition());
    }

}
