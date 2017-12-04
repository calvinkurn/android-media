package com.tokopedia.design.banner;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alifa on 11/28/17.
 */

public class BannerView extends BaseCustomView {

    private static final long SLIDE_DELAY = 5000;
    private static final String SAVED = "instance state BannerView.class";
    private static final String SAVE_STATE_AUTO_SCROLL_ON_PROGRESS = "auto_scroll_on_progress";

    private RecyclerView bannerRecyclerView;
    private ViewGroup bannerIndicator;
    private View bannerSeeAll;
    private BannerPagerAdapter bannerPagerAdapter;
    private List<String> promoImageUrls;
    private OnPromoClickListener onPromoClickListener;

    private ArrayList<ImageView> indicatorItems;
    private ArrayList<Boolean> impressionStatusList;

    private int currentPosition;

    private Handler bannerHandler;
    private Runnable runnableScrollBanner;
    private boolean autoScrollOnProgress;

    public BannerView(@NonNull Context context) {
        super(context);
        init();
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_banner, this);
        bannerRecyclerView = view.findViewById(R.id.viewpager_banner_category);
        bannerIndicator = view.findViewById(R.id.indicator_banner_container);
        bannerSeeAll = view.findViewById(R.id.promo_link);
        indicatorItems = new ArrayList<>();
        impressionStatusList = new ArrayList<>();
        promoImageUrls = new ArrayList<>();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bannerSeeAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: banner tracking and click all
                //UnifyTracking.eventClickViewAllPromo();



               /* Intent intent = new Intent(getContext(), BannerWebView.class);
                intent.putExtra(BannerWebView.EXTRA_TITLE, getContext().getString(R.string.title_activity_promo));
                intent.putExtra(BannerWebView.EXTRA_URL,
                        TkpdBaseURL.URL_PROMO + TkpdBaseURL.FLAG_APP
                );
                getContext().startActivity(intent);*/
            }
        });

        invalidate();
        requestLayout();
    }

    public void setPromoList(List<String> promoImageUrls) {
        this.promoImageUrls = promoImageUrls;
    }

    public void buildView() {
        setVisibility(VISIBLE);
        resetImpressionStatus();

        bannerIndicator.setVisibility(VISIBLE);
        indicatorItems.clear();
        bannerIndicator.removeAllViews();

        BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(promoImageUrls,onPromoClickListener);

        bannerRecyclerView.setHasFixedSize(true);
        indicatorItems.clear();
        bannerIndicator.removeAllViews();
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        bannerRecyclerView.setLayoutManager(layoutManager);
        bannerRecyclerView.setAdapter(bannerPagerAdapter);

        for (int count = 0; count < promoImageUrls.size(); count++) {
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(5, 0, 5, 0);
            if (count == 0) {
                pointView.setImageResource(R.drawable.indicator_focus);
            } else {
                pointView.setImageResource(R.drawable.indicator);
            }
            indicatorItems.add(pointView);
            bannerIndicator.addView(pointView);
        }

        bannerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPosition =
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                setCurrentIndicator();
                trackingImpression();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && recyclerView.isInTouchMode()) {
                    stopAutoScrollBanner();
                }
            }

        });

        if (promoImageUrls.size() == 1) {
            bannerIndicator.setVisibility(View.GONE);
        }

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        bannerRecyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(bannerRecyclerView);

        bannerHandler = new Handler();
        runnableScrollBanner = new Runnable() {
            @Override
            public void run() {
                if (bannerRecyclerView != null) {
                    if (currentPosition == bannerRecyclerView.getAdapter().getItemCount() - 1) {
                        currentPosition = -1;
                    }
                    bannerRecyclerView.smoothScrollToPosition(currentPosition + 1);
                    bannerHandler.postDelayed(this, SLIDE_DELAY);
                }
            }
        };

        startAutoScrollBanner();
    }

    private void trackingImpression() {
        //TODO: banner tracking
       /* if (!isCurrentPositionHasImpression(currentPosition)) {
            impressionStatusList.set(currentPosition, true);

            Promotion promotion = new Promotion();
            promotion.setPromotionID(promoList.get(currentPosition).getPromoId());
            promotion.setPromotionName(promoList.get(currentPosition).getPromoTitle());
            promotion.setPromotionAlias(promoList.get(currentPosition).getPromoTitle());
            promotion.setPromotionPosition(currentPosition);

            PaymentTracking.eventPromoImpression(promotion);
        }*/
    }

    private void setCurrentIndicator() {
        for (int i = 0; i < indicatorItems.size(); i++) {
            if (currentPosition != i) {
                indicatorItems.get(i).setImageResource(R.drawable.indicator);
            } else {
                indicatorItems.get(i).setImageResource(R.drawable.indicator_focus);
            }
        }
    }

    private boolean isCurrentPositionHasImpression(int currentPosition) {
        if (currentPosition >= 0 && currentPosition <= impressionStatusList.size()) {
            return impressionStatusList.get(currentPosition);
        } else {
            return true;
        }
    }

    public void resetImpressionStatus() {
        impressionStatusList.clear();
        for (int i = 0; i < promoImageUrls.size(); i++) {
            impressionStatusList.add(false);
        }
    }

    public void startAutoScrollBanner() {
        if (bannerHandler != null && runnableScrollBanner != null) {
            setAutoScrollOnProgress(true);
            bannerHandler.postDelayed(runnableScrollBanner, SLIDE_DELAY);
        }
    }

    public void stopAutoScrollBanner() {
        if (bannerHandler != null && runnableScrollBanner != null) {
            setAutoScrollOnProgress(false);
            bannerHandler.removeCallbacks(runnableScrollBanner);
        }
    }

    public void setAutoScrollOnProgress(boolean autoScrollOnProgress) {
        this.autoScrollOnProgress = autoScrollOnProgress;
    }

    public boolean isAutoScrollOnProgress() {
        return autoScrollOnProgress;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setAutoScrollOnProgress(bundle.getBoolean(SAVE_STATE_AUTO_SCROLL_ON_PROGRESS));
            if (isAutoScrollOnProgress()) {
                startAutoScrollBanner();
            } else {
                stopAutoScrollBanner();
            }
            state = bundle.getParcelable(SAVED);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVED, super.onSaveInstanceState());
        bundle.putBoolean(SAVE_STATE_AUTO_SCROLL_ON_PROGRESS, isAutoScrollOnProgress());
        return bundle;
    }

    public void restartAutoScrollBanner() {
        resetImpressionStatus();
        startAutoScrollBanner();
    }

    public interface OnPromoClickListener {
        void onPromoClick(int position);
    }

    public OnPromoClickListener getOnPromoClickListener() {
        return onPromoClickListener;
    }

    public void setOnPromoClickListener(OnPromoClickListener onPromoClickListener) {
        this.onPromoClickListener = onPromoClickListener;
    }
}

