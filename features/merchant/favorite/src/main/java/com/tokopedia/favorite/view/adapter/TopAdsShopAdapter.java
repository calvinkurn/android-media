package com.tokopedia.favorite.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.favorite.R;
import com.tokopedia.favorite.utils.TrackingConst;
import com.tokopedia.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;
import java.util.List;


/**
 * @author by erry on 30/01/17.
 */

public class TopAdsShopAdapter extends RecyclerView.Adapter<TopAdsShopAdapter.ViewHolder> {

    protected ScaleAnimation anim;
    private List<TopAdsShopItem> data;
    private FavoriteClickListener favoriteClickListener;
    private Context context;
    private final String PATH_VIEW = "views";
    private ImageLoader imageLoader;
    private static final String className = "com.tokopedia.favorite.view.adapter.TopAdsShopAdapter";

    public TopAdsShopAdapter(FavoriteClickListener favoriteClickListener) {
        this.favoriteClickListener = favoriteClickListener;
        data = new ArrayList<>();
    }

    public void setData(List<TopAdsShopItem> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        imageLoader = new ImageLoader(context);
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.favorite_listview_reccommend_shop, parent, false);
        createScaleAnimation();
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopAdsShopItem shopItem = data.get(position);
        holder.shopName.setText(Html.fromHtml(shopItem.getShopName()));
        holder.shopLocation.setText(Html.fromHtml(shopItem.getShopLocation()));
        imageLoader.loadImage(shopItem.getShopImageUrl(), shopItem.getShopImageEcs(), holder.shopIcon);
        setShopCover(holder, shopItem);
        setFavorite(holder, shopItem);
        holder.mainContent.setOnClickListener(onShopClicked(shopItem));
        holder.favButton.setOnClickListener(
                shopItem.isFav() ? null : onFavClicked(holder, shopItem)
        );
    }


    private void setShopCover(ViewHolder holder, TopAdsShopItem shopItem) {
        if (shopItem.getShopCoverUrl() == null) {
            holder.shopCover.setImageResource(com.tokopedia.abstraction.R.drawable.ic_loading_toped);
        } else {
            Glide.with(context)
                    .load(shopItem.getShopCoverEcs())
                    .dontAnimate()
                    .placeholder(com.tokopedia.topads.sdk.R.drawable.loading_page)
                    .error(com.tokopedia.topads.sdk.R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(375, 97)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            new TopAdsUrlHitter(holder.getContext()).hitImpressionUrl(
                                    className,
                                    shopItem.getShopImageUrl(),
                                    shopItem.getShopId(),
                                    shopItem.getShopName(),
                                    shopItem.getShopImageUrl());
                            return false;
                        }
                    })
                    .into(holder.shopCover);
        }
    }

    private void setFavorite(ViewHolder holder, TopAdsShopItem shopItem) {
        try {
            if (shopItem.isFav()) {
                holder.favButton.setImageResource(R.drawable.ic_faved);
            } else {
                holder.favButton.setImageResource(R.drawable.ic_fav_white);
            }
        } catch (Exception e) {
            holder.favButton.setImageResource(R.drawable.ic_fav_white);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void createScaleAnimation() {
        anim = new ScaleAnimation(1, 1.25f, 1, 1.25f,
                Animation.RELATIVE_TO_SELF,
                (float) 0.2, Animation.RELATIVE_TO_SELF, (float) 0.2);

        anim.setDuration(250);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setFillAfter(false);
    }

    private View.OnClickListener onShopClicked(final TopAdsShopItem item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                new TopAdsUrlHitter(view.getContext()).hitClickUrl(
                        className,
                        item.getShopClickUrl(),
                        item.getShopId(),
                        item.getShopName(),
                        item.getShopImageUrl());
                eventFavoriteViewRecommendation();
                Intent intent = RouteManager.getIntent(context, ApplinkConst.SHOP, item.getShopId());
                context.startActivity(intent);
            }
        };
    }

    public void eventFavoriteViewRecommendation() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                TrackingConst.Event.FAVORITE,
                TrackingConst.Category.HOMEPAGE.toLowerCase(),
                TrackingConst.Action.CLICK_SHOP_FAVORITE,
                "");
    }

    private View.OnClickListener onFavClicked(final ViewHolder holder, final TopAdsShopItem item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item.isFav()) {
                    view.startAnimation(anim);
                    item.setFav(true);
                    holder.favButton.setImageResource(R.drawable.ic_faved);
                }
                if (favoriteClickListener != null) {
                    favoriteClickListener.onFavoriteShopClicked(view, item);
                }
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainContent;
        ImageView shopCover;
        ImageView shopIcon;
        TextView shopName;
        TextView shopLocation;
        LinearLayout shopInfo;
        ImageView favButton;

        public ViewHolder(View itemView) {
            super(itemView);

            mainContent = (LinearLayout) itemView.findViewById(R.id.main_content);
            shopCover = (ImageView) itemView.findViewById(R.id.shop_cover);
            shopIcon = (ImageView) itemView.findViewById(R.id.shop_icon);
            shopName = (TextView) itemView.findViewById(R.id.shop_name);
            shopLocation = (TextView) itemView.findViewById(R.id.shop_location);
            shopInfo = (LinearLayout) itemView.findViewById(R.id.shop_info);
            favButton = (ImageView) itemView.findViewById(R.id.fav_button);
        }

        public Context getContext() {
            return itemView.getContext();
        }

    }

}
