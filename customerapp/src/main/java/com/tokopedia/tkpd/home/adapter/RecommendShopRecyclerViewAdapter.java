package com.tokopedia.tkpd.home.adapter;

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
import com.tokopedia.tkpd.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.TopAdsUtil;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.tkpd.home.favorite.presenter.Favorite;

import java.util.List;

/**
 * Created by Nisie on 11/06/15.
 */
public class RecommendShopRecyclerViewAdapter extends RecyclerView.Adapter<RecommendShopRecyclerViewAdapter.ViewHolder> {

    private final String FAV_SHOP = "fav_shop";

    public interface RecShopListener {
        void OnClicked(View view, ShopItem shop);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout shopLayout;
        ImageView shopCover;
        ImageView shopIcon;
        TextView shopName;
        TextView shopLocation;
        ImageView shopFav;

        public ViewHolder(View itemView) {
            super(itemView);
            shopLayout = (LinearLayout) itemView.findViewById(R.id.main_content);
            shopCover = (ImageView) itemView.findViewById(R.id.shop_cover);
            shopIcon = (ImageView) itemView.findViewById(R.id.shop_icon);
            shopName = (TextView) itemView.findViewById(R.id.shop_name);
            shopLocation = (TextView) itemView.findViewById(R.id.shop_location);
            shopFav = (ImageView) itemView.findViewById(R.id.fav_button);
        }

        public Context getContext(){
            return itemView.getContext();
        }
    }

    private Context context;
    private List<ShopItem> data;
    Favorite favorite;
    protected ScaleAnimation anim;

    public RecommendShopRecyclerViewAdapter(Context context, List<ShopItem> data, Favorite favorite){
        this.context = context;
        this.data = data;
        this.favorite = favorite;
    }

    @Override

    public RecommendShopRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_reccommend_shop, null);
        createScaleAnimation();
        return new ViewHolder(itemLayoutView);
    }

    private void createScaleAnimation() {
        anim = new ScaleAnimation(1, 1.25f, 1, 1.25f, Animation.RELATIVE_TO_SELF, (float)0.2, Animation.RELATIVE_TO_SELF, (float)0.2);
        anim.setDuration(250);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setFillAfter(false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.shopName.setText(Html.fromHtml(data.get(position).name));
        holder.shopLocation.setText(data.get(position).location);
        setShopCover(holder, holder.shopCover, data.get(position).coverUri);
//        ImageHandler.LoadImageFit(holder.shopIcon, data.get(position).iconUri);
        ImageHandler.loadImageFit2(holder.getContext(), holder.shopIcon, data.get(position).iconUri);
        setFavorite(holder, position);

        holder.shopLayout.setOnClickListener(onShopClickedListener(data.get(position)));
        holder.shopFav.setOnClickListener(onFavClickedListener(holder, data.get(position)));

    }

    private void setShopCover(ViewHolder holder,ImageView shopCover, String coverUri) {
        if(coverUri == null ){
            shopCover.setImageResource(R.drawable.ic_loading_toped);
        }else {
//            ImageHandler.LoadImageFit(shopCover, coverUri);
            ImageHandler.loadImageFit2(holder.getContext(),shopCover, coverUri);
        }
    }

    private View.OnClickListener onFavClickedListener(final ViewHolder holder, final ShopItem shop) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shop.isFav.equals("0")) {
                    view.startAnimation(anim);
                    shop.isFav = "1";
                    holder.shopFav.setImageResource(R.drawable.ic_faved);
                    favorite.onRecommendShopClicked(view, shop);
                }
            }
        };
    }

    private View.OnClickListener onShopClickedListener(final ShopItem shop) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopAdsUtil.clickTopAdsAction(context, shop.shopClickUrl);
                UnifyTracking.eventFavoriteViewRecommendation(shop.name);
                Intent intent = new Intent(context, ShopInfoActivity.class);
                Bundle bundle = ShopInfoActivity.createBundle(shop.id, "", shop.name, shop.iconUri, shop.coverUri, Integer.parseInt(shop.isFav));
                bundle.putString(ShopInfoActivity.SHOP_AD_KEY, shop.adKey);
//                bundle.putString("ad_r", shop.adR);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };
    }

    private void setFavorite(ViewHolder holder, int position) {
        try {
            if (data.get(position).isFav.equals("1")) {
                holder.shopFav.setImageResource(R.drawable.ic_faved);
            } else {
                holder.shopFav.setImageResource(R.drawable.ic_fav_white);
            }
        } catch (Exception e) {
            holder.shopFav.setImageResource(R.drawable.ic_fav_white);
        }
    }


    @Override
    public int getItemCount() {
        if(data!= null) {
            return data.size();
        } else {
            return 0;
        }
    }


}