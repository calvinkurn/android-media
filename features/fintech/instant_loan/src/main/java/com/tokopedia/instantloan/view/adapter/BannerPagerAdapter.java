package com.tokopedia.instantloan.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.view.model.BannerViewModel;

import java.util.List;

public class BannerPagerAdapter extends PagerAdapter {

    private List<BannerViewModel> banners;
    LayoutInflater mInflater;

    public BannerPagerAdapter(Context context, List<BannerViewModel> images) {
        this.banners = images;
        mInflater = LayoutInflater.from(context);
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
        ImageHandler.LoadImage(banner,banners.get(position).getImage());
        view.addView(banner);

        return banner;
    }
}
