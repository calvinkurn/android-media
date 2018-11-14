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
import com.tokopedia.tokopoints.view.model.LinksItemEntity;

import java.util.List;

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
        private ImageView ivNextArrow;
        private ImageView ivBack;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBack = itemView.findViewById(R.id.iv_background_image);
            tvDynamicText = itemView.findViewById(R.id.tv_dynamic_text);
            ivNextArrow = itemView.findViewById(R.id.iv_right_arrow);
        }

        public void bindData(LinksItemEntity linksItemEntity) {
            if (getAdapterPosition() == getItemCount() - 1) {
                ivNextArrow.setVisibility(View.GONE);
                if (getItemCount() % 2 != 0) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvDynamicText.getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, 0);
                    tvDynamicText.setGravity(Gravity.CENTER);
                }
            }

            if (URLUtil.isValidUrl(linksItemEntity.getBackgroundURL())) {
                ImageHandler.loadImage(context, ivBack, linksItemEntity.getBackgroundURL(), R.color.grey_1100, R.color.grey_1100);
            } else {
                ivBack.setBackgroundColor(Color.parseColor(linksItemEntity.getBackgroundColor()));
            }

            if (!TextUtils.isEmpty(linksItemEntity.getText())) {
                tvDynamicText.setText(linksItemEntity.getText());
            }
            if (!TextUtils.isEmpty(linksItemEntity.getFontColor())) {
                tvDynamicText.setTextColor(Color.parseColor(linksItemEntity.getFontColor()));
                ivNextArrow.setColorFilter(Color.parseColor(linksItemEntity.getFontColor()));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!TextUtils.isEmpty(linksItemEntity.getAppLink())) {
                        RouteManager.route(context, linksItemEntity.getAppLink());
                    } else if (!TextUtils.isEmpty(linksItemEntity.getUrl())) {
                        RouteManager.route(context, String.format("%s?url=%s",
                                ApplinkConst.WEBVIEW,
                                linksItemEntity.getUrl()));
                    }
                }
            });

        }
    }
}
