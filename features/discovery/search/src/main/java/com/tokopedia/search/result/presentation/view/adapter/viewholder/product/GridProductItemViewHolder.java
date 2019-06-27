package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

import java.util.List;

/**
 * Created by henrypriyono on 10/11/17.
 */

@Deprecated
public class GridProductItemViewHolder extends AbstractViewHolder<ProductItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_srp_item_grid;

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
    private RelativeLayout topadsIcon;

    public GridProductItemViewHolder(View itemView, ProductListener productListener) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
        location = itemView.findViewById(R.id.location);
        badgesContainer = itemView.findViewById(R.id.badges_container);
        wishlistButton = itemView.findViewById(R.id.wishlist_button);
        wishlistButtonContainer = itemView.findViewById(R.id.wishlist_button_container);
        container = itemView.findViewById(R.id.container);
        rating = itemView.findViewById(R.id.rating);
        reviewCount = itemView.findViewById(R.id.review_count);
        ratingReviewContainer = itemView.findViewById(R.id.rating_review_container);
        topLabel = itemView.findViewById(R.id.topLabel);
        bottomLabel = itemView.findViewById(R.id.bottomLabel);
        newLabel = itemView.findViewById(R.id.new_label);
        topadsIcon = itemView.findViewById(R.id.topads_icon);
        context = itemView.getContext();
        this.productListener = productListener;
    }

    @Override
    public void bind(final ProductItemViewModel productItem) {
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
                if (productListener != null) {
                    productListener.onProductImpressed(productItem, getAdapterPosition());
                }
            }
        });
        wishlistButtonContainer.setVisibility(View.VISIBLE);
        wishlistButton.setBackgroundResource(R.drawable.ic_wishlist);

        if (productItem.isWishlisted()) {
            wishlistButton.setBackgroundResource(R.drawable.ic_wishlist_red);
        } else {
            wishlistButton.setBackgroundResource(R.drawable.ic_wishlist);
        }

        wishlistButtonContainer.setEnabled(productItem.isWishlistButtonEnabled());

        wishlistButtonContainer.setOnClickListener(v -> {
            if (productListener != null && productItem.isWishlistButtonEnabled()) {
                productListener.onWishlistButtonClicked(productItem);
            }
        });

        container.setOnLongClickListener(v -> {
            if (productListener != null) {
                productListener.onLongClick(productItem, getAdapterPosition());
            }
            return true;
        });

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productListener != null) {
                    productListener.onItemClicked(productItem, getAdapterPosition());
                }
            }
        });

        if (productItem.getRating() != 0) {
            rating.setVisibility(View.VISIBLE);
            rating.setImageResource(
                    getRatingDrawable((productItem.isTopAds())
                            ? getStarCount(productItem.getRating())
                            : Math.round(productItem.getRating())
                    ));
        } else {
            rating.setVisibility(View.GONE);
        }

        if (productItem.getCountReview() != 0) {
            reviewCount.setVisibility(View.VISIBLE);
            reviewCount.setText("(" + productItem.getCountReview() + ")");
        } else {
            reviewCount.setVisibility(View.GONE);
        }

        if (productItem.getCountReview() != 0 || productItem.getRating() != 0) {
            ratingReviewContainer.setVisibility(View.VISIBLE);
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

    private int getRatingDrawable(int param) {
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

    private int getStarCount(int rating) {
        return Math.round(rating / 20f);
    }

    private boolean isBadgesExist(ProductItemViewModel productItem) {
        List<BadgeItemViewModel> badgesList = productItem.getBadgesList();
        if (badgesList == null || badgesList.isEmpty()) {
            return false;
        }

        for (BadgeItemViewModel badgeItem : badgesList) {
            if (badgeItem.isShown()) {
                return true;
            }
        }
        return false;
    }

    protected void renderBadges(List<BadgeItemViewModel> badgesList) {
        badgesContainer.removeAllViews();
        for (BadgeItemViewModel badgeItem : badgesList) {
            if (badgeItem.isShown()) {
                loadLuckyShopImage(context, badgeItem.getImageUrl(), badgesContainer);
            }
        }
    }

    public static void loadLuckyShopImage(Context context, String url, final LinearLayout container) {
        if (url != null && !url.equals("")) {
            final View view = LayoutInflater.from(context).inflate(R.layout.search_product_card_badge_layout, null);

            ImageHandler.loadImageBitmap2(context, url, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    ImageView image = view.findViewById(R.id.badge);
                    if (bitmap.getHeight() <= 1 && bitmap.getWidth() <= 1) {
                        view.setVisibility(View.GONE);
                    } else {
                        image.setImageBitmap(bitmap);
                        view.setVisibility(View.VISIBLE);
                        container.addView(view);
                    }
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    view.setVisibility(View.GONE);
                }
            });
        }
    }

    public void setImageProduct(ProductItemViewModel productItem) {
        ImageHandler.loadImageThumbs(context, productImage, productItem.getImageUrl());
    }

}