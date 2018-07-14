package com.tokopedia.shop.product.view.adapter.newadapter.viewholder;

import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductViewHolder extends AbstractViewHolder<ShopProductViewModelOld> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_grid;
    public static final int SPAN_LOOK_UP = 1;

    private final ShopProductClickedListener shopProductClickedListener;
    private ImageView wishlistImageView;
    private FrameLayout wishlistContainer;
    protected ImageView productImageView;
    protected TextView titleTextView;
    private TextView displayedPriceTextView;
    private TextView originalPriceTextView;
    private TextView discountPercentageTextView;
    private TextView cashBackTextView;
    private TextView wholesaleTextView;
    private TextView preOrderTextView;
    private ImageView freeReturnImageView;
    private AppCompatRatingBar qualityRatingBar;
    private TextView totalReview;
    private View soldOutView;

    public ShopProductViewHolder(View itemView, ShopProductClickedListener shopProductClickedListener) {
        super(itemView);
        this.shopProductClickedListener = shopProductClickedListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        titleTextView = view.findViewById(R.id.title);
        displayedPriceTextView = view.findViewById(R.id.text_view_displayed_price);
        originalPriceTextView = view.findViewById(R.id.text_view_original_price);
        discountPercentageTextView = view.findViewById(R.id.text_view_discount_percentage);

        cashBackTextView = view.findViewById(R.id.text_view_cashback);
        wholesaleTextView = view.findViewById(R.id.text_view_wholesale);
        preOrderTextView = view.findViewById(R.id.text_view_pre_order);

        freeReturnImageView = view.findViewById(R.id.image_view_free_return);
        productImageView = view.findViewById(R.id.product_image);
        wishlistImageView = view.findViewById(R.id.image_view_wishlist);
        wishlistContainer = view.findViewById(R.id.wishlist_button_container);
        soldOutView = view.findViewById(R.id.sold_out_view);

        qualityRatingBar = view.findViewById(R.id.ratingBar);
        totalReview = view.findViewById(R.id.total_review);
    }

    @Override
    public void bind(final ShopProductViewModelOld shopProductViewModelOld) {
        updateDisplayGeneralView(shopProductViewModelOld);
        updateDisplayPrice(shopProductViewModelOld);
        updateDisplayRating(shopProductViewModelOld);
        updateDisplayBadges(shopProductViewModelOld);
        updateDisplayWishList(shopProductViewModelOld);
    }

    private void updateDisplayGeneralView(final ShopProductViewModelOld shopProductViewModelOld) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductClicked(shopProductViewModelOld);
            }
        });

        titleTextView.setText(shopProductViewModelOld.getName());
        ImageHandler.LoadImage(productImageView, getImageUrl(shopProductViewModelOld));

        if (soldOutView == null) return;

        if (shopProductViewModelOld.isSoldOut()) {
            soldOutView.setVisibility(View.VISIBLE);
        } else {
            soldOutView.setVisibility(View.GONE);
        }
    }

    protected void onProductClicked(ShopProductViewModelOld shopProductViewModelOld) {
        shopProductClickedListener.onProductClicked(shopProductViewModelOld);
    }

    private void updateDisplayRating(final ShopProductViewModelOld shopProductViewModelOld) {
        if (totalReview != null && qualityRatingBar != null && !TextUtils.isEmpty(shopProductViewModelOld.getTotalReview())) {
            totalReview.setText(itemView.getResources().getString(R.string.total_point_format,
                    String.valueOf(shopProductViewModelOld.getTotalReview())));
            totalReview.setVisibility(View.VISIBLE);
            if (qualityRatingBar != null) {
                qualityRatingBar.setRating((float) shopProductViewModelOld.getRating());
                qualityRatingBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateDisplayPrice(final ShopProductViewModelOld shopProductViewModelOld) {
        if (!TextUtils.isEmpty(shopProductViewModelOld.getOriginalPrice())) {
            originalPriceTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriceTextView.setVisibility(View.VISIBLE);
            originalPriceTextView.setText(shopProductViewModelOld.getOriginalPrice());
        } else {
            originalPriceTextView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(shopProductViewModelOld.getDiscountPercentage())) {
            discountPercentageTextView.setVisibility(View.VISIBLE);
            discountPercentageTextView.setText(discountPercentageTextView.getContext().
                    getString(R.string.shop_product_discount_percentage_format, shopProductViewModelOld.getDiscountPercentage()));
        } else {
            discountPercentageTextView.setVisibility(View.GONE);
        }
        if (displayedPriceTextView != null) {
            displayedPriceTextView.setText(shopProductViewModelOld.getDisplayedPrice());
        }
    }

    private void updateDisplayBadges(final ShopProductViewModelOld shopProductViewModelOld) {
        if (shopProductViewModelOld.getCashback() > 0) {
            cashBackTextView.setText(cashBackTextView.getContext().getString(
                    R.string.shop_product_manage_item_cashback, (int) shopProductViewModelOld.getCashback()));
            cashBackTextView.setVisibility(View.VISIBLE);
        } else {
            cashBackTextView.setVisibility(View.GONE);
        }
        freeReturnImageView.setVisibility(shopProductViewModelOld.isFreeReturn() ? View.VISIBLE : View.GONE);
        preOrderTextView.setVisibility(shopProductViewModelOld.isPo() ? View.VISIBLE : View.GONE);
        wholesaleTextView.setVisibility(shopProductViewModelOld.isWholesale() ? View.VISIBLE : View.GONE);
    }

    private void updateDisplayWishList(final ShopProductViewModelOld shopProductViewModelOld) {
        wishlistContainer.setVisibility(shopProductViewModelOld.isShowWishList() ? View.VISIBLE : View.GONE);
        wishlistImageView.setImageResource(shopProductViewModelOld.isWishList() ? R.drawable.ic_wishlist_checked : R.drawable.ic_wishlist_unchecked);
        wishlistContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWishlistClicked(shopProductViewModelOld);
            }
        });
    }

    protected void onWishlistClicked(ShopProductViewModelOld shopProductViewModelOld) {
        shopProductClickedListener.onWishListClicked(shopProductViewModelOld);
    }

    protected String getImageUrl(ShopProductViewModelOld shopProductViewModelOld) {
        return shopProductViewModelOld.getImageUrl();
    }
}