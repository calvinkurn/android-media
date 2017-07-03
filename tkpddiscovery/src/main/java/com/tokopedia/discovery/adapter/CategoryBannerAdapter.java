package com.tokopedia.discovery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.entity.discovery.BannerOfficialStoreModel;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.BannerModel;
import com.tokopedia.discovery.intermediary.view.adapter.BannerPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alifa on 7/3/17.
 */

public class CategoryBannerAdapter {

    public static CategoryBannerAdapter.CategoryBannerViewHolder onCreateCategoryBanner(Context context, ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_intermediary, parent, false);
        return new CategoryBannerAdapter.CategoryBannerViewHolder(context, inflate);
    }

    public static class CategoryBannerViewModel extends RecyclerViewItem {
        List<BannerModel> bannerModelList;

        private CategoryBannerViewModel() {
            setType(TkpdState.RecyclerView.VIEW_CATEGORY_BANNER);
        }

        public CategoryBannerViewModel(List<BannerModel> bannerModelList) {
            this();
            this.bannerModelList = bannerModelList;
        }
    }

    public static class CategoryBannerViewHolder extends RecyclerView.ViewHolder {

        private static final long SLIDE_DELAY = 8000;

        Context context;
        private ViewPager bannerViewPager;
        private CirclePageIndicator bannerIndicator;
        private BannerPagerAdapter bannerPagerAdapter;
        private Handler bannerHandler;
        private Runnable incrementPage;

        public CategoryBannerViewHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
            bannerViewPager = (ViewPager) itemView.findViewById(R.id.view_pager_intermediary);
            bannerIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator_intermediary);
        }

        public void bind(final CategoryBannerViewModel viewModel) {
            bannerHandler = new Handler();
            incrementPage = runnableIncrement();
            bannerPagerAdapter = new BannerPagerAdapter(context,viewModel.bannerModelList);
            bannerViewPager.setAdapter(bannerPagerAdapter);
            bannerViewPager.addOnPageChangeListener(onBannerChange());
            bannerIndicator.setFillColor(ContextCompat.getColor(context, R.color.tkpd_dark_orange));
            bannerIndicator.setPageColor(ContextCompat.getColor(context, R.color.white));
            bannerIndicator.setViewPager(bannerViewPager);
            bannerPagerAdapter.notifyDataSetChanged();
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) bannerViewPager.getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            bannerViewPager.setLayoutParams(param);
            if (viewModel.bannerModelList.size()==1) bannerIndicator.setVisibility(View.GONE);
            startSlide();
        }

        private Runnable runnableIncrement() {
            return new Runnable() {
                @Override
                public void run() {
                    try {
                        int currentItem = bannerViewPager.getCurrentItem();
                        int maxItems = bannerViewPager.getAdapter().getCount();
                        if (maxItems != 0) {
                            bannerViewPager.setCurrentItem((currentItem + 1) % maxItems, true);
                        } else {
                            bannerViewPager.setCurrentItem(0, true);
                        }
                        bannerHandler.postDelayed(incrementPage, SLIDE_DELAY);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

            };
        }

        private ViewPager.OnPageChangeListener onBannerChange() {
            return new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    stopSlide();
                    startSlide();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };
        }


        private void stopSlide() {
            if (bannerHandler!=null && incrementPage!=null) bannerHandler.removeCallbacks(incrementPage);
        }

        private void startSlide() {
            bannerHandler.removeCallbacks(incrementPage);
            bannerHandler.postDelayed(incrementPage, SLIDE_DELAY);
        }
    }
}
