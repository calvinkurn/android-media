package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.CatalogBanner;
import com.tokopedia.tokopoints.view.presenter.CatalogListingPresenter;

import java.util.List;

public class CatalogBannerPagerAdapter extends PagerAdapter {

    private List<CatalogBanner> mItems;
    private LayoutInflater mInflater;
    private CatalogListingPresenter mPresenter;

    public CatalogBannerPagerAdapter(Context context, List<CatalogBanner> items, CatalogListingPresenter presenter) {
        this.mItems = items;
        mInflater = LayoutInflater.from(context);
        mPresenter = presenter;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        ImageView banner = (ImageView) mInflater.inflate(R.layout.tp_item_catalog_banner, view, false);
        ImageHandler.loadImageFit2(banner.getContext(), banner, mItems.get(position).getImageUrl());
        banner.setOnClickListener(view1 -> {
            if (URLUtil.isValidUrl(mItems.get(position).getRedirectUrl())) {
                mPresenter.getView().openWebView(mItems.get(position).getRedirectUrl());
            } else {
                if (mPresenter.getView().getActivityContext() != null) {
                    RouteManager.route(mPresenter.getView().getActivityContext(), mItems.get(position).getRedirectUrl());
                }
            }
        });
        view.addView(banner);

        return banner;
    }
}
