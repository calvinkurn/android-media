package com.tokopedia.instantloan.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.data.model.response.BannerEntity;

import java.util.List;

public class BannerPagerAdapter extends PagerAdapter {

    private List<BannerEntity> banners;
    LayoutInflater mInflater;
    private BannerClick bannerClick;

    public BannerPagerAdapter(Context context, List<BannerEntity> images, BannerClick bannerClick) {
        this.banners = images;
        mInflater = LayoutInflater.from(context);
        this.bannerClick = bannerClick;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        ImageView banner = (ImageView) mInflater.inflate(R.layout.item_pager_banner, view, false);
        ImageHandler.LoadImage(banner, banners.get(position).getImage());
        view.addView(banner);

        banner.setTag(banners.get(position).getLink());
        banner.setOnClickListener(view1 -> {
            bannerClick.onBannerClick(view1);
        });

        return banner;
    }

    public interface BannerClick {
        public void onBannerClick(View view);
    }
}
