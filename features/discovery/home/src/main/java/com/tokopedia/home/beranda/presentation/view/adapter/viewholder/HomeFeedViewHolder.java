package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

//import android.support.annotation.LayoutRes;
//import android.view.View;
//
//import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
//import com.tokopedia.home.R;
//import com.tokopedia.home.beranda.domain.gql.feed.Badge;
//import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
//import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
//import com.tokopedia.productcard.v2.ProductCardViewSmallGrid;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HomeFeedViewHolder extends AbstractViewHolder<HomeFeedViewModel> {
//
//    @LayoutRes
//    public static final int LAYOUT = R.layout.home_feed_item;
//
//    private ProductCardViewSmallGrid productCardView;
//    private final HomeFeedContract.View homeFeedview;
//
//    public HomeFeedViewHolder(View itemView, HomeFeedContract.View homeFeedview) {
//        super(itemView);
//        this.homeFeedview = homeFeedview;
//        productCardView = itemView.findViewById(R.id.productCardView);
//    }
//
//    @Override
//    public void bind(HomeFeedViewModel element) {
//        productCardView.setImageProductUrl(element.getImageUrl());
//        productCardView.setProductNameText(element.getProductName());
//        productCardView.setPriceText(element.getPrice());
//        productCardView.setImageTopAdsVisible(element.isTopAds());
//        productCardView.setButtonWishlistVisible(true);
//        productCardView.setSlashedPriceText(element.getSlashedPrice());
//        productCardView.setLabelDiscountText(element.getDiscountPercentage());
//        productCardView.setRating(element.getRating());
//        productCardView.setReviewCount(element.getCountReview());
//        productCardView.addShopBadge(mapBadges(element.getBadges()));
//        productCardView.setShopLocationText(element.getLocation());
//        productCardView.setImageProductViewHintListener(element, new Unit(){
//            @Override
//            public void onViewHint() {
//                homeFeedview.onProductImpression(element, getAdapterPosition());
//            }
//        });
//    }
//
//    private List<String> mapBadges(List<Badge> badges){
//        List<String> result = new ArrayList<>();
//        for (Badge badge : badges){
//            result.add(badge.getImageUrl());
//        }
//        return result;
//    }
//}
