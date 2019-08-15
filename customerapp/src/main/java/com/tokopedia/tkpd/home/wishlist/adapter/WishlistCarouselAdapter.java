package com.tokopedia.tkpd.home.wishlist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.tkpd.home.wishlist.adapter.viewholder.WishlistRecommendationCarouselItemViewHolder;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecommendationCarouselItemViewModel;
import com.tokopedia.tkpd.home.wishlist.analytics.WishlistAnalytics;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Lukas on 2019-08-14
 */
public class WishlistCarouselAdapter extends RecyclerView.Adapter<WishlistRecommendationCarouselItemViewHolder> implements RecommendationCardView.TrackingListener {
    private List<RecommendationItem> itemList;
    private Context context;
    private RecommendationCardView.WishlistListener listener;
    private WishlistAnalytics wishlistAnalytics;
    public WishlistCarouselAdapter(Context context, List<RecommendationItem> itemList, WishlistAnalytics wishlistAnalytics, RecommendationCardView.WishlistListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;
        this.wishlistAnalytics = wishlistAnalytics;
    }

    @NonNull
    @Override
    public WishlistRecommendationCarouselItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(WishlistRecommendationCarouselItemViewHolder.LAYOUT, viewGroup, false);
        return new WishlistRecommendationCarouselItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistRecommendationCarouselItemViewHolder viewHolder, int i) {
        viewHolder.bind(new WishlistRecommendationCarouselItemViewModel(itemList.get(i), this, listener));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onImpressionTopAds(@NotNull RecommendationItem item) {
        wishlistAnalytics.eventRecommendationProductImpression(item, item.getPosition());
    }

    @Override
    public void onImpressionOrganic(@NotNull RecommendationItem item) {
        wishlistAnalytics.eventRecommendationProductImpression(item, item.getPosition());
    }

    @Override
    public void onClickTopAds(@NotNull RecommendationItem item) {
        wishlistAnalytics.eventRecommendationProductClick(item, item.getPosition());
    }

    @Override
    public void onClickOrganic(@NotNull RecommendationItem item) {
        wishlistAnalytics.eventRecommendationProductClick(item, item.getPosition());
    }

    public void updateWishlist(int position, boolean isWishlist){
        itemList.get(position).setWishlist(isWishlist);
        notifyItemChanged(position);
    }
}
