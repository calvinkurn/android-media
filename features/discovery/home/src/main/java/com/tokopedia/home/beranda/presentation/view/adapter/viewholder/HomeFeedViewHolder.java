package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.gql.feed.Badge;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
import com.tokopedia.productcard.ProductCardView;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedViewHolder extends AbstractViewHolder<HomeFeedViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.home_feed_item;

    private ProductCardView productCardView;
    private final HomeFeedContract.View homeFeedview;

    public HomeFeedViewHolder(View itemView, HomeFeedContract.View homeFeedview) {
        super(itemView);
        this.homeFeedview = homeFeedview;
        productCardView = itemView.findViewById(R.id.productCardView);
    }

    @Override
    public void bind(HomeFeedViewModel element) {
        productCardView.setImageUrl(element.getImageUrl());
        productCardView.setTitle(element.getProductName());
        productCardView.setPrice(element.getPrice());
        productCardView.setTopAdsVisible(element.isTopAds());
        productCardView.setWishlistButtonVisible(false);
        productCardView.setSlashedPrice(element.getSlashedPrice());
        productCardView.setDiscount(element.getDiscountPercentage());
        productCardView.setRatingReviewCount(element.getRating(), element.getCountReview());
        productCardView.setBadges(mapBadges(element.getBadges()));
        productCardView.setLocation(element.getLocation());
        productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFeedview.onProductClick(element, getAdapterPosition());
            }
        });
        productCardView.getImageView().setViewHintListener(element, new ImpressedImageView.ViewHintListener() {
            @Override
            public void onViewHint() {
                homeFeedview.onProductImpression(element, getAdapterPosition());
            }
        });
    }

    private List<String> mapBadges(List<Badge> badges){
        List<String> result = new ArrayList<>();
        for (Badge badge : badges){
            result.add(badge.getImageUrl());
        }
        return result;
    }
}
