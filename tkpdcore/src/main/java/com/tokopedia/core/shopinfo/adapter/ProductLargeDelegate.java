package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
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

import com.tkpd.library.utils.BadgeUtil;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.shopinfo.models.productmodel.List;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.ProductItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.tint;

/**
 * Created by Tkpd_Eka on 10/9/2015.
 */
public class ProductLargeDelegate {

    public class VHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.product_image)
        public SquareImageView img;
        @BindView(R2.id.title)
        public TextView name;
        @BindView(R2.id.price)
        public TextView price;
        @BindView(R2.id.container)
        public View mainView;
        @BindView(R2.id.badges_container)
        public LinearLayout containerBadge;
        @BindView(R2.id.label_container)
        public FlowLayout containerLabel;
        @BindView(R2.id.shop_name)
        public View shopName;
        @BindView(R2.id.location)
        public View location;

        public VHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_product_item_grid, parent, false);
        return new VHolder(view);
    }

    public void onBindViewHolder(List item, RecyclerView.ViewHolder holder) {
        VHolder vholder = (VHolder) holder;
        Context context = vholder.mainView.getContext();
        ImageHandler.LoadImage(vholder.img, item.productImage300);
        vholder.name.setText(MethodChecker.fromHtml(item.productName));
        vholder.price.setText(item.productPrice);
        if (item.labels == null) {
            item.labels = new ArrayList<ProductItem.Label>();
            if (item.productPreorder != null && item.productPreorder.equals("1")) {
                ProductItem.Label label = new ProductItem.Label();
                label.setTitle(context.getString(R.string.preorder));
                label.setColor(context.getString(R.string.white_hex_color));
                item.labels.add(label);
            }
            if (item.productWholesale != null && item.productWholesale.equals("1")) {
                ProductItem.Label label = new ProductItem.Label();
                label.setTitle(context.getString(R.string.grosir));
                label.setColor(context.getString(R.string.white_hex_color));
                item.labels.add(label);
            }
        }
        setLabels(vholder, item);
        setBadge(vholder, item);
        vholder.shopName.setVisibility(View.GONE);
        vholder.location.setVisibility(View.GONE);
    }

    private void setLabels(VHolder holder, List data) {
        holder.containerLabel.removeAllViews();
        if (data.labels != null) {
            for (ProductItem.Label label : data.labels) {
                View view = LayoutInflater.from(holder.mainView.getContext()).inflate(R.layout.label_layout, null);
                TextView labelText = (TextView) view.findViewById(R.id.label);
                labelText.setText(label.getTitle());
                if (!label.getColor().toLowerCase().equals("#ffffff")) {
                    labelText.setBackgroundResource(R.drawable.bg_label);
                    labelText.setTextColor(ContextCompat.getColor(holder.mainView.getContext(), R.color.white));
                    ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        labelText.setBackgroundTintList(tint);
                    } else {
                        ViewCompat.setBackgroundTintList(labelText, tint);
                    }
                }
                holder.containerLabel.addView(view);
            }
        }
    }


    private void setBadge(VHolder holder, List item) {
        java.util.List<ProductItem.Badge> badgeList = item.badges;
        holder.containerBadge.removeAllViews();
        if(badgeList != null) {
            for (int i = 0; i < badgeList.size(); i++) {
                ProductItem.Badge badge = badgeList.get(i);
                ImageView badgeImage = BadgeUtil.createDynamicBadge(holder.itemView.getContext());
                badgeImage.setVisibility(View.GONE);
                holder.containerBadge.addView(badgeImage);
                LuckyShopImage.loadImage(badgeImage, badge.getImageUrl());
            }
        }
    }

    public void onItemClickListener(View.OnClickListener onClick, RecyclerView.ViewHolder holder) {
        ((VHolder) holder).mainView.setOnClickListener(onClick);
    }
}
