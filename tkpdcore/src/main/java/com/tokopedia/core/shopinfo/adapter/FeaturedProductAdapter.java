package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.product.model.goldmerchant.FeaturedProductItem;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HenryPri on 15/06/17.
 */

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ViewHolder> {

    private List<FeaturedProductItem> dataList = new ArrayList<>();

    public void setDataList(List<FeaturedProductItem> featuredProductItemList) {
        dataList.clear();
        dataList.addAll(featuredProductItemList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.featured_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_image)
        ImageView productImage;
        @BindView(R2.id.title)
        TextView title;
        @BindView(R2.id.price)
        TextView price;
        @BindView(R2.id.label_container)
        FlowLayout labelContainer;
        @BindView(R2.id.badges_container)
        LinearLayout badgesContainer;
        @BindView(R2.id.wishlist_button)
        ImageView wishlistButton;
        @BindView(R2.id.wishlist_button_container)
        RelativeLayout wishlistButtonContainer;
        @BindView(R2.id.container)
        View container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final FeaturedProductItem data, final int position) {
            wishlistButtonContainer.setVisibility(View.GONE);
            title.setText(data.getName());
            ImageHandler.loadImageThumbs(MainApplication.getAppContext(), productImage, data.getImageUri());
            price.setText(data.getPrice());
        }
    }
}
