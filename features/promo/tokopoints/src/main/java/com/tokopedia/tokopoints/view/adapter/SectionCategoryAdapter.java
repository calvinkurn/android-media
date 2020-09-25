package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.section.CategoryTokopointsList;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommanUtilsKt;
import com.tokopedia.tokopoints.view.util.ImageUtil;
import com.tokopedia.unifycomponents.ImageUnify;
import com.tokopedia.utils.image.ImageUtils;

import java.util.List;

public class SectionCategoryAdapter extends RecyclerView.Adapter<SectionCategoryAdapter.ViewHolder> {
    private final List<CategoryTokopointsList> mCategories;
    private Context context;

    public SectionCategoryAdapter(Context context, List<CategoryTokopointsList> categories) {
        this.context = context;
        this.mCategories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        private View viewCategoryNew;
        private TextView tvTitle;
        private AppCompatImageView ivBg;
        public boolean isVisited = false;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBg = itemView.findViewById(R.id.iv_bg);
            tvTitle = itemView.findViewById(R.id.text_title);
            viewCategoryNew = itemView.findViewById(R.id.view_category_new);
        }

        public void bindData(CategoryTokopointsList category) {
            if (category == null) {
                return;
            }

            if (category.getIsNewCategory()) {
                viewCategoryNew.setVisibility(View.VISIBLE);
            } else {
                viewCategoryNew.setVisibility(View.GONE);
            }

            if (URLUtil.isValidUrl(category.getIconImageURL())) {
                ImageHandler.loadImageCircle2(context, ivBg, category.getIconImageURL());
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
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_DYNAMIC_ICON,
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
