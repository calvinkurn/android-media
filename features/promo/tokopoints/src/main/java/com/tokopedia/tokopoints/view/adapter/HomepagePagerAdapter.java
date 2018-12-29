package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.presenter.HomepagePresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;
import java.util.Map;

public class HomepagePagerAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private List<CatalogsValueEntity> mCatalogs;
    private List<CouponValueEntity> mCoupons;
    private CatalogListAdapter mCatalogsAdapter;
    private CouponListAdapter mCouponsAdapter;
    private HomepagePresenter mPresenter;
    private Map<String, String> mEmptyMessages;
    private SwipeToRefresh swipeToRefresh[] = new SwipeToRefresh[2];

    public HomepagePagerAdapter(Context context, HomepagePresenter presenter,
                                List<CatalogsValueEntity> catalogs, List<CouponValueEntity> coupons) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mCatalogs = catalogs;
        this.mCoupons = coupons;
        this.mPresenter = presenter;
        this.mCatalogsAdapter = new CatalogListAdapter(presenter, mCatalogs, true);
        this.mCouponsAdapter = new CouponListAdapter(presenter, mCoupons, true);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_promos_list_container, container, false);
        ViewFlipper containerInner = view.findViewById(R.id.container);
        if (position == 0) {
            if (mCatalogs != null && !mCatalogs.isEmpty()) {
                containerInner.setDisplayedChild(0);
                RecyclerView recyclerView = view.findViewById(R.id.recycler_view_promos);
                swipeToRefresh[position] = view.findViewById(R.id.swipe_refresh_layout);
                swipeToRefresh[position].setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPresenter.getPromos();
                    }
                });
                recyclerView.addItemDecoration(new SpacesItemDecoration(container.getResources().getDimensionPixelOffset(R.dimen.dp_10),
                        container.getResources().getDimensionPixelOffset(R.dimen.dp_14),
                        container.getResources().getDimensionPixelOffset(R.dimen.dp_14)));
                recyclerView.setAdapter(mCatalogsAdapter);
            } else {
                containerInner.setDisplayedChild(1);
                view.findViewById(R.id.text_empty_action).setOnClickListener(view1 -> mPresenter.getView().openWebView(CommonConstant.WebLink.INFO));
            }

            view.findViewById(R.id.text_link_first).setOnClickListener(v -> {
                mPresenter.getView().gotoCatalog();

                AnalyticsTrackerUtil.sendEvent(view.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_LIHAT_SEMUA,
                        "");
            });
        } else {
            if (mCoupons == null || mCoupons.isEmpty()) {
                containerInner.setDisplayedChild(1);
                ((ImageView) view.findViewById(R.id.img_error2)).setImageResource(R.drawable.ic_tp_empty_pages);
                ((TextView) view.findViewById(R.id.text_title_error2)).setText(mEmptyMessages.get(CommonConstant.CouponMapKeys.TITLE));
                ((TextView) view.findViewById(R.id.text_label_error2)).setText(mEmptyMessages.get(CommonConstant.CouponMapKeys.SUB_TITLE));
                view.findViewById(R.id.button_continue).setVisibility(View.VISIBLE);
                view.findViewById(R.id.button_continue).setOnClickListener(view12 -> mPresenter.getView().gotoCatalog());
                view.findViewById(R.id.text_empty_action).setOnClickListener(v ->
                        mPresenter.getView().openWebView(CommonConstant.WebLink.INFO));
            } else {
                containerInner.setDisplayedChild(0);
                RecyclerView recyclerView = view.findViewById(R.id.recycler_view_promos);
                recyclerView.addItemDecoration(new SpacesItemDecoration(container.getResources().getDimensionPixelOffset(R.dimen.dp_14),
                        container.getResources().getDimensionPixelOffset(R.dimen.dp_16),
                        container.getResources().getDimensionPixelOffset(R.dimen.dp_16)));
                recyclerView.setAdapter(mCouponsAdapter);
                swipeToRefresh[position] = view.findViewById(R.id.swipe_refresh_layout);
                swipeToRefresh[position].setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPresenter.getPromos();
                    }
                });

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.findViewById(R.id.view_dummy).getLayoutParams();     //margin for bottom view
                params.height = container.getResources().getDimensionPixelSize(R.dimen.tp_margin_bottom_egg);
            }

            view.findViewById(R.id.text_link_first).setOnClickListener(v -> {
                mPresenter.getView().gotoCoupons();

                AnalyticsTrackerUtil.sendEvent(view.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_LIHAT_SEMUA,
                        "");
            });
        }

        view.setTag(position);
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return CommonConstant.HOMEPAGE_TAB_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public void setEmptyMessages(Map<String, String> emptyMessages) {
        this.mEmptyMessages = emptyMessages;
    }

    public void setRefreshing(boolean refresh) {
        for (SwipeToRefresh swipeToRefrsh : swipeToRefresh) {
            if (swipeToRefrsh != null)
                swipeToRefrsh.setRefreshing(refresh);

        }
    }
}
