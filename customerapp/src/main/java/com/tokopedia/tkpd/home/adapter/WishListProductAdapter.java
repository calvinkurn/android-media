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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.network.entity.home.recentView.RecentView;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.presenter.WishListView;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 16/06/15.
 */
public class WishListProductAdapter extends BaseRecyclerViewAdapter {

    private static final String TAG = WishListProductAdapter.class.getSimpleName();
    private List<RecyclerViewItem> data;
    private Context context;
    private WishListView wishlistView;
    private OnWishlistActionButtonClicked actionButtonClicked;


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

    public WishListProductAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
        this.context = context;
        this.data = data;

    }

    public void setActionButtonClicked(OnWishlistActionButtonClicked actionButtonClicked) {
        this.actionButtonClicked = actionButtonClicked;
    }

    public void setWishlistView(WishListView view) {
        wishlistView = view;
    }


    public void setSearchNotFound() {
        data.add(new EmptySearchItem());
    }

    public void setEmptyState() {
        data.add(new EmptyStateItem());
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
            case TkpdState.RecyclerView.VIEW_EMPTY_SEARCH:
                return createEmptySearch(viewGroup);
            case TkpdState.RecyclerView.VIEW_EMPTY_STATE:
                return createEmptyState(viewGroup);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    public static class EmptyStateItem extends RecyclerViewItem {
        public EmptyStateItem() {
            setType(TkpdState.RecyclerView.VIEW_EMPTY_STATE);
        }
    }

    public static class EmptySearchItem extends RecyclerViewItem {
        public EmptySearchItem() {
            setType(TkpdState.RecyclerView.VIEW_EMPTY_SEARCH);
        }
    }

    public RecyclerView.ViewHolder createEmptyState(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wishlist_empty_state, null);
        return new EmptyViewHolder(view, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionButtonClicked != null) {
                    actionButtonClicked.findProduct();
                }
            }
        });
    }

    public RecyclerView.ViewHolder createEmptySearch(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wishlist_empty_search, null);
        return new EmptyViewHolder(view, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionButtonClicked != null) {
                    actionButtonClicked.showAllWishlist();
                }
            }
        });
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder implements
            TopAdsItemClickListener {
        @BindView(R.id.topads)
        TopAdsView topAdsView;
        @BindView(R.id.action_btn)
        Button actionBtn;
        private Context context;
        private final String WISHLISH_SRC = "wishlist";

        public EmptyViewHolder(View itemView, View.OnClickListener clickListener) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
            TopAdsParams params = new TopAdsParams();
            params.getParam().put(TopAdsParams.KEY_SRC, WISHLISH_SRC);
            Config topAdsconfig = new Config.Builder()
                    .setSessionId(GCMHandler.getRegistrationId(context))
                    .setUserId(SessionHandler.getLoginID(context))
                    .withPreferedCategory()
                    .displayMode(DisplayMode.FEED)
                    .setEndpoint(Endpoint.PRODUCT)
                    .topAdsParams(params)
                    .build();
            topAdsView.setConfig(topAdsconfig);
            topAdsView.setDisplayMode(DisplayMode.FEED);
            topAdsView.setMaxItems(4);
            topAdsView.setAdsItemClickListener(this);
            actionBtn.setOnClickListener(clickListener);
        }

        public void loadTopAds() {
            topAdsView.loadTopAds();
        }

        @Override
        public void onProductItemClicked(Product product) {
            ProductItem data = new ProductItem();
            data.setId(product.getId());
            data.setName(product.getName());
            data.setPrice(product.getPriceFormat());
            data.setImgUri(product.getImage().getM_url());
            Bundle bundle = new Bundle();
            Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context);
            bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

        @Override
        public void onShopItemClicked(Shop shop) {
            Intent intent = ShopPageActivity.createIntent(context, shop.getId());
            context.startActivity(intent);
        }

        @Override
        public void onAddFavorite(int position, Data data) {

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
            case TkpdState.RecyclerView.VIEW_EMPTY_SEARCH:
            case TkpdState.RecyclerView.VIEW_EMPTY_STATE:
                ((EmptyViewHolder) viewHolder).loadTopAds();
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
        }
    }

    private void bindProductViewHolder(ViewHolder viewHolder, int position) {
        if (data.get(position) !=null && data.get(position) instanceof ProductItem) {
            ProductItem product = (ProductItem) data.get(position);
            viewHolder.productName.setText(Html.fromHtml(product.name));
            viewHolder.productPrice.setText(product.price);
            viewHolder.shopName.setText(Html.fromHtml(product.shop));
            if (product.getOfficial()) {
                viewHolder.location.setCompoundDrawablesWithIntrinsicBounds(com.tokopedia.core.R.drawable.ic_icon_authorize_grey, 0, 0, 0);
                viewHolder.location.setText(context.getResources().getString(com.tokopedia.core.R.string.authorized));
            } else {
                viewHolder.location.setCompoundDrawablesWithIntrinsicBounds(com.tokopedia.core.R.drawable.ic_icon_location_grey, 0, 0, 0);
                viewHolder.location.setText(product.getShopLocation());
            }
            setProductImage(viewHolder, product.getImgUri());
            setBadges(viewHolder, product);
            setLabels(viewHolder, product);
            viewHolder.mainContent.setOnClickListener(onProductItemClicked(position));
        } else if (data.get(position) !=null && data.get(position) instanceof RecentView) {
            RecentView product = (RecentView) data.get(position);
            viewHolder.productName.setText(Html.fromHtml(product.getProductName()));
            viewHolder.productPrice.setText(product.getProductPrice());
            viewHolder.shopName.setText(Html.fromHtml(product.getShopName()));
            viewHolder.location.setText(product.getShopLocation());
            setProductImage(viewHolder, product.getProductImage());
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
        if (product.getOfficial()) {
            viewHolder.location.setCompoundDrawablesWithIntrinsicBounds(com.tokopedia.core.R.drawable.ic_icon_authorize_grey, 0, 0, 0);
            viewHolder.location.setText(context.getResources().getString(com.tokopedia.core.R.string.authorized));
        } else {
            viewHolder.location.setCompoundDrawablesWithIntrinsicBounds(com.tokopedia.core.R.drawable.ic_icon_location_grey, 0, 0, 0);
            viewHolder.location.setText(product.getShopLocation());
        }
        setProductImage(viewHolder, product.getImgUri());
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
                    UnifyTracking.eventWishlistView(product.getName());

                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, ProductInfoActivity.class);
                    bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else if (data.get(position) instanceof RecentView) {
                    RecentView product = (RecentView) data.get(position);
                    UnifyTracking.eventWishlistView(product.getProductName());
                    context.startActivity(
                            ProductDetailRouter.createInstanceProductDetailInfoActivity(
                                    context, getProductDataToPass((RecentView) data.get(position))
                            )
                    );
                }
            }
        };
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
                if (!label.getColor().toLowerCase().equals(context.getString(R.string.white_hex_color))) {
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
                if (!label.getColor().toLowerCase().equals(context.getString(R.string.white_hex_color))) {
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
        if (data.get(position) instanceof EmptySearchItem) {
            return TkpdState.RecyclerView.VIEW_EMPTY_SEARCH;
        }
        if (data.get(position) instanceof EmptyStateItem) {
            return TkpdState.RecyclerView.VIEW_EMPTY_STATE;
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

    private ProductPass getProductDataToPass(RecentView recentView) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(recentView.getProductPrice())
                .setProductId(recentView.getProductId().toString())
                .setProductName(recentView.getProductName())
                .setProductImage(recentView.getProductImage())
                .build();
    }

    public interface OnWishlistActionButtonClicked {
        void showAllWishlist();

        void findProduct();
    }

}