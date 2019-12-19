package com.tokopedia.tkpd.home.wishlist.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kotlin.extensions.view.ViewHintListener;
import com.tokopedia.productcard.v2.BlankSpaceConfig;
import com.tokopedia.productcard.v2.ProductCardModel;
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid;
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecomendationViewModel;
import com.tokopedia.tkpd.home.wishlist.analytics.WishlistAnalytics;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistRecomendationViewHolder extends AbstractViewHolder<WishlistRecomendationViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.wishlist_item_recomnedation;
    private ProductCardViewSmallGrid productCardViewSmallGrid;
    private WishlistAnalytics wishlistAnalytics;

    public WishlistRecomendationViewHolder(View itemView, WishlistAnalytics wishlistAnalytics) {
        super(itemView);
        this.wishlistAnalytics = wishlistAnalytics;
        productCardViewSmallGrid = itemView.findViewById(com.tokopedia.navigation.R.id.productCardView);
    }

    @Override
    public void bind(WishlistRecomendationViewModel element) {
        RecommendationItem item = element.getRecommendationItem();
        List<ProductCardModel.ShopBadge> badges = new ArrayList<>();
        for (String badge : item.getBadgesUrl()) {
            badges.add(new ProductCardModel.ShopBadge(true, badge));
        }

        ProductCardModel.FreeOngkir freeOngkir = new ProductCardModel.FreeOngkir(
                item.isFreeOngkirActive(),
                item.getFreeOngkirImageUrl()
        );

        productCardViewSmallGrid.setProductModel(
                new ProductCardModel(
                        item.getImageUrl(),
                        item.isWishlist(),
                        false,
                        new ProductCardModel.Label(),
                        "",
                        item.getShopName(),
                        item.getName(),
                        String.valueOf(item.getDiscountPercentage()),
                        item.getSlashedPrice(),
                        item.getPrice(),
                        badges,
                        item.getLocation(),
                        item.getRating(),
                        item.getCountReview(),
                        new ProductCardModel.Label(),
                        new ProductCardModel.Label(),
                        freeOngkir,
                        item.isTopAds()
                ), new BlankSpaceConfig()
        );

        productCardViewSmallGrid.setImageProductViewHintListener(
                item, () -> {
                    wishlistAnalytics.eventEmptyWishlistProductImpressions(item, item.getPosition());
                }
        );

        productCardViewSmallGrid.setOnClickListener(view -> {
            wishlistAnalytics.eventEmptyWishlistProductClick(item, item.getPosition());
        });
    }
}
