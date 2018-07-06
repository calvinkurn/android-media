package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.presenter.HomepagePresenter;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

public class HomepagePagerAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private List<CatalogsValueEntity> mCatalogs;
    private List<CouponValueEntity> mCoupons;
    private CatalogListAdapter mCatalogsAdapter;
    private CouponListAdapter mCouponsAdapter;
    private HomepagePresenter mPresenter;

    public HomepagePagerAdapter(Context context, HomepagePresenter presenter,
                                List<CatalogsValueEntity> catalogs, List<CouponValueEntity> coupons) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mCatalogs = catalogs;
        this.mCoupons = coupons;
        this.mPresenter = presenter;
        this.mCatalogsAdapter = new CatalogListAdapter(presenter, mCatalogs);
        this.mCouponsAdapter = new CouponListAdapter(presenter, mCoupons);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.tp_layout_promos_list_container, container, false);
        ViewFlipper containerInner = view.findViewById(R.id.container);

        if (position == 0) {
            if (mCatalogs != null && !mCatalogs.isEmpty()) {
                containerInner.setDisplayedChild(0);
                RecyclerView recyclerView = view.findViewById(R.id.recycler_view_promos);
                recyclerView.addItemDecoration(new SpacesItemDecoration(recyclerView.getResources().getDimensionPixelOffset(R.dimen.tp_padding_small)));
                recyclerView.setAdapter(mCatalogsAdapter);
            } else {
                containerInner.setDisplayedChild(1);
                view.findViewById(R.id.text_empty_action).setOnClickListener(view1 -> mPresenter.getView().openWebView(CommonConstant.WebLink.INFO));
            }

            view.findViewById(R.id.text_link_first).setOnClickListener(v -> mPresenter.getView().gotoCatalog());

            view.findViewById(R.id.text_link_second).setOnClickListener(v -> mPresenter.getView().openWebView(CommonConstant.WebLink.INFO));
        } else {
            if (mCoupons != null) {
                containerInner.setDisplayedChild(0);
                RecyclerView recyclerView = view.findViewById(R.id.recycler_view_promos);
                recyclerView.addItemDecoration(new SpacesItemDecoration(recyclerView.getResources().getDimensionPixelOffset(R.dimen.tp_padding_small)));
                recyclerView.setAdapter(mCouponsAdapter);
            }

            view.findViewById(R.id.text_link_first).setOnClickListener(v -> mPresenter.getView().gotoCoupons());

            view.findViewById(R.id.text_link_second).setOnClickListener(v -> mPresenter.getView().openWebView(CommonConstant.WebLink.INFO));
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
