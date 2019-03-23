package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.section.ImageList;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

public class SectionCarouselAdapter extends RecyclerView.Adapter<SectionCarouselAdapter.ViewHolder> {
    private List<ImageList> mItems;
    private Context context;
    private String mType;

    public SectionCarouselAdapter(List<ImageList> items, String type) {
        this.mItems = items;
        this.mType = type;
    }

    @NonNull
    @Override
    public SectionCarouselAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;

        switch (mType) {
            case CommonConstant.BannerType.CAROUSEL_3_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tp_item_carousel_3_1, parent, false);
                break;
            case CommonConstant.BannerType.CAROUSEL_2_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tp_item_carousel_2_1, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tp_item_carousel_1_1, parent, false);
        }
        return new SectionCarouselAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCol;
        public boolean isVisited = false;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCol = itemView.findViewById(R.id.iv_col_1);
        }

        public void bindData(ImageList item) {
            if (item == null) {
                return;
            }

            if (URLUtil.isValidUrl(item.getImageURLMobile())) {
                ImageHandler.loadImageFitCenter(context, ivCol, item.getImageURLMobile());
            }

            itemView.setOnClickListener(view -> {

            });

        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        try {
            ImageList data = mItems.get(holder.getAdapterPosition());
            if (data == null || holder.itemView == null) {
                return;
            }

            if (!holder.isVisited) {
                AnalyticsTrackerUtil.sendEvent(holder.itemView.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_DYNAMIC_CAT,
                        data.getTitle());

                holder.isVisited = true;
            }
        } catch (Exception e) {

        }
    }
}
