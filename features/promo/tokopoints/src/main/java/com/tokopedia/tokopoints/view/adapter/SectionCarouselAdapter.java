package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
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
        private TextView bnrTitle, bnrSubTitle, titleBottom, subTitleBottom;
        public boolean isVisited = false;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCol = itemView.findViewById(R.id.iv_col_1);
            bnrTitle = itemView.findViewById(R.id.text_title_banner);
            bnrSubTitle = itemView.findViewById(R.id.text_sub_title_banner);
            titleBottom = itemView.findViewById(R.id.text_title_bottom);
            subTitleBottom = itemView.findViewById(R.id.text_sub_title_bottom);
        }

        public void bindData(ImageList item) {
            if (item == null) {
                return;
            }

            if (URLUtil.isValidUrl(item.getImageURLMobile())) {
                ImageHandler.loadImageFitCenter(context, ivCol, item.getImageURLMobile());
            }

            bnrTitle.setText(item.getInBannerTitle());
            bnrSubTitle.setText(item.getInBannerSubTitle());

            itemView.setOnClickListener(view -> {
                handledClick(item.getRedirectAppLink(), item.getRedirectURL());
            });

            if (TextUtils.isEmpty(item.getTitle())) {
                titleBottom.setVisibility(View.GONE);
            } else {
                titleBottom.setVisibility(View.VISIBLE);
                titleBottom.setText(item.getTitle());
            }

            if (TextUtils.isEmpty(item.getSubTitle())) {
                subTitleBottom.setVisibility(View.GONE);
            } else {
                subTitleBottom.setVisibility(View.VISIBLE);
                subTitleBottom.setText(item.getSubTitle());
            }

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
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_DYNAMIC_ICON,
                        data.getTitle());

                holder.isVisited = true;
            }
        } catch (Exception e) {

        }
    }

    void handledClick(String appLink, String webLink) {
        if (TextUtils.isEmpty(appLink)) {
            ((TokopointRouter) context.getApplicationContext()).openTokoPoint(context, webLink);
        } else {
            RouteManager.route(context, appLink);
        }
    }
}
