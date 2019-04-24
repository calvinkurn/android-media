package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.customview.WrapContentHeightViewPager;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;


import java.util.HashMap;
import java.util.List;


public class SlidingImageAdapter extends PagerAdapter {


    private List<ProductItem> productItems;
    private Context context;
    private DealsHomePresenter mPresenter;
    private int currentPosition=-1;


    public SlidingImageAdapter(List<ProductItem> productItems, DealsHomePresenter presenter) {
        this.productItems = productItems;
        this.mPresenter = presenter;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return (productItems == null) ? 0 : productItems.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        this.context = view.getContext();
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.deals_banner_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.banner_item);

        ImageHandler.loadImage(context, imageView, productItems.get(position).getImageWeb(), R.color.grey_1100, R.color.grey_1100);

        view.addView(imageLayout, 0);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.sendEventEcommerce(productItems.get(position).getId(), position, productItems.get(position).getDisplayName(), DealsAnalytics.EVENT_PROMO_CLICK
                        , DealsAnalytics.EVENT_CLICK_PROMO_BANNER, DealsAnalytics.LIST_DEALS_TOP_BANNER);
//                mPresenter.onClickBanner();
            }
        });
        return imageLayout;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != currentPosition) {
            ImageView image = (ImageView) object;
            WrapContentHeightViewPager pager = (WrapContentHeightViewPager) container;
            if (image != null ) {
                currentPosition = position;
                pager.measureCurrentView(image);
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);

    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
