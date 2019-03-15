package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.network.entity.home.recentView.RecentView;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.presenter.WishListView;
import java.util.ArrayList;
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
        public FlowLayout labelContainer;
        public RelativeLayout mainContent;
        public LinearLayout wishlistContent;
        public TextView productName;
        public TextView productPrice;
        public TextView shopName;
        public ImageView productImage;
        public ImageView deleteWishlistBut;
        public TextView buyWishlistBut;
        public TextView location;


        public ViewHolder(View itemView) {
            super(itemView);
            badgeContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
            labelContainer = (FlowLayout) itemView.findViewById(R.id.label_container);
            mainContent = (RelativeLayout) itemView.findViewById(R.id.container);
            productName = (TextView) itemView.findViewById(R.id.title);
            productPrice = (TextView) itemView.findViewById(R.id.price);
            shopName = (TextView) itemView.findViewById(R.id.shop_name);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            wishlistContent = (LinearLayout) itemView.findViewById(R.id.wishlist);
            buyWishlistBut = (TextView) itemView.findViewById(R.id.buy_button);
            deleteWishlistBut = (ImageView) itemView.findViewById(R.id.delete_but);
            location = (TextView) itemView.findViewById(R.id.location);
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
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
        }
    }

    private void bindProductViewHolder(ViewHolder viewHolder, int position) {
        if (data.get(position) instanceof ProductItem) {
            ProductItem product = (ProductItem) data.get(position);
            viewHolder.productName.setText(Html.fromHtml(product.name));
            viewHolder.productPrice.setText(product.price);
            viewHolder.shopName.setText(Html.fromHtml(product.shop));
            viewHolder.location.setText(product.getShopLocation());
            setProductImage(viewHolder, product.getImgUri());
            if (product.labels == null) {
                product.labels = new ArrayList<Label>();
                if (product.preorder != null && product.preorder.equals("1")) {
                    Label label = new Label();
                    label.setTitle(context.getString(R.string.preorder));
                    label.setColor(context.getString(R.string.white_hex_color));
                    product.labels.add(label);
                }
                if (product.wholesale != null && product.wholesale.equals("1")) {
                    Label label = new Label();
                    label.setTitle(context.getString(R.string.grosir));
                    label.setColor(context.getString(R.string.white_hex_color));
                    product.labels.add(label);
                }
            }
            setBadges(viewHolder, product);
            setLabels(viewHolder, product);
            viewHolder.mainContent.setOnClickListener(onProductItemClicked(position));
        } else if (data.get(position) instanceof RecentView) {
            RecentView product = (RecentView) data.get(position);
            viewHolder.productName.setText(Html.fromHtml(product.getProductName()));
            viewHolder.productPrice.setText(product.getProductPrice());
            viewHolder.shopName.setText(Html.fromHtml(product.getShopName()));
            viewHolder.location.setText(product.getShopLocation());
            setProductImage(viewHolder, product.getProductImage());
            if (product.getLabels() == null) {
                product.setLabels(new ArrayList<com.tokopedia.core.network.entity.home.recentView.Label>());
                if (product.getProductPreorder() != null && product.getProductPreorder().equals("1")) {
                    com.tokopedia.core.network.entity.home.recentView.Label label
                            = new com.tokopedia.core.network.entity.home.recentView.Label();

                    label.setTitle(context.getString(R.string.preorder));
                    label.setColor(context.getString(R.string.white_hex_color));
                    product.getLabels().add(label);
                }
                if (product.getProductWholesale() != null && product.getProductWholesale().equals("1")) {
                    com.tokopedia.core.network.entity.home.recentView.Label label
                            = new com.tokopedia.core.network.entity.home.recentView.Label();

                    label.setTitle(context.getString(R.string.grosir));
                    label.setColor(context.getString(R.string.white_hex_color));
                    product.getLabels().add(label);
                }
            }
            setBadgesRecentView(viewHolder, product);
            setLabelsRecentView(viewHolder, product);
            viewHolder.mainContent.setOnClickListener(onProductItemClicked(position));
        }
    }

    private void bindWishlistViewHolder(ViewHolder viewHolder, int position) {
        ProductItem product = (ProductItem) data.get(position);
        viewHolder.productName.setText(Html.fromHtml(product.name));
        viewHolder.productPrice.setText(product.price);
        viewHolder.shopName.setText(Html.fromHtml(product.shop));
        viewHolder.location.setText(product.getShopLocation());
        setProductImage(viewHolder, product.getImgUri());
        if (product.labels == null) {
            product.labels = new ArrayList<Label>();
            if (product.preorder != null && product.preorder.equals("1")) {
                Label label = new Label();
                label.setTitle(context.getString(R.string.preorder));
                label.setColor(context.getString(R.string.white_hex_color));
                product.labels.add(label);
            }
            if (product.wholesale != null && product.wholesale.equals("1")) {
                Label label = new Label();
                label.setTitle(context.getString(R.string.grosir));
                label.setColor(context.getString(R.string.white_hex_color));
                product.labels.add(label);
            }
        }
        if (product.badges == null) {
            product.badges = new ArrayList<>();
            if (product.isGold != null && product.isGold.equals("1")) {
                Badge badge = new Badge();
                badge.setImageUrl("https://ecs7.tokopedia.net/img/gold-active-large.png");
                product.badges.add(badge);
            }
            if (product.luckyShop != null && !product.luckyShop.isEmpty()) {
                Badge badge = new Badge();
                badge.setImageUrl(product.luckyShop);
                product.badges.add(badge);
            }
        }
        setBadges(viewHolder, product);
        setLabels(viewHolder, product);

        if (product.getIsWishlist()) {
            viewHolder.wishlistContent.setVisibility(View.VISIBLE);
            viewHolder.deleteWishlistBut.setOnClickListener(onDeleteWishlistClicked(product.getId(), position));

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
                if (data.get(position) instanceof ProductItem) {
                    ProductItem product = (ProductItem) data.get(position);
                    UnifyTracking.eventWishlistView(view.getContext(), product.getName());

                    Bundle bundle = new Bundle();
                    Intent intent
                            = getProductIntent(((ProductItem) data.get(position)).id);
                    context.startActivity(intent);
                } else if (data.get(position) instanceof RecentView) {
                    RecentView product = (RecentView) data.get(position);
                    UnifyTracking.eventWishlistView(view.getContext(), product.getProductName());
                    context.startActivity(
                            getProductIntent(((RecentView) data.get(position)).getProductId().toString())
                    );
                }
            }
        };
    }

    private Intent getProductIntent(String productId){
        if (context != null) {
            return RouteManager.getIntent(context,
                    UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId));
        } else {
            return null;
        }
    }

    private void setBadges(ViewHolder holder, ProductItem data) {
        holder.badgeContainer.removeAllViews();
        if (data.getBadges() != null) {
            for (Badge badges : data.getBadges()) {
                LuckyShopImage.loadImage(context, badges.getImageUrl(), holder.badgeContainer);
            }
        }
    }

    private void setProductImage(ViewHolder holder, String imageUrl) {
        ImageHandler.loadImageFit2(holder.getContext(), holder.productImage, imageUrl);
    }

    private void setBadgesRecentView(ViewHolder holder, RecentView data) {
        holder.badgeContainer.removeAllViews();
        if (data.getBadges() != null && holder.badgeContainer.getChildCount() == 0) {
            for (com.tokopedia.core.network.entity.home.recentView.Badge badges : data.getBadges()) {
                LuckyShopImage.loadImage(context, badges.getImageUrl(), holder.badgeContainer);
            }
        }
    }


    private void setLabels(ViewHolder holder, ProductItem data) {
        holder.labelContainer.removeAllViews();
        if (data.getLabels() != null) {
            for (Label label : data.getLabels()) {
                View view = LayoutInflater.from(context).inflate(R.layout.label_layout, null);
                TextView labelText = (TextView) view.findViewById(R.id.label);
                labelText.setText(label.getTitle());
                if (!label.getColor().toLowerCase().equals(context.getString(R.color.white))) {
                    labelText.setBackgroundResource(R.drawable.bg_label);
                    labelText.setTextColor(ContextCompat.getColor(context, R.color.white));
                    ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        labelText.setBackgroundTintList(tint);
                    } else {
                        ViewCompat.setBackgroundTintList(labelText, tint);
                    }
                }
                holder.labelContainer.addView(view);
            }
        }
    }

    private void setLabelsRecentView(ViewHolder holder, RecentView data) {
        holder.labelContainer.removeAllViews();
        if (data.getLabels() != null) {
            for (com.tokopedia.core.network.entity.home.recentView.Label label : data.getLabels()) {
                View view = LayoutInflater.from(context).inflate(R.layout.label_layout, null);
                TextView labelText = (TextView) view.findViewById(R.id.label);
                labelText.setText(label.getTitle());
                if (!label.getColor().toLowerCase().equals("#ffffff")) {
                    labelText.setBackgroundResource(R.drawable.bg_label);
                    labelText.setTextColor(ContextCompat.getColor(context, R.color.white));
                    ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        labelText.setBackgroundTintList(tint);
                    } else {
                        ViewCompat.setBackgroundTintList(labelText, tint);
                    }
                }
                holder.labelContainer.addView(view);
            }
        }
    }

    private void setProductImageWoFit(ViewHolder holder, ProductItem product) {
        ImageHandler.LoadImage(holder.productImage, product.imgUri);
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && (!isLastItemPosition(position) || position < data.size())) {
            if (data.get(position) instanceof ProductItem) {
                ProductItem product = (ProductItem) data.get(position);
                if (product.getIsWishlist()) {
                    return TkpdState.RecyclerView.VIEW_WISHLIST;
                }
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

    private View.OnClickListener onDeleteWishlistClicked(final String productId, final int position) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (wishlistView != null) wishlistView.displayDeleteWishlistDialog(productId, position);
            }
        };
    }

    private View.OnClickListener onBuyButtonClicked(final String productId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventWishlistBuy(v.getContext());
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
        buyButtonUnavailable.setBackgroundResource(R.drawable.rect_orange);
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

    private ProductPass getProductDataToPass(RecentView recentView) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(recentView.getProductPrice())
                .setProductId(recentView.getProductId().toString())
                .setProductName(recentView.getProductName())
                .setProductImage(recentView.getProductImage())
                .build();
    }

}