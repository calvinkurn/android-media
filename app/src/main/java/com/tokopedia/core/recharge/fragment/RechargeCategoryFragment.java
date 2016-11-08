package com.tokopedia.core.recharge.fragment;


import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.recharge.adapter.RechargeViewPagerAdapter;
import com.tokopedia.core.recharge.model.category.CategoryData;
import com.tokopedia.core.recharge.presenter.RechargeCategoryPresenterImpl;
import com.tokopedia.core.recharge.view.RechargeCategoryView;
import com.tokopedia.core.recharge.view.widget.WrapContentViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a recharge category
 * @author kulomady on Sep-22-2016
 */
public class RechargeCategoryFragment extends
        Fragment implements RechargeCategoryView {

    @Bind(R2.id.tablayout_recharge)
    TabLayout tabLayoutRecharge;
    @Bind(R2.id.viewpager_pulsa)
    WrapContentViewPager viewpagerRecharge;
    @Bind(R2.id.rechargeWrapperLayout)
    LinearLayout wrapperLayout;

    private TkpdProgressDialog mainprogress;


    public RechargeCategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recharge_main, container, false);
        ButterKnife.bind(this, view);
        initVar();

        return view;
    }

    private void initVar() {
        mainprogress = new TkpdProgressDialog(getActivity(),
                TkpdProgressDialog.MAIN_PROGRESS,
                getActivity().getWindow().getDecorView().getRootView());
        mainprogress.setLoadingViewId(R.id.include_loading);
        RechargeCategoryPresenterImpl rechargeCategoryPresenter = new RechargeCategoryPresenterImpl(getActivity(), this);

        showFetchDataLoading();
        rechargeCategoryPresenter.fecthDataRechargeCategory();
    }


    @Override
    public void renderDataRechargeCategory(CategoryData rechargeCategory) {
        hideFetchDataLoading();
        if (rechargeCategory.getData().size() == 0) {
            return;
        }
        ((LinearLayout) tabLayoutRecharge.getParent()).setVisibility(View.VISIBLE);

        List<Integer> newRechargePositions = new ArrayList<>();

        tabLayoutRecharge.removeAllTabs();
        addChildTablayout(rechargeCategory, newRechargePositions);
        getPositionFlagNewRecharge(newRechargePositions);


        if (rechargeCategory.getData().size() == 1)
            tabLayoutRecharge.setTabMode(TabLayout.MODE_SCROLLABLE);
        else {
            tabLayoutRecharge.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayoutRecharge.setTabMode(TabLayout.MODE_FIXED);
        }

        final RechargeViewPagerAdapter rechargeViewPagerAdapter = new RechargeViewPagerAdapter(
                getChildFragmentManager(), rechargeCategory.getData()
        );
        viewpagerRecharge.setAdapter(rechargeViewPagerAdapter);
        viewpagerRecharge.getAdapter().notifyDataSetChanged();
        LocalCacheHandler handler = new LocalCacheHandler(getActivity(), "tabSelection");
        if (handler.getInt("rechargeSelectedPosition") != null && handler.getInt("rechargeSelectedPosition") == 2) {
            viewpagerRecharge.setCurrentItem(1);
            LocalCacheHandler.clearCache(getActivity(), "tabSelection");
        } else {
            viewpagerRecharge.setCurrentItem(0);
        }
        addTablayoutListener(rechargeViewPagerAdapter);

    }


    @Override
    public void failedRenderDataRechargeCategory() {
        hideFetchDataLoading();
        ((LinearLayout) tabLayoutRecharge.getParent()).setVisibility(View.GONE);
    }

    private void addChildTablayout(CategoryData rechargeCategory, List<Integer> newRechargePositions) {
        for (int i = 0; i < rechargeCategory.getData().size(); i++) {
            com.tokopedia.core.recharge.model.category.Category category = rechargeCategory.getData().get(i);
            TabLayout.Tab tab = tabLayoutRecharge.newTab();
            tab.setText(category.getAttributes().getName());
            tabLayoutRecharge.addTab(tab);
            if (category.getAttributes().getIsNew()) {
                newRechargePositions.add(i);

            }
        }
    }

    private void getPositionFlagNewRecharge(List<Integer> newRechargePositions) {
        for (int positionRecharge : newRechargePositions) {
            TextView tv = (TextView) (((LinearLayout) ((LinearLayout)
                    tabLayoutRecharge.getChildAt(0))
                    .getChildAt(positionRecharge)).getChildAt(1));
            if (tv != null) tv.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ResourcesCompat.getDrawable(getResources(), R.drawable.recharge_circle, null)
                    , null
            );
        }
    }

    private void addTablayoutListener(final RechargeViewPagerAdapter rechargeViewPagerAdapter) {
        viewpagerRecharge.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayoutRecharge)
        );

        tabLayoutRecharge.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpagerRecharge.setCurrentItem(tab.getPosition(), false);
                rechargeViewPagerAdapter.notifyDataSetChanged();
                CommonUtils.dumper("GAv4 " + tab.getPosition() + " " + tab.getText());
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


    public void showFetchDataLoading() {
        wrapperLayout.setVisibility(View.GONE);
        mainprogress.showDialog();
    }

    public void hideFetchDataLoading() {
        wrapperLayout.setVisibility(View.VISIBLE);
        mainprogress.dismiss();
    }

}
