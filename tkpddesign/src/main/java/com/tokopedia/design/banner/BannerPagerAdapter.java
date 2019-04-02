package com.tokopedia.design.banner;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.tokopedia.design.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alifa on 11/28/17.
 */

public class BannerPagerAdapter extends RecyclerView.Adapter<BannerPagerAdapter.BannerViewHolder> {

    protected List<String> bannerImageUrls = new ArrayList<>();
    private BannerView.OnPromoClickListener onPromoClickListener;

    public BannerPagerAdapter(List<String> bannerImageUrls, BannerView.OnPromoClickListener onPromoClickListener) {
        this.bannerImageUrls = bannerImageUrls;
        this.onPromoClickListener = onPromoClickListener;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;

        public BannerViewHolder(View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.image);
        }

        public ImageView getBannerImage() {
            return bannerImage;
        }
    }

    @Override
    public int getItemCount() {
        return bannerImageUrls.size();
    }

    @NotNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new BannerViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_slider_banner_design, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        if (bannerImageUrls.get(position) != null &&
                bannerImageUrls.get(position).length() > 0) {
            holder.bannerImage.setOnClickListener(
                    getBannerImageOnClickListener(position)
            );
        }
        try {
            Glide.with(holder.itemView.getContext())
                    .load(bannerImageUrls.get(position))
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.drawable.ic_loading_image)
                    .error(R.drawable.ic_loading_image)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(holder.bannerImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private View.OnClickListener getBannerImageOnClickListener(final int currentPosition) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPromoClickListener != null)
                    onPromoClickListener.onPromoClick(currentPosition);
            }
        };
    }

    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public void setItems(List<String> bannerImageUrls) {
        this.bannerImageUrls = bannerImageUrls;
    }

    public void clear() {
        this.bannerImageUrls.clear();
    }

    public void setOnItemClickListener(BannerView.OnPromoClickListener onPromoClickListener) {
        this.onPromoClickListener = onPromoClickListener;
    }
}
