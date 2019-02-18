package com.tokopedia.home.beranda.presentation.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.banner.BannerPagerAdapter;
import com.tokopedia.design.banner.BannerView;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeBannerPagerAdapter;
import com.tokopedia.home.R;

import java.util.ArrayList;

public class HomeBannerView extends BannerView {

    ImageView img_banner_background;

    public HomeBannerView(@NonNull Context context) {
        super(context);
    }

    public HomeBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        View view = View.inflate(getContext(), R.layout.widget_banner_home, this);
        bannerRecyclerView = view.findViewById(R.id.viewpager_banner_category);
        bannerIndicator = view.findViewById(R.id.indicator_banner_container);
        bannerSeeAll = view.findViewById(R.id.promo_link);
        img_banner_background = view.findViewById(R.id.img_banner_background);
        indicatorItems = new ArrayList<>();
        impressionStatusList = new ArrayList<>();
        promoImageUrls = new ArrayList<>();
    }

    @Override
    public void buildView() {
        super.buildView();
        bannerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = currentPosition > 0 ? currentPosition : 0;
                String url = promoImageUrls.get(position);
                ImageHandler.loadImageBlur(
                        getContext(),
                        img_banner_background,
                        url
                );
            }
        });
    }

    @Override
    protected BannerPagerAdapter getBannerAdapter() {
        return new HomeBannerPagerAdapter(promoImageUrls, onPromoClickListener);
    }
}
