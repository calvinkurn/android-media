package com.tokopedia.topads.sdk.view.adapter.viewholder.feednew;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.RoundedCornerImageView;
import com.tokopedia.topads.sdk.view.adapter.FeedShopAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ShopFeedNewViewModel;

import java.util.List;

/**
 * @author by milhamj on 29/03/18.
 */

public class ShopFeedNewViewHolder extends AbstractViewHolder<ShopFeedNewViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_shop_feed_new;

    private static final int SPAN_COUNT = 3;

    private Context context;
    private ImageLoader imageLoader;
    private LocalAdsClickListener itemClickListener;
    private int adapterPosition = 0;
    private RoundedCornerImageView shopImage;
    private TextView shopTitle;
    private TextView shopSubtitle;
    private RecyclerView productListRv;
    private FrameLayout favoriteButton;
    private TextView favoriteText;
    private FeedShopAdapter adapter;
    private View.OnClickListener shopItemClickListener;

    private Data data;

    public ShopFeedNewViewHolder(View itemView, ImageLoader imageLoader,
                                 LocalAdsClickListener itemClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.imageLoader = imageLoader;
        this.itemClickListener = itemClickListener;
        View header = itemView.findViewById(R.id.header);
        shopImage = itemView.findViewById(R.id.shop_image);
        shopTitle = itemView.findViewById(R.id.shop_title);
        shopSubtitle = itemView.findViewById(R.id.shop_subtitle);
        favoriteButton = itemView.findViewById(R.id.favorite_button);
        favoriteText = itemView.findViewById(R.id.favorite_text);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        adapter = new FeedShopAdapter(onShopItemClicked());

        productListRv = itemView.findViewById(R.id.product_list);
        productListRv.setLayoutManager(gridLayoutManager);
        productListRv.setAdapter(adapter);

        itemView.setOnClickListener(onShopItemClicked());
        header.setOnClickListener(onShopItemClicked());
        favoriteButton.setOnClickListener(onAddFavorite());
    }

    public View.OnClickListener onShopItemClicked() {
        if (shopItemClickListener == null) {
            shopItemClickListener = v -> itemClickListener.onShopItemClicked(adapterPosition, data);
        }
        return shopItemClickListener;
    }

    private View.OnClickListener onAddFavorite() {
        return v -> itemClickListener.onAddFavorite(adapterPosition, data);
    }

    @Override
    public void bind(ShopFeedNewViewModel element) {
        data = element.getData();
        Shop shop = data.getShop();

        if (shop != null) {
            imageLoader.loadImage(shop, shopImage);
            if (shop.getImageProduct() != null) {
                generateThumbnailImages(shop.getImageProduct());
            }

            Spanned title;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title = Html.fromHtml(shop.getName(), Html.FROM_HTML_MODE_LEGACY);
                shopSubtitle.setText(Html.fromHtml(shop.getCity(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                title = Html.fromHtml(shop.getName());
                shopSubtitle.setText(Html.fromHtml(shop.getCity()));
            }

            Drawable img = context.getResources().getDrawable(R.drawable.ic_toa);
            img.setBounds(0, 0,
                    context.getResources().getDimensionPixelOffset(R.dimen.feed_badge_size),
                    context.getResources().getDimensionPixelOffset(R.dimen.feed_badge_size));

            if (shop.isGoldShopBadge()) {
                setShopBadge(img, GMConstant.getGMDrawableResource(context));
            } else if (shop.isShop_is_official()) {
                setShopBadge(img, R.drawable.ic_official);
            } else {
                shopTitle.setCompoundDrawables(null, null, img, null);
            }

            shopTitle.setText(title);
            setFavorite(data.isFavorit());
        }
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public void onViewRecycled() {
        ImageLoader.clearImage(shopImage);

        if (adapter == null || productListRv == null) {
            return;
        }
        for (int i = 0; i < adapter.getItemCount(); i++) {
            RecyclerView.ViewHolder holder = productListRv.findViewHolderForAdapterPosition(i);
            if (holder instanceof FeedShopAdapter.ViewHolder) {
                adapter.onViewRecycled((FeedShopAdapter.ViewHolder) holder);
            }
        }
    }

    private void setShopBadge(Drawable topadsBadge, int drawable) {
        Drawable shopBadge = context.getResources().getDrawable(drawable);
        shopBadge.setBounds(0, 0,
                context.getResources().getDimensionPixelOffset(R.dimen.feed_badge_size),
                context.getResources().getDimensionPixelOffset(R.dimen.feed_badge_size));
        shopTitle.setCompoundDrawables(shopBadge, null, topadsBadge, null);
    }

    private void setFavorite(boolean isFavorite) {
        String text;
        Drawable drawable;
        if (isFavorite) {
            favoriteButton.setSelected(true);
            text = context.getString(R.string.topads_following);
            drawable = AppCompatResources.getDrawable(context, R.drawable.ic_check_favorite);
            favoriteText.setTextColor(ContextCompat.getColor(context, R.color.label_color));
        } else {
            favoriteButton.setSelected(false);
            text = context.getString(R.string.topads_follow);
            drawable = AppCompatResources.getDrawable(context, R.drawable.ic_add_white_24px);
            favoriteText.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        if (drawable != null) {
            drawable.setBounds(0, 0,
                    context.getResources().getDimensionPixelOffset(R.dimen.feed_fav_icon),
                    context.getResources().getDimensionPixelOffset(R.dimen.feed_fav_icon));
        }
        favoriteText.setCompoundDrawables(drawable, null, null, null);
        favoriteText.setText(text);

        favoriteButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver viewTreeObserver = favoriteButton.getViewTreeObserver();
                viewTreeObserver.removeOnGlobalLayoutListener(this);

                favoriteButton.setMinimumWidth(favoriteButton.getWidth());
            }
        });
    }

    private void generateThumbnailImages(List<ImageProduct> imageProducts) {
        adapter.setList(imageProducts);
    }
}
