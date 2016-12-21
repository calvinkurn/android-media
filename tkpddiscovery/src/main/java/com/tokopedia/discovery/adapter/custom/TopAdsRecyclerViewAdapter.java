package com.tokopedia.discovery.adapter.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.InfoTopAds;
import com.tokopedia.core.R;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.util.TopAdsUtil;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;

import java.util.List;

/**
 * Created by admin on 8/06/15.
 */
public class TopAdsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final String TAG = TopAdsRecyclerViewAdapter.class.getSimpleName();
    private List<ProductItem> data;
    private Context context;
    private String source;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout badgeContainer;
        public FlowLayout labelContainer;
        public TextView productName;
        public TextView productPrice;
        public TextView shopName;
        public TextView shopLocation;
        public ImageView productImage;
        public View mainContent;
        public View grosir;
        public View preorder;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mainContent = itemLayoutView.findViewById(R.id.container);
            badgeContainer = (LinearLayout) itemLayoutView.findViewById(R.id.badges_container);
            labelContainer = (FlowLayout) itemLayoutView.findViewById(R.id.label_container);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            shopName = (TextView) itemLayoutView.findViewById(R.id.shop_name);
            shopLocation = (TextView) itemLayoutView.findViewById(R.id.location);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            grosir = itemLayoutView.findViewById(R.id.grosir);
            preorder = itemLayoutView.findViewById(R.id.preorder);
        }
    }

    public TopAdsRecyclerViewAdapter(Context context, List<ProductItem> data, String source) {
        this.data = data;
        this.context = context;
        this.source = source;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.listview_product_item_grid, parent, false));
        } else if (viewType == TYPE_HEADER) {
            return new VHHeader(LayoutInflater.from(context).inflate(R.layout.child_main_top_ads_header, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            ProductItem item = getItem(position);
            holder.productName.setText(Html.fromHtml(item.name));
            holder.productPrice.setText(item.price);
            holder.shopLocation.setText(item.getShop_location());
            holder.shopName.setText(Html.fromHtml(item.shop));
            setProductImage(holder, item);
            setClickListener(holder, item);
            setLabels(holder, item);
            setBadges(holder, item);
        }
    }

    private void setProductImage(ViewHolder holder, ProductItem item) {
        ImageHandler.loadImageFit2(context, holder.productImage, item.getImgUri());
    }

    private void setClickListener(ViewHolder holder, final ProductItem item) {
        holder.mainContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopAdsUtil.clickTopAdsAction(context, item.getTopAds().getProductClickUrl());

                Bundle bundle = new Bundle();
                Intent intent = new Intent(context, ProductInfoActivity.class);
                bundle.putString("product_id", item.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    private void setLabels(ViewHolder holder, ProductItem item) {
        if (item.getLabels() != null && holder.labelContainer.getChildCount() == 0)
            for (Label label : item.getLabels()) {
                View view = LayoutInflater.from(context).inflate(R.layout.label_layout, null);
                TextView labelText = (TextView) view.findViewById(R.id.label);
                labelText.setText(label.getTitle());
                if (!label.getColor().toLowerCase().equals("#ffffff")) {
                    labelText.setBackgroundResource(R.drawable.bg_label);
                    labelText.setTextColor(ContextCompat.getColor(context, R.color.white));
                    ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        labelText.setBackgroundTintList(tint);
                    } else {
                        ViewCompat.setBackgroundTintList(labelText, tint);
                    }
                }
                holder.labelContainer.addView(view);
            }
    }

    private void setBadges(ViewHolder holder, ProductItem item) {
        if (item.getBadges() != null && holder.badgeContainer.getChildCount() == 0)
            for (Badge badges : item.getBadges()) {
                View view = LayoutInflater.from(context).inflate(R.layout.badge_layout, null);
                ImageView imageBadge = (ImageView) view.findViewById(R.id.badge);
                holder.badgeContainer.addView(view);
                LuckyShopImage.loadImage(imageBadge, badges.getImageUrl());
            }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private ProductItem getItem(int position) {
        return data.get(position - 1);
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    class VHHeader extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView infoTopAds;

        public VHHeader(View itemView) {
            super(itemView);
            infoTopAds = (ImageView) itemView.findViewById(R.id.info_topads);
            infoTopAds.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            InfoTopAds infoTopAds = InfoTopAds.newInstance(source);
            Activity activity = (Activity) context;
            infoTopAds.show(activity.getFragmentManager(), "INFO_TOPADS");
        }
    }
}