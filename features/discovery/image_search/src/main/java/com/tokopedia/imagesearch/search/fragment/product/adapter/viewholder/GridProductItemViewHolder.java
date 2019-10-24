package com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.imagesearch.R;
import com.tokopedia.imagesearch.domain.viewmodel.BadgeItem;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.imagesearch.helper.BadgeHelper;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

import java.util.List;

/**
 * Created by henrypriyono on 10/11/17.
 */

public class GridProductItemViewHolder extends AbstractViewHolder<ProductItem> {

    @LayoutRes
    public static final int LAYOUT = R.layout.image_search_result_product_item;

    protected ImpressedImageView productImage;
    private TextView title;
    private TextView price;
    private TextView location;
    private LinearLayout badgesContainer;
    private ImageView wishlistButton;
    private RelativeLayout wishlistButtonContainer;
    private View container;
    private ImageView rating;
    private TextView reviewCount;
    private LinearLayout ratingReviewContainer;
    private ProductListener productListener;
    protected Context context;
    private TextView topLabel;
    private TextView bottomLabel;
    private TextView newLabel;
    private String searchQuery;
    private RelativeLayout topadsIcon;

    public GridProductItemViewHolder(View itemView, ProductListener productListener, String searchQuery) {
        super(itemView);
        this.searchQuery = searchQuery;
        productImage = itemView.findViewById(R.id.image_search_product_image);
        title = (TextView) itemView.findViewById(R.id.image_search_title_text);
        price = (TextView) itemView.findViewById(R.id.image_search_price);
        location = (TextView) itemView.findViewById(R.id.image_search_location);
        badgesContainer = (LinearLayout) itemView.findViewById(R.id.image_search_badges_container);
        wishlistButton = (ImageView) itemView.findViewById(R.id.image_search_wishlist_button);
        wishlistButtonContainer = (RelativeLayout) itemView.findViewById(R.id.image_search_wishlist_button_container);
        container = itemView.findViewById(R.id.image_search_product_item_container);
        rating = (ImageView) itemView.findViewById(R.id.image_search_rating);
        reviewCount = (TextView) itemView.findViewById(R.id.image_search_review_count);
        ratingReviewContainer = (LinearLayout) itemView.findViewById(R.id.image_search_rating_review_container);
        topLabel = itemView.findViewById(R.id.image_search_topLabel);
        bottomLabel = itemView.findViewById(R.id.image_search_bottomLabel);
        newLabel = itemView.findViewById(R.id.image_search_new_label);
        topadsIcon = itemView.findViewById(R.id.image_search_topads_icon);
        context = itemView.getContext();
        this.productListener = productListener;
    }

    @Override
    public void bind(final ProductItem productItem) {
        if (!TextUtils.isEmpty(productItem.getTopLabel())) {
            topLabel.setText(productItem.getTopLabel());
            topLabel.setVisibility(View.VISIBLE);
        } else {
            topLabel.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(productItem.getBottomLabel())) {
            bottomLabel.setText(productItem.getBottomLabel());
            bottomLabel.setVisibility(View.VISIBLE);
        } else {
            bottomLabel.setVisibility(View.GONE);
        }
        title.setText(MethodChecker.fromHtml(productItem.getProductName()));
        String priceText = !TextUtils.isEmpty(productItem.getPriceRange()) ?
                productItem.getPriceRange() : productItem.getPrice();
        price.setText(priceText);
        if (productItem.getShopCity() != null && !productItem.getShopCity().isEmpty()) {
            if (isBadgesExist(productItem)) {
                location.setText(" \u2022 " + MethodChecker.fromHtml(productItem.getShopCity()));
            } else {
                location.setText(MethodChecker.fromHtml(productItem.getShopCity()));
            }
            location.setVisibility(View.VISIBLE);
        } else {
            location.setVisibility(View.GONE);
        }

        setImageProduct(productItem);
        if (productItem.isTopAds()) {
            topadsIcon.setVisibility(View.VISIBLE);
        } else {
            topadsIcon.setVisibility(View.GONE);
        }
        productImage.setViewHintListener(productItem, new ImpressedImageView.ViewHintListener() {
            @Override
            public void onViewHint() {
                productListener.onProductImpressed(productItem, getAdapterPosition());
            }
        });
        wishlistButtonContainer.setVisibility(View.VISIBLE);
        wishlistButton.setBackgroundResource(com.tokopedia.topads.sdk.R.drawable.ic_wishlist);

        if (productItem.isWishlisted()) {
            wishlistButton.setBackgroundResource(com.tokopedia.topads.sdk.R.drawable.ic_wishlist_red);
        } else {
            wishlistButton.setBackgroundResource(com.tokopedia.topads.sdk.R.drawable.ic_wishlist);
        }

        wishlistButtonContainer.setEnabled(productItem.isWishlistButtonEnabled());

        wishlistButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productItem.isWishlistButtonEnabled()) {
                    productListener.onWishlistButtonClicked(productItem);
                }
            }
        });

        container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                productListener.onLongClick(productItem, getAdapterPosition());
                return true;
            }
        });

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productListener.onItemClicked(productItem, getAdapterPosition());
            }
        });

        if (productItem.getRating() != 0) {
            ratingReviewContainer.setVisibility(View.VISIBLE);
            rating.setImageResource(
                    getRatingDrawable(Math.round(productItem.getRating())
                    ));
            reviewCount.setText("(" + productItem.getCountReview() + ")");
        } else {
            ratingReviewContainer.setVisibility(View.GONE);
        }
        if (productItem.isNew()) {
            newLabel.setVisibility(View.VISIBLE);
        } else {
            newLabel.setVisibility(View.GONE);
        }
        renderBadges(productItem.getBadgesList());
    }

    private boolean isBadgesExist(ProductItem productItem) {
        List<BadgeItem> badgesList = productItem.getBadgesList();
        if (badgesList == null || badgesList.isEmpty()) {
            return false;
        }

        for (BadgeItem badgeItem : badgesList) {
            if (badgeItem.isShown()) {
                return true;
            }
        }
        return false;
    }

    protected void renderBadges(List<BadgeItem> badgesList) {
        badgesContainer.removeAllViews();
        for (BadgeItem badgeItem : badgesList) {
            if (badgeItem.isShown()) {
                BadgeHelper.loadShopBadgesIcon(badgeItem.getImageUrl(), badgesContainer, context);
            }
        }
    }

    public void setImageProduct(ProductItem productItem) {
        ImageHandler.loadImageThumbs(context, productImage, productItem.getImageUrl());
    }

    private static int getRatingDrawable(int param) {
        switch (param) {
            case 0:
                return R.drawable.ic_star_none;
            case 1:
                return R.drawable.ic_star_one;
            case 2:
                return R.drawable.ic_star_two;
            case 3:
                return R.drawable.ic_star_three;
            case 4:
                return R.drawable.ic_star_four;
            case 5:
                return R.drawable.ic_star_five;
            default:
                return R.drawable.ic_star_none;
        }
    }
}