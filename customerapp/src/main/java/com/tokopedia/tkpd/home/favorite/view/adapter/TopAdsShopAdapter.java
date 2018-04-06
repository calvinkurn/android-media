package com.tokopedia.tkpd.home.favorite.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.core.util.TopAdsUtil;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.topads.sdk.utils.ImpresionTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author by erry on 30/01/17.
 */

public class TopAdsShopAdapter extends RecyclerView.Adapter<TopAdsShopAdapter.ViewHolder> {

    protected ScaleAnimation anim;
    private List<TopAdsShopItem> data;
    private FavoriteClickListener favoriteClickListener;
    private Context context;
    private final String PATH_VIEW = "views";

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
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.listview_reccommend_shop, parent, false);
        createScaleAnimation();
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopAdsShopItem shopItem = data.get(position);
        holder.shopName.setText(Html.fromHtml(shopItem.getShopName()));
        holder.shopLocation.setText(Html.fromHtml(shopItem.getShopLocation()));
        ImageHandler.loadImageFit2(holder.getContext(), holder.shopIcon, shopItem.getShopImageUrl());
        setShopCover(holder, shopItem.getShopCoverUrl(), shopItem.getShopCoverEcs());
        setFavorite(holder, shopItem);
        holder.mainContent.setOnClickListener(onShopClicked(shopItem));
        holder.favButton.setOnClickListener(
                shopItem.isFav() ? null : onFavClicked(holder, shopItem)
        );
    }


    private void setShopCover(ViewHolder holder, final String coverUri, String ecs) {
        if (coverUri == null) {
            holder.shopCover.setImageResource(R.drawable.ic_loading_toped);
        } else {
            Glide.with(context)
                    .load(ecs)
                    .dontAnimate()
                    .placeholder(com.tokopedia.core.R.drawable.loading_page)
                    .error(com.tokopedia.core.R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .override(375, 97)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache,
                                                       boolean isFirstResource) {
                            if(coverUri.contains(PATH_VIEW) && !isFromMemoryCache) {
                                new ImpresionTask().execute(coverUri);
                            }
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
                TopAdsUtil.clickTopAdsAction(context, item.getShopClickUrl());
                UnifyTracking.eventFavoriteViewRecommendation();
                Intent intent = ShopPageActivity.createIntent(context, item.getShopId());
                context.startActivity(intent);
            }
        };
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
            ButterKnife.bind(this, itemView);
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
