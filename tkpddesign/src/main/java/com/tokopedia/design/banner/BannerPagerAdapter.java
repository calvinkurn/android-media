package com.tokopedia.design.banner;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.tokopedia.design.R;

import java.util.List;

/**
 * @author by alifa on 11/28/17.
 */

public class BannerPagerAdapter extends RecyclerView.Adapter<BannerPagerAdapter.BannerViewHolder> {

    private static final String TAG = BannerPagerAdapter.class.getSimpleName();
    private final List<String> bannerImageUrls;
    private final BannerView.OnPromoClickListener onPromoClickListener;

    public BannerPagerAdapter(List<String> bannerImageUrls,  BannerView.OnPromoClickListener onPromoClickListener) {
        this.bannerImageUrls = bannerImageUrls;
        this.onPromoClickListener = onPromoClickListener;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView bannerImage;
        CardView cardView;

        public BannerViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout_slider_banner_category);
            bannerImage = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BannerViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_slider_banner_category, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        if (bannerImageUrls.get(position)!=null &&
                bannerImageUrls.get(position).length()>0) {
            holder.bannerImage.setOnClickListener(
                    getBannerImageOnClickListener(position)
            );
        }

/*        float scale = holder.itemView.getContext().getResources().getDisplayMetrics().density;
        int padding_8dp = (int) (4 * scale + 0.5f);
        holder.itemView.setPadding(padding_8dp, padding_8dp, padding_8dp, padding_8dp);

        int width320dp = (int) holder.itemView.getContext().getResources().getDimension(R.dimen.item_banner_width_category);
        int height160dp = (int) holder.itemView.getContext().getResources().getDimension(R.dimen.item_banner_height_category);

        ViewGroup.LayoutParams layoutParams = holder.bannerImage.getLayoutParams();
        layoutParams.height = height160dp;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.bannerImage.setLayoutParams(layoutParams);
        holder.bannerImage.requestLayout();*/

        if (bannerImageUrls.size() == 1) {
            holder.linearLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            holder.linearLayout.requestLayout();
            holder.linearLayout.setGravity(Gravity.CENTER);
        }

        Glide.with(holder.bannerImage.getContext())
                .load(bannerImageUrls.get(position))
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.ic_loading_image)
                .error(R.drawable.ic_loading_image)
                .into(holder.bannerImage);


    }

    @Override
    public int getItemCount() {
        return bannerImageUrls.size();
    }

    private View.OnClickListener getBannerImageOnClickListener(final int currentPosition) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}
