package com.tokopedia.shop.product.view.adapter.viewholder;

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
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import static com.tokopedia.shop.common.constant.ShopPageConstant.ITEM_OFFSET;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductViewHolder extends AbstractViewHolder<ShopProductViewModel> {

    @LayoutRes
    public static final int GRID_LAYOUT = R.layout.item_shop_product_grid;
    public static final int LIST_LAYOUT = R.layout.item_shop_product_list;
    public static final double RATIO_WITH_RELATIVE_TO_SCREEN = 2.3;

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

    private boolean isFixWidth;
    private @ShopTrackProductTypeDef int shopTrackType;
    private int deviceWidth;
    private int layoutType;
    private View vgRating;
    private View badgeContainer;

    public ShopProductViewHolder(View itemView, ShopProductClickedListener shopProductClickedListener,
                                 boolean isFixWidth, int deviceWidth,
                                 @ShopTrackProductTypeDef int shopTrackType, int layoutType) {
        super(itemView);
        this.isFixWidth = isFixWidth;
        this.shopTrackType = shopTrackType;
        this.deviceWidth = deviceWidth;
        this.layoutType = layoutType;
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

        badgeContainer = view.findViewById(R.id.badges_container);

        freeReturnImageView = view.findViewById(R.id.image_view_free_return);
        productImageView = view.findViewById(R.id.product_image);
        wishlistImageView = view.findViewById(R.id.image_view_wishlist);
        wishlistContainer = view.findViewById(R.id.wishlist_button_container);
        soldOutView = view.findViewById(R.id.sold_out_view);
        vgRating = view.findViewById(R.id.vg_rating);
        qualityRatingBar = view.findViewById(R.id.ratingBar);
        totalReview = view.findViewById(R.id.total_review);

    }

    @Override
    public void bind(final ShopProductViewModel shopProductViewModel) {
        if (isFixWidth && deviceWidth > 0 && layoutType == ShopProductViewHolder.GRID_LAYOUT) {
            itemView.getLayoutParams().width = (int) (deviceWidth / RATIO_WITH_RELATIVE_TO_SCREEN);
        }

        updateDisplayGeneralView(shopProductViewModel);
        updateDisplayPrice(shopProductViewModel);
        updateDisplayRating(shopProductViewModel);
        updateDisplayBadges(shopProductViewModel);
        updateDisplayWishList(shopProductViewModel);
    }

    private void updateDisplayGeneralView(final ShopProductViewModel shopProductViewModel) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductClicked(shopProductViewModel);
            }
        });

        titleTextView.setText(shopProductViewModel.getName());
        ImageHandler.LoadImage(productImageView, getImageUrl(shopProductViewModel));

        if (soldOutView == null) return;

        if (shopProductViewModel.isSoldOut()) {
            soldOutView.setVisibility(View.VISIBLE);
        } else {
            soldOutView.setVisibility(View.GONE);
        }
    }

    protected void onProductClicked(ShopProductViewModel shopProductViewModel) {
        shopProductClickedListener.onProductClicked(shopProductViewModel, shopTrackType, getAdapterPosition() - ITEM_OFFSET );
    }

    private void updateDisplayRating(final ShopProductViewModel shopProductViewModel) {
        if (TextUtils.isEmpty(shopProductViewModel.getTotalReview()) ||
                shopProductViewModel.getRating() == 0) {
            vgRating.setVisibility(View.GONE);
        } else {
            totalReview.setText(itemView.getResources().getString(R.string.total_point_format,
                    String.valueOf(shopProductViewModel.getTotalReview())));
            if (qualityRatingBar != null) {
                qualityRatingBar.setRating((float) shopProductViewModel.getRating());
            }
            vgRating.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplayPrice(final ShopProductViewModel shopProductViewModel) {
        if (!TextUtils.isEmpty(shopProductViewModel.getOriginalPrice())) {
            originalPriceTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriceTextView.setVisibility(View.VISIBLE);
            originalPriceTextView.setText(shopProductViewModel.getOriginalPrice());
        } else {
            originalPriceTextView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(shopProductViewModel.getDiscountPercentage()) && Integer.parseInt(shopProductViewModel.getDiscountPercentage()) > 0) {
            discountPercentageTextView.setVisibility(View.VISIBLE);
            discountPercentageTextView.setText(discountPercentageTextView.getContext().
                    getString(R.string.shop_product_discount_percentage_format, shopProductViewModel.getDiscountPercentage()));
        } else {
            discountPercentageTextView.setVisibility(View.GONE);
        }
        if (displayedPriceTextView != null) {
            displayedPriceTextView.setText(shopProductViewModel.getDisplayedPrice());
        }
    }

    private void updateDisplayBadges(final ShopProductViewModel shopProductViewModel) {
        badgeContainer.setVisibility(View.GONE);
        if (shopProductViewModel.getCashback() > 0) {
            cashBackTextView.setText(cashBackTextView.getContext().getString(
                    R.string.shop_product_manage_item_cashback, (int) shopProductViewModel.getCashback()));
            cashBackTextView.setVisibility(View.VISIBLE);
            badgeContainer.setVisibility(View.VISIBLE);
        } else {
            cashBackTextView.setVisibility(View.GONE);
        }

        if (shopProductViewModel.isFreeReturn()) {
            freeReturnImageView.setVisibility(View.VISIBLE);
            badgeContainer.setVisibility(View.VISIBLE);
        } else {
            freeReturnImageView.setVisibility(View.GONE);
        }

        if (shopProductViewModel.isPo()) {
            preOrderTextView.setVisibility(View.VISIBLE);
            badgeContainer.setVisibility(View.VISIBLE);
        } else {
            preOrderTextView.setVisibility(View.GONE);
        }

        if (shopProductViewModel.isWholesale()) {
            wholesaleTextView.setVisibility(View.VISIBLE);
            badgeContainer.setVisibility(View.VISIBLE);
        } else {
            wholesaleTextView.setVisibility(View.GONE);
        }
    }

    private void updateDisplayWishList(final ShopProductViewModel shopProductViewModel) {
        wishlistContainer.setVisibility(shopProductViewModel.isShowWishList() ? View.VISIBLE : View.GONE);
        wishlistImageView.setImageResource(shopProductViewModel.isWishList() ? R.drawable.ic_wishlist_checked : R.drawable.ic_wishlist_unchecked);
        wishlistContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWishlistClicked(shopProductViewModel);
            }
        });
    }

    protected void onWishlistClicked(ShopProductViewModel shopProductViewModel) {
        shopProductClickedListener.onWishListClicked(shopProductViewModel, shopTrackType);
    }

    protected String getImageUrl(ShopProductViewModel shopProductViewModel) {
        return shopProductViewModel.getImageUrl();
    }
}