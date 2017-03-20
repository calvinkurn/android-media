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

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.TopAdsUtil;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.viewListener.FavoriteClickListener;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by erry on 30/01/17.
 */

public class TopAdsShopAdapter extends RecyclerView.Adapter<TopAdsShopAdapter.ViewHolder> {

    protected ScaleAnimation anim;
    private List<TopAdsShopItem> data;
    private FavoriteClickListener favoriteClickListener;

    public TopAdsShopAdapter(FavoriteClickListener favoriteClickListener) {
        this.favoriteClickListener = favoriteClickListener;
        data = new ArrayList<>();
    }

    public void setData(List<TopAdsShopItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
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
        setShopCover(holder, shopItem.getShopCoverUrl());
        setFavorite(holder, shopItem);
        holder.mainContent.setOnClickListener(onShopClicked(shopItem));
        holder.favButton.setOnClickListener(onFavClicked(holder, shopItem));
    }


    private void setShopCover(ViewHolder holder, String coverUri) {
        if (coverUri == null) {
            holder.shopCover.setImageResource(R.drawable.ic_loading_toped);
        } else {
            ImageHandler.loadImageFit2(holder.getContext(), holder.shopCover, coverUri);
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
        anim = new ScaleAnimation(1, 1.25f, 1, 1.25f, Animation.RELATIVE_TO_SELF, (float) 0.2, Animation.RELATIVE_TO_SELF, (float) 0.2);
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
                UnifyTracking.eventFavoriteViewRecommendation(item.getShopName());
                Intent intent = new Intent(context, ShopInfoActivity.class);
                Bundle bundle = ShopInfoActivity.createBundle(item.getShopId(), item.getShopDomain(), item.getShopName(), item.getShopImageUrl(), item.getShopCoverUrl(), (item.isFav() ? 1 : 0));
                bundle.putString(ShopInfoActivity.SHOP_AD_KEY, item.getAdKey());
                intent.putExtras(bundle);
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
                    favoriteClickListener.onFavClicked(view, item);
                }
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_content)
        LinearLayout mainContent;
        @BindView(R.id.shop_cover)
        ImageView shopCover;
        @BindView(R.id.shop_icon)
        ImageView shopIcon;
        @BindView(R.id.shop_name)
        TextView shopName;
        @BindView(R.id.shop_location)
        TextView shopLocation;
        @BindView(R.id.shop_info)
        LinearLayout shopInfo;
        @BindView(R.id.fav_button)
        ImageView favButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public Context getContext() {
            return itemView.getContext();
        }

    }


}
