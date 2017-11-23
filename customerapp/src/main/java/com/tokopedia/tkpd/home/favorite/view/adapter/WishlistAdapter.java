package com.tokopedia.tkpd.home.favorite.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.WishlistItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Kulomady on 1/27/17.
 */

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<WishlistItem> data;

    public WishlistAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_product_item_small, parent, false);
        return new WishlistAdapter.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(WishlistAdapter.ViewHolder holder, int position) {
        WishlistItem wishlistItem = data.get(position);
        holder.productName.setText(Html.fromHtml(wishlistItem.getName()));
        holder.productPrice.setText(wishlistItem.getPrice());
        holder.shopName.setText(wishlistItem.getShopName());
        ImageHandler.loadImageFit2(holder.getContext(),
                holder.productImage, wishlistItem.getProductImage());
        setBadges(holder, wishlistItem.getBadgeImageUrl());
        holder.productLayout.setOnClickListener(onProductClicked(wishlistItem));
    }

    @Override
    public int getItemCount() {
        final int maxWishlistInAdapter = 4;
        if (data.size() > maxWishlistInAdapter) return maxWishlistInAdapter;
        else return data.size();
    }


    private View.OnClickListener onProductClicked(final WishlistItem item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventFavoriteView(item.getName());
                Context context = view.getContext();
                Intent intent
                        = ProductDetailRouter
                        .createInstanceProductDetailInfoActivity(context, item.getProductId());
                context.startActivity(intent);
            }
        };
    }

    public void setData(List<WishlistItem> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    private void setBadges(WishlistAdapter.ViewHolder holder, List<String> stringList) {
        holder.badgesContainer.removeAllViews();
        for (String bagdeUrl : stringList) {
            LuckyShopImage.loadImage(holder.getContext(), bagdeUrl, holder.badgesContainer);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_name)
        TextView productName;
        @BindView(R.id.product_price)
        TextView productPrice;
        @BindView(R.id.product_shop)
        TextView shopName;
        @BindView(R.id.product_image)
        ImageView productImage;
        @BindView(R.id.main_content)
        LinearLayout productLayout;
        @BindView(R.id.badges_container)
        LinearLayout badgesContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }

}