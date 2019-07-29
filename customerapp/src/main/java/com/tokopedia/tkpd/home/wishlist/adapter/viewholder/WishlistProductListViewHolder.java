package com.tokopedia.tkpd.home.wishlist.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.presenter.WishListView;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistProductViewModel;
import com.tokopedia.tkpd.home.wishlist.analytics.WishlistAnalytics;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistProductListViewHolder extends AbstractViewHolder<WishlistProductViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.listview_product_item;
    private final WishlistAnalytics wishlistAnalytics;
    private final WishListView wishlistView;
    private final Context context;
    private TextView locationTxt;
    private TextView buyWishlistBtn;
    private View wishlistBtn;
    private RelativeLayout container;

    public WishlistProductListViewHolder(View itemView, WishlistAnalytics wishlistAnalytics, WishListView wishlistView) {
        super(itemView);
        this.wishlistAnalytics = wishlistAnalytics;
        this.wishlistView = wishlistView;
        this.context = itemView.getContext();
        this.locationTxt = itemView.findViewById(R.id.location);
        this.buyWishlistBtn = itemView.findViewById(R.id.buy_button);
        this.wishlistBtn = itemView.findViewById(R.id.wishlist);
        this.container = itemView.findViewById(R.id.container);
    }

    @Override
    public void bind(WishlistProductViewModel element) {
        ProductItem product = element.getProductItem();
        ImageHandler.loadImageFit2(context, itemView.findViewById(R.id.product_image), element.getProductItem().imgUri);
        ((TextView) itemView.findViewById(R.id.title)).setText(Html.fromHtml(product.name));
        ((TextView) itemView.findViewById(R.id.shop_name)).setText(Html.fromHtml(product.shop));
        ((TextView) itemView.findViewById(R.id.price)).setText(product.price);

        if (product.getOfficial()){
            locationTxt.setCompoundDrawablesWithIntrinsicBounds(com.tokopedia.core2.R.drawable.ic_icon_authorize_grey, 0, 0, 0);
            locationTxt.setText(context.getResources().getString(com.tokopedia.core2.R.string.authorized));
        } else{
            locationTxt.setCompoundDrawablesWithIntrinsicBounds(com.tokopedia.core2.R.drawable.ic_icon_location_grey_wishlist, 0, 0, 0);
            locationTxt.setText(product.getShopLocation());
        }
        setBadges(product);
        setLabels(product);
        container.setOnClickListener(onProductItemClicked(product, getAdapterPosition()));
        if (product.getIsWishlist()){
            wishlistBtn.setVisibility(View.VISIBLE);
            wishlistBtn.setOnClickListener(onDeleteWishlistClicked(product.getId(), getAdapterPosition()));
            if (product.getIsAvailable()){
                setBuyButtonAvailable(buyWishlistBtn);
                buyWishlistBtn.setOnClickListener(onBuyButtonClicked(product.getId()));
            } else{
                setBuyButtonUnavailable(buyWishlistBtn);
                buyWishlistBtn.setOnClickListener(null);
            }
        }
    }

    private int getColor(Context context, int id) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private void setBuyButtonUnavailable(TextView buyButtonUnavailable) {
        buyButtonUnavailable.setBackgroundResource(R.drawable.btn_buy_unvailable);
        buyButtonUnavailable.setTextColor(getColor(context, R.color.grey_700));
        buyButtonUnavailable.setText(R.string.title_empty_stock);
    }

    private void setBuyButtonAvailable(TextView buyButtonUnavailable) {
        buyButtonUnavailable.setBackgroundResource(R.drawable.rect_orange);
        buyButtonUnavailable.setTextColor(getColor(context, R.color.white));
        buyButtonUnavailable.setText(R.string.title_buy);
    }

    private View.OnClickListener onDeleteWishlistClicked(String productId, int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wishlistView != null)
                    wishlistView.displayDeleteWishlistDialog(productId, position);
            }
        };
    }

    private View.OnClickListener onBuyButtonClicked(String productId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventWishlistBuy(v.getContext());
                if (wishlistView != null) wishlistView.displayAddToCart(productId);
            }
        };
    }

    private View.OnClickListener onProductItemClicked(ProductItem data, int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventWishlistView(v.getContext(), data.name);
                wishlistAnalytics.trackEventClickOnProductWishlist(String.valueOf((position + 1)), data.getProductAsObjectDataLayerForWishlistClick(position + 1));
                Intent intent = getProductIntent(data.getId());
                context.startActivity(intent);
            }
        };
    }

    private Intent getProductIntent(String productId) {
        if (context != null) {
            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    private void setLabels(ProductItem data) {
        FlowLayout container = itemView.findViewById(R.id.label_container);
        container.removeAllViews();
        if (data.getLabels() != null) {
            for (Label label : data.getLabels()) {
                View view = LayoutInflater.from(context).inflate(R.layout.label_layout, null);
                TextView labelText = view.findViewById(R.id.label);
                labelText.setText(label.getTitle());
                if (label.getColor().toLowerCase() != context.getString(R.string.white_hex_color)) {
                    labelText.setBackgroundResource(R.drawable.bg_label);
                    labelText.setTextColor(ContextCompat.getColor(context, R.color.white));
                    ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        labelText.setBackgroundTintList(tint);
                    } else {
                        ViewCompat.setBackgroundTintList(labelText, tint);
                    }
                }
                container.addView(view);
            }
        }
    }

    private void setBadges(ProductItem data) {
        LinearLayout container = itemView.findViewById(R.id.badges_container);
        container.removeAllViews();
        if (data.getBadges() != null) {
            for (Badge badges : data.getBadges()) {
                LuckyShopImage.loadImage(context, badges.getImageUrl(), container);
            }
        }
    }
}
