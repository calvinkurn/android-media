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

    protected RecyclerView bannerRecyclerView;
    private ViewGroup bannerIndicator;
    private View bannerSeeAll;
    private Handler bannerHandler;
    private Runnable runnableScrollBanner;
    private boolean autoScrollOnProgress;

    private OnPromoClickListener onPromoClickListener;
    private OnPromoLoadedListener onPromoLoadedListener;
    private OnPromoScrolledListener onPromoScrolledListener;
    private OnPromoAllClickListener onPromoAllClickListener;
    private OnPromoDragListener onPromoDragListener;

    private ArrayList<ImageView> indicatorItems;
    private ArrayList<Boolean> impressionStatusList;
    protected List<String> promoImageUrls;
    protected int currentPosition;

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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bannerSeeAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPromoAllClickListener != null) onPromoAllClickListener.onPromoAllClick();
            }
        });

        invalidate();
        requestLayout();
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScrollBanner();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoScrollBanner();
    }

    public interface OnPromoLoadedListener {
        void onPromoLoaded();
    }

    public interface OnPromoClickListener {
        void onPromoClick(int position);
    }

    public interface OnPromoScrolledListener {
        void onPromoScrolled(int position);
    }

    public interface OnPromoAllClickListener {
        void onPromoAllClick();
    }

    public interface OnPromoDragListener {
        void onPromoDragStart();
        void onPromoDragEnd();
    }

    private void init(AttributeSet attrs) {
        init();
    }

    protected void init() {
        View view = inflateView();
        bannerRecyclerView = view.findViewById(R.id.viewpager_banner_category);
        bannerIndicator = view.findViewById(R.id.indicator_banner_container);
        bannerSeeAll = view.findViewById(R.id.promo_link);
        indicatorItems = new ArrayList<>();
        impressionStatusList = new ArrayList<>();
        promoImageUrls = new ArrayList<>();
    }

    protected View inflateView() {
        return inflate(getContext(), R.layout.widget_banner, this);
    }

    public void buildView() {
        setVisibility(VISIBLE);
        resetImpressionStatus();
        bannerIndicator.setVisibility(VISIBLE);
        indicatorItems.clear();
        bannerIndicator.removeAllViews();

        BannerPagerAdapter bannerPagerAdapter = getBannerAdapter();
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
        bannerRecyclerView.clearOnScrollListeners();
        bannerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPosition =
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                setCurrentIndicator();
                if (!isCurrentPositionHasImpression(currentPosition)) {
                    impressionStatusList.set(currentPosition, true);
                    onPromoScrolledListener.onPromoScrolled(currentPosition);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && recyclerView.isInTouchMode()) {
                    stopAutoScrollBanner();
                    if (onPromoDragListener != null) {
                        onPromoDragListener.onPromoDragStart();
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (onPromoDragListener != null) {
                        onPromoDragListener.onPromoDragEnd();
                    }
                }
            }

        });
        if (promoImageUrls.size() == 1) {
            bannerIndicator.setVisibility(View.GONE);
        }
        if (onPromoLoadedListener!= null){
            onPromoLoadedListener.onPromoLoaded();
        }
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        bannerRecyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(bannerRecyclerView);
        if (bannerHandler == null && runnableScrollBanner == null) {
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
    }

    protected BannerPagerAdapter getBannerAdapter() {
        return new BannerPagerAdapter(promoImageUrls, onPromoClickListener);
    }

    public void setPagerAdapter(BannerPagerAdapter bannerPagerAdapter) {
        bannerRecyclerView.setAdapter(bannerPagerAdapter);
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

    public void restartAutoScrollBanner() {
        resetImpressionStatus();
        startAutoScrollBanner();
    }

    public void setPromoList(List<String> promoImageUrls) {
        this.promoImageUrls = promoImageUrls;
    }

    public OnPromoClickListener getOnPromoClickListener() {
        return onPromoClickListener;
    }

    public void setOnPromoClickListener(OnPromoClickListener onPromoClickListener) {
        this.onPromoClickListener = onPromoClickListener;
    }

    public OnPromoScrolledListener getOnPromoScrolledListener() {
        return onPromoScrolledListener;
    }

    public void setOnPromoScrolledListener(OnPromoScrolledListener onPromoScrolledListener) {
        this.onPromoScrolledListener = onPromoScrolledListener;
    }

    public void setOnPromoLoadedListener(OnPromoLoadedListener onPromoLoadedListener) {
        this.onPromoLoadedListener = onPromoLoadedListener;
    }

    public OnPromoAllClickListener getOnPromoAllClickListener() {
        return onPromoAllClickListener;
    }

    public void setOnPromoAllClickListener(OnPromoAllClickListener onPromoAllClickListener) {
        this.onPromoAllClickListener = onPromoAllClickListener;
    }

    public void setOnPromoDragListener(OnPromoDragListener onPromoDragListener) {
        this.onPromoDragListener = onPromoDragListener;
    }

    public boolean isInitialized() {
        return (bannerHandler != null && runnableScrollBanner != null && !promoImageUrls.isEmpty());
    }
}

