package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.section.CategoryTokopointsList;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;

import java.util.List;

public class SectionCategoryAdapter extends RecyclerView.Adapter<SectionCategoryAdapter.ViewHolder> {
    private final List<CategoryTokopointsList> mCategories;
    private Context context;

    public SectionCategoryAdapter(List<CategoryTokopointsList> categories) {
        this.mCategories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tp_item_section_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategories != null ? mCategories.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivBg;
        public boolean isVisited = false;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBg = itemView.findViewById(R.id.iv_bg);
            tvTitle = itemView.findViewById(R.id.text_title);
        }

        public void bindData(CategoryTokopointsList category) {
            if (category == null) {
                return;
            }

            if (URLUtil.isValidUrl(category.getIconImageURL())) {
                ImageHandler.loadImageFitCenter(context, ivBg, category.getIconImageURL());
            }

            if (!TextUtils.isEmpty(category.getText())) {
                tvTitle.setText(category.getText());
            }

            itemView.setOnClickListener(view -> {

                if (TextUtils.isEmpty(category.getAppLink())) {
                    RouteManager.route(context, String.format("%s?url=%s",
                            ApplinkConst.WEBVIEW,
                            category.getUrl()));
                } else {
                    RouteManager.route(context, category.getAppLink());
                }

                AnalyticsTrackerUtil.sendEvent(itemView.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_DYNAMIC_CAT,
                        category.getText()
                );
            });

        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        try {
            CategoryTokopointsList data = mCategories.get(holder.getAdapterPosition());
            if (data == null || holder.itemView == null) {
                return;
            }

            if (!holder.isVisited) {
                AnalyticsTrackerUtil.sendEvent(holder.itemView.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_DYNAMIC_CAT,
                        data.getText());

                holder.isVisited = true;
            }
        } catch (Exception e) {

        }
    }
}
