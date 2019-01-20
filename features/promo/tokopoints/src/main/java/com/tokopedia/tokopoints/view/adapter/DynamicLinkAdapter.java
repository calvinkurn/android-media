package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.LinksItemEntity;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicLinkAdapter extends RecyclerView.Adapter<DynamicLinkAdapter.ViewHolder> {


    private final List<LinksItemEntity> linksList;
    private Context context;

    public DynamicLinkAdapter(List<LinksItemEntity> links) {
        this.linksList = links;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tp_dynamic_link_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(linksList.get(position));
    }

    @Override
    public int getItemCount() {
        return linksList != null ? linksList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDynamicText;
        private ImageView ivBack;
        public boolean isVisited = false;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBack = itemView.findViewById(R.id.iv_background_image);
            tvDynamicText = itemView.findViewById(R.id.tv_dynamic_text);
        }

        public void bindData(LinksItemEntity linksItemEntity) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvDynamicText.getLayoutParams();
            if (getAdapterPosition() == getItemCount() - 1 && getItemCount() % 2 != 0) {
                layoutParams.setMargins(0, 0, 0, 0);
                tvDynamicText.setGravity(Gravity.CENTER);
            } else {
                layoutParams.setMargins(context.getResources().getDimensionPixelSize(R.dimen.dp_8), 0, 0, 0);
                tvDynamicText.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            }

            if (URLUtil.isValidUrl(linksItemEntity.getBackgroundURL())) {
                ImageHandler.loadImage(context, ivBack, linksItemEntity.getBackgroundURL(), R.color.medium_green, R.color.medium_green);
            } else {
                ivBack.setBackgroundColor(Color.parseColor(linksItemEntity.getBackgroundColor()));
            }

            if (!TextUtils.isEmpty(linksItemEntity.getText())) {
                tvDynamicText.setText(linksItemEntity.getText());
            }
            if (!TextUtils.isEmpty(linksItemEntity.getFontColor())) {
                tvDynamicText.setTextColor(Color.parseColor(linksItemEntity.getFontColor()));
            }
            itemView.setOnClickListener(view -> {

                if (!TextUtils.isEmpty(linksItemEntity.getAppLink())) {
                    RouteManager.route(context, linksItemEntity.getAppLink());
                } else if (!TextUtils.isEmpty(linksItemEntity.getUrl())) {
                    RouteManager.route(context, String.format("%s?url=%s",
                            ApplinkConst.WEBVIEW,
                            linksItemEntity.getUrl()));
                }

                AnalyticsTrackerUtil.sendEvent(itemView.getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_DYNAMIC_CAT,
                        linksItemEntity.getText()
                );
            });

        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        LinksItemEntity data = linksList.get(holder.getAdapterPosition());
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
    }
}