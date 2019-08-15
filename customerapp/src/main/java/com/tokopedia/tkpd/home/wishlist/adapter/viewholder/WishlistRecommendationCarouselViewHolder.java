package com.tokopedia.tkpd.home.wishlist.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.wishlist.adapter.WishlistCarouselAdapter;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecommendationCarouselItemViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecommendationCarouselViewModel;
import com.tokopedia.tkpd.home.wishlist.analytics.WishlistAnalytics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Author Lukas on 14/08/2019
 */
public class WishlistRecommendationCarouselViewHolder extends AbstractViewHolder<WishlistRecommendationCarouselViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.layout_wishlist_carousel;
    private List<WishlistRecommendationCarouselItemViewModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView titleView;
    private WishlistCarouselAdapter adapter;
    private WishlistAnalytics wishlistAnalytics;
    public WishlistRecommendationCarouselViewHolder(View itemView, WishlistAnalytics wishlistAnalytics) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.list);
        titleView = itemView.findViewById(R.id.title);
        this.wishlistAnalytics = wishlistAnalytics;
    }

    @Override
    public void bind(WishlistRecommendationCarouselViewModel element) {
        titleView.setText(element.getTitle());
        setupRecyclerView(element);
    }

    private void setupRecyclerView(WishlistRecommendationCarouselViewModel dataModel){
        adapter = new WishlistCarouselAdapter(recyclerView.getContext(), dataModel.getRecommendationItems(), wishlistAnalytics, dataModel.getListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    public void updateWishlist(int position, boolean isWishlist){
        adapter.updateWishlist(position, isWishlist);
    }
}
