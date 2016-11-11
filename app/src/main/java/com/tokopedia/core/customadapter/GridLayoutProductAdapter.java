package com.tokopedia.core.customadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.tokopedia.core.home.presenter.WishListView;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.R;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

/**
 * Created by Nisie on 16/06/15.
 */
public class GridLayoutProductAdapter extends BaseRecyclerViewAdapter {

    private static final String TAG = GridLayoutProductAdapter.class.getSimpleName();
    private List<RecyclerViewItem> data;
    private Context context;
    private WishListView wishlistView;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout badgeContainer;
        public LinearLayout mainContent;
        public LinearLayout wishlistContent;
        public TextView productName;
        public TextView productPrice;
        public TextView shopName;
        public ImageView productImage;
        public View preorder;
        public View grosir;
        public ImageView deleteWishlistBut;
        public TextView buyWishlistBut;


        public ViewHolder(View itemView) {
            super(itemView);
            badgeContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
            mainContent = (LinearLayout) itemView.findViewById(R.id.main_content);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            shopName = (TextView) itemView.findViewById(R.id.product_shop);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            grosir = itemView.findViewById(R.id.grosir);
            preorder = itemView.findViewById(R.id.preorder);
            wishlistContent = (LinearLayout) itemView.findViewById(R.id.wishlist);
            buyWishlistBut = (TextView) itemView.findViewById(R.id.buy_button);
            deleteWishlistBut = (ImageView) itemView.findViewById(R.id.delete_but);
//            freeReturns = (ImageView) itemView.findViewById(R.id.prod_free_return);
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }

    public GridLayoutProductAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
        this.context = context;
        this.data = data;

    }

    public void setWishlistView(WishListView view) {
        wishlistView = view;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return createProductView(viewGroup);
            case TkpdState.RecyclerView.VIEW_PRODUCT_RIGHT:
                return createProductView(viewGroup);
            case TkpdState.RecyclerView.VIEW_WISHLIST:
                return createProductView(viewGroup);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    private ViewHolder createProductView(ViewGroup viewGroup) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_product_item, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                bindProductViewHolder((ViewHolder) viewHolder, position);
                break;
            case TkpdState.RecyclerView.VIEW_PRODUCT_RIGHT:
                bindProductViewHolder((ViewHolder) viewHolder, position);
                break;
            case TkpdState.RecyclerView.VIEW_WISHLIST:
                bindWishlistViewHolder((ViewHolder) viewHolder, position);
            default:
                super.onBindViewHolder(viewHolder, position);
        }
    }

    private void bindProductViewHolder(ViewHolder viewHolder, int position) {
        ProductItem product = (ProductItem) data.get(position);
        viewHolder.productName.setText(Html.fromHtml(product.name));
        viewHolder.productPrice.setText(product.price);
        viewHolder.shopName.setText(Html.fromHtml(product.shop));
        setProductImage(viewHolder, product);
        setWholesale(viewHolder, product);
        setPreorder(viewHolder, product);
        setBadges(viewHolder, product);
        viewHolder.mainContent.setOnClickListener(onProductItemClicked(position));
    }

    private void bindWishlistViewHolder(ViewHolder viewHolder, int position) {
        ProductItem product = (ProductItem) data.get(position);
        viewHolder.productName.setText(Html.fromHtml(product.name));
        viewHolder.productPrice.setText(product.price);
        viewHolder.shopName.setText(Html.fromHtml(product.shop));
        setProductImage(viewHolder, product);
        setWholesale(viewHolder, product);
        setPreorder(viewHolder, product);
        setBadges(viewHolder, product);

        if (product.getIsWishlist()) {
            viewHolder.wishlistContent.setVisibility(View.VISIBLE);
            viewHolder.deleteWishlistBut.setOnClickListener(onDeleteWishlistClicked(product.getId()));

            if (product.getIsAvailable()) {
                setBuyButtonAvailable(viewHolder.buyWishlistBut);
                viewHolder.buyWishlistBut.setOnClickListener(onBuyButtonClicked(product.getId()));
            } else {
                setBuyButtonUnavailable(viewHolder.buyWishlistBut);
                viewHolder.buyWishlistBut.setOnClickListener(null);
            }
        }


        viewHolder.mainContent.setOnClickListener(onProductItemClicked(position));
    }

    private View.OnClickListener onProductItemClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductItem product = (ProductItem) data.get(position);
                UnifyTracking.eventWishlistView(product.getName());

                Bundle bundle = new Bundle();
                Intent intent = new Intent(context, ProductInfoActivity.class);
                bundle.putString("product_id", product.id);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };
    }

    private void setBadges(ViewHolder holder, ProductItem data) {
        holder.badgeContainer.removeAllViews();
        if (data.getBadges() != null)
            for (ProductItem.Badge badges : data.getBadges()) {
                View view = LayoutInflater.from(context).inflate(R.layout.badge_layout, null);
                ImageView imageBadge = (ImageView) view.findViewById(R.id.badge);
                holder.badgeContainer.addView(view);
                LuckyShopImage.loadImage(imageBadge, badges.getImageUrl());
            }
    }

    private void setProductImage(ViewHolder holder, ProductItem product) {
        ImageHandler.loadImageFit2(holder.getContext(), holder.productImage, product.imgUri);
    }

    private void setWholesale(ViewHolder holder, ProductItem data) {
        if (data.wholesale != null)
            holder.grosir.setVisibility(data.wholesale.equals("0") ? View.GONE : View.VISIBLE);
        else
            holder.grosir.setVisibility(View.GONE);
    }

    private void setPreorder(ViewHolder holder, ProductItem data) {
        if (data.preorder != null)
            holder.preorder.setVisibility(data.preorder.equals("0") ? View.INVISIBLE : View.VISIBLE);
        else
            holder.preorder.setVisibility(View.INVISIBLE);
    }

    private void setProductImageWoFit(ViewHolder holder, ProductItem product) {
        ImageHandler.LoadImage(holder.productImage, product.imgUri);
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && (!isLastItemPosition(position) || position < data.size())) {
            ProductItem product = (ProductItem) data.get(position);
            if (product.getIsWishlist()) {
                return TkpdState.RecyclerView.VIEW_WISHLIST;
            }
        }

        if (isLastItemPosition(position) || data.size() == 0) {
            return super.getItemViewType(position);
        }
        if (isRightMostProduct(position)) {
            return TkpdState.RecyclerView.VIEW_PRODUCT_RIGHT;
        }

        return TkpdState.RecyclerView.VIEW_PRODUCT;
    }

    private boolean isRightMostProduct(int position) {
        return (position + 1) % 2 == 0;
    }

    private View.OnClickListener onDeleteWishlistClicked(final String productId) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (wishlistView != null) wishlistView.displayDeleteWishlistDialog(productId);
            }
        };
    }

    private View.OnClickListener onBuyButtonClicked(final String productId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventWishlistBuy();
                if (wishlistView != null) wishlistView.displayAddToCart(productId);
            }
        };
    }

    private void setBuyButtonUnavailable(TextView buyButtonUnavailable) {
        buyButtonUnavailable.setBackgroundResource(R.drawable.btn_buy_unvailable);
        buyButtonUnavailable.setTextColor(getColor(context, R.color.grey_700));
        buyButtonUnavailable.setText(R.string.title_empty_stock);
    }

    private void setBuyButtonAvailable(TextView buyButtonUnavailable) {
        buyButtonUnavailable.setBackgroundResource(R.drawable.btn_buy);
        buyButtonUnavailable.setTextColor(getColor(context, R.color.white));
        buyButtonUnavailable.setText(R.string.title_buy);
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}