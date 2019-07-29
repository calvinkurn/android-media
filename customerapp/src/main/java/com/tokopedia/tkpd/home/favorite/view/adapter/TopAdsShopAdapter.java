package com.tokopedia.tkpd.home.favorite.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.track.TrackApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
                .inflate(R.layout.listview_reccommend_shop, parent, false);
        createScaleAnimation();
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopAdsShopItem shopItem = data.get(position);
        holder.shopName.setText(Html.fromHtml(shopItem.getShopName()));
        holder.shopLocation.setText(Html.fromHtml(shopItem.getShopLocation()));
        imageLoader.loadImage(shopItem.getShopImageUrl(), shopItem.getShopImageEcs(), holder.shopIcon);
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
                    .placeholder(com.tokopedia.core2.R.drawable.loading_page)
                    .error(com.tokopedia.core2.R.drawable.error_drawable)
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
                            if (coverUri.contains(PATH_VIEW) && !isFromMemoryCache) {
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
                new FireTopAdsActionAsyncTask().execute(item.getShopClickUrl());
                eventFavoriteViewRecommendation();
                Intent intent = ShopPageActivity.createIntent(context, item.getShopId());
                context.startActivity(intent);
            }
        };
    }

    /**
     * Hack solution using AsyncTask
     * This is to handled fire and forget url to shopfavorit
     * Previously was using Volley (TopadsUtil.clickTopAdsAction)
     */
    public static class FireTopAdsActionAsyncTask extends AsyncTask<String, Void, Void> {

        @NonNull
        @Override
        protected Void doInBackground(@NonNull String... strings) {
            URL url;
            HttpURLConnection urlConnection;

            try {
                String stringBuilder = strings[0] +
                        "?device=android&os_type=1&appversion=" +
                        GlobalConfig.VERSION_CODE;
                url = new URL(stringBuilder);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    readStream(urlConnection.getInputStream());
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        private void readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuilder response = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void eventFavoriteViewRecommendation() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                AppEventTracking.Action.CLICK_SHOP_FAVORITE,
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
