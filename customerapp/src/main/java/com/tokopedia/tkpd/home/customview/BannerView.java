package com.tokopedia.tkpd.home.customview;

import android.content.Context;
import android.content.Intent;
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

import com.tokopedia.core.analytics.container.GTMContainer;
import com.tokopedia.core.analytics.nishikino.model.Promotion;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.BannerPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 9/13/17.
 */

public class BannerView extends BaseCustomView {

    private static final long SLIDE_DELAY = 5000;

    private RecyclerView bannerRecyclerView;
    private ViewGroup bannerIndicator;
    private View bannerSeeAll;
    private List<PromoItem> promoList;

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
        View view = inflate(getContext(), R.layout.home_banner, this);
        bannerRecyclerView = (RecyclerView) view.findViewById(R.id.viewpager_banner_category);
        bannerIndicator = (ViewGroup) view.findViewById(R.id.indicator_banner_container);
        bannerSeeAll = view.findViewById(R.id.promo_link);
        indicatorItems = new ArrayList<>();
        impressionStatusList = new ArrayList<>();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        bannerSeeAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BannerWebView.class);
                intent.putExtra(BannerWebView.EXTRA_TITLE, getContext().getString(R.string.title_activity_promo));
                intent.putExtra(BannerWebView.EXTRA_URL,
                        TkpdBaseURL.URL_PROMO + TkpdBaseURL.FLAG_APP
                );
                getContext().startActivity(intent);
            }
        });

        invalidate();
        requestLayout();
    }

    public void setPromoList(List<PromoItem> promoList) {
        this.promoList = promoList;
    }

    public void buildView() {
        setVisibility(VISIBLE);

        resetImpressionStatus();

        BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(promoList);

        bannerRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        bannerRecyclerView.setLayoutManager(layoutManager);
        bannerRecyclerView.setAdapter(bannerPagerAdapter);

        for (int count = 0; count < promoList.size(); count++) {
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

        if (promoList.size() == 1) {
            bannerIndicator.setVisibility(View.GONE);
        }

        PagerSnapHelper snapHelper = new PagerSnapHelper();
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
        if (!isCurrentPositionHasImpression(currentPosition)) {
            impressionStatusList.set(currentPosition, true);

            Promotion promotion = new Promotion();
            promotion.setPromotionID(promoList.get(currentPosition).getPromoId());
            promotion.setPromotionName(promoList.get(currentPosition).getPromoTitle());
            promotion.setPromotionAlias(promoList.get(currentPosition).getPromoTitle());
            promotion.setPromotionPosition(currentPosition);

            GTMContainer gtmContainer = GTMContainer.newInstance(getContext());
            gtmContainer.eventBannerImpression(promotion);

        }
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
        for (int i = 0; i < promoList.size(); i++) {
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

    public static class PromoItem implements Parcelable {
        public String imgUrl;
        public String promoUrl;
        public String promoId;
        public String promoTitle;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getPromoUrl() {
            return promoUrl;
        }

        public void setPromoUrl(String promoUrl) {
            this.promoUrl = promoUrl;
        }

        public String getPromoId() {
            return promoId;
        }

        public void setPromoId(String promoId) {
            this.promoId = promoId;
        }

        public String getPromoTitle() {
            return promoTitle;
        }

        public void setPromoTitle(String promoTitle) {
            this.promoTitle = promoTitle;
        }

        public PromoItem() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.imgUrl);
            dest.writeString(this.promoUrl);
            dest.writeString(this.promoId);
            dest.writeString(this.promoTitle);
        }

        protected PromoItem(Parcel in) {
            this.imgUrl = in.readString();
            this.promoUrl = in.readString();
            this.promoId = in.readString();
            this.promoTitle = in.readString();
        }

        public static final Creator<PromoItem> CREATOR = new Creator<PromoItem>() {
            @Override
            public PromoItem createFromParcel(Parcel source) {
                return new PromoItem(source);
            }

            @Override
            public PromoItem[] newArray(int size) {
                return new PromoItem[size];
            }
        };
    }

}
