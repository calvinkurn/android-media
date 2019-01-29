package com.tokopedia.topads.sdk.view.adapter.viewholder.discovery;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.PositionChangeListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductBigViewModel;

import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductBigViewHolder extends AbstractViewHolder<ProductBigViewModel> implements
        View.OnClickListener, PositionChangeListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_product_list_big;
    private static final String TAG = ProductBigViewHolder.class.getSimpleName();

    private LocalAdsClickListener itemClickListener;
    private Data data;
    private Context context;
    public LinearLayout badgeContainer;
    public TextView productName;
    public TextView productPrice;
    public TextView shopLocation;
    public ImpressedImageView productImage;
    private ImageLoader imageLoader;
    private ImageView rating;
    private TextView newLabelTxt;
    private TextView reviewCount;
    private LinearLayout topLabelContainer;
    private LinearLayout bottomLabelContainer;
    private ImageView btnWishList;
    private int clickPosition;
    private RelativeLayout wishlistBtnContainer;
    private TopAdsItemImpressionListener impressionListener;


    public ProductBigViewHolder(View itemView, ImageLoader imageLoader,
                                LocalAdsClickListener itemClickListener,
                                TopAdsItemImpressionListener itemImpressionListener,
                                boolean enableWishlist) {
        super(itemView);
        itemView.findViewById(R.id.container).setOnClickListener(this);
        itemView.findViewById(R.id.wishlist_button_container).setOnClickListener(this);
        this.itemClickListener = itemClickListener;
        this.impressionListener = itemImpressionListener;
        this.imageLoader = imageLoader;
        this.clickPosition = clickPosition;
        context = itemView.getContext();
        badgeContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        productImage = (ImpressedImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.title);
        productPrice = (TextView) itemView.findViewById(R.id.price);
        shopLocation = (TextView) itemView.findViewById(R.id.location);
        rating = (ImageView) itemView.findViewById(R.id.rating);
        reviewCount = (TextView) itemView.findViewById(R.id.review_count);
        newLabelTxt = itemView.findViewById(R.id.new_label);
        topLabelContainer = itemView.findViewById(R.id.top_label_container);
        bottomLabelContainer = itemView.findViewById(R.id.bottom_label_container);
        btnWishList = itemView.findViewById(R.id.wishlist_button);
        wishlistBtnContainer = itemView.findViewById(R.id.wishlist_button_container);
        wishlistBtnContainer.setVisibility(enableWishlist ? View.VISIBLE : View.GONE);
        wishlistBtnContainer.setOnClickListener(this);

    }

    @Override
    public void bind(ProductBigViewModel element) {
        data = element.getData();
        if (data.getProduct() != null) {
            bindProduct(data.getProduct());
        }
        if (data.getShop() != null) {
            bindShop(data.getShop());
        }
    }

    private void bindShop(Shop shop) {
        if (shop.getBadges() != null && shop.getLocation() != null && !shop.getLocation().isEmpty()) {
            shopLocation.setVisibility(View.VISIBLE);
            imageLoader.loadBadge(badgeContainer, shop.getBadges());
            if (isBadgesExist(shop.getBadges())) {
                shopLocation.setText(String.format(" \u2022 %s", shop.getLocation()));
            } else {
                shopLocation.setText(shop.getLocation());
            }
        } else if (shop.getLocation() != null && !shop.getLocation().isEmpty()) {
            shopLocation.setVisibility(View.VISIBLE);
            shopLocation.setText(shop.getLocation());
        } else {
            shopLocation.setVisibility(View.GONE);
        }
    }

    private void bindProduct(final Product product) {
        productImage.setImage(product.getImage());
        productImage.setViewHintListener(new ImpressedImageView.ViewHintListener() {
            @Override
            public void onViewHint() {
                if(impressionListener!=null){
                    impressionListener.onImpressionProductAdsItem((clickPosition < 0 ?
                            getAdapterPosition() : clickPosition), product);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            productName.setText(Html.fromHtml(product.getName(),
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            productName.setText(Html.fromHtml(product.getName()));
        }
        productPrice.setText(product.getPriceFormat());

        if (data.getProduct().getProductRating() == 0) {
            rating.setVisibility(View.GONE);
            reviewCount.setVisibility(View.GONE);
            if (data.getProduct().isProductNewLabel()) {
                newLabelTxt.setVisibility(View.VISIBLE);
            } else {
                newLabelTxt.setVisibility(View.GONE);
            }
        } else {
            newLabelTxt.setVisibility(View.GONE);
            rating.setVisibility(View.VISIBLE);
            reviewCount.setVisibility(View.VISIBLE);
            rating.setImageResource(
                    ImageLoader.getRatingDrawable(getStarCount(data.getProduct().getProductRating()))
            );
            reviewCount.setText("(" + data.getProduct().getCountReviewFormat() + ")");
        }
        topLabelContainer.removeAllViews();
        if (product.getTopLabels() != null) {
            for (String l : product.getTopLabels()) {
                TextView label = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_top_label,
                        null, false);
                label.setText(l);
                topLabelContainer.addView(label);
            }
        }
        bottomLabelContainer.removeAllViews();
        if (product.getBottomLabels() != null) {
            for (String l : product.getBottomLabels()) {
                TextView label = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_bottom_label,
                        null, false);
                label.setText(l);
                bottomLabelContainer.addView(label);
            }
        }
        renderWishlistButton(data.getProduct().isWishlist());
    }

    private int getStarCount(int rating) {
        return Math.round(rating / 20f);
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            if (v.getId() == R.id.container) {
                itemClickListener.onProductItemClicked((clickPosition < 0 ? getAdapterPosition() : clickPosition), data);
            }
            if (v.getId() == R.id.wishlist_button_container) {
                itemClickListener.onAddWishLish((clickPosition < 0 ? getAdapterPosition() : clickPosition), data);
            }
        }
    }

    protected void renderWishlistButton(boolean wishlist) {
        if (wishlist) {
            btnWishList.setBackgroundResource(R.drawable.ic_wishlist_red);
        } else {
            btnWishList.setBackgroundResource(R.drawable.ic_wishlist);
        }
    }

    private boolean isBadgesExist(List<Badge> badges) {
        if (badges == null || badges.isEmpty()) {
            return false;
        }

        for (Badge badgeItem : badges) {
            if (badgeItem.isShow()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPositionChange(int position) {
        this.clickPosition = position;
    }
}
