package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.BadgeUtil;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.shopinfo.models.productmodel.Label;
import com.tokopedia.core.shopinfo.models.productmodel.List;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;


/**
 * Created by Tkpd_Eka on 10/9/2015.
 */
public class ProductLargeDelegate {

    public class VHolder extends RecyclerView.ViewHolder {
        public SquareImageView img;
        public TextView name;
        public TextView price;
        public View mainView;
        public LinearLayout containerBadge;
        public FlowLayout containerLabel;
        public View shopName;
        public View location;
        public TextView textOriginalPrice;
        public TextView textDiscount;
        public String discount;

        public VHolder(View itemView) {
            super(itemView);

            img = (SquareImageView) itemView.findViewById(R.id.product_image);
            name = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            mainView = (View) itemView.findViewById(R.id.container);
            containerBadge = (LinearLayout) itemView.findViewById(R.id.badges_container);
            containerLabel = (FlowLayout) itemView.findViewById(R.id.label_container);
            shopName = (View) itemView.findViewById(R.id.shop_name);
            location = (View) itemView.findViewById(R.id.location);
            textOriginalPrice = (TextView) itemView.findViewById(R.id.text_original_price);
            textDiscount = (TextView) itemView.findViewById(R.id.text_discount);
            discount = itemView.getResources().getString(R.string.label_discount_percentage);
            mainView = itemView;
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
            item.labels = new ArrayList<Label>();
            if (item.productPreorder != null && item.productPreorder.equals("1")) {
                Label label = new Label();
                label.setTitle(context.getString(R.string.preorder));
                label.setColor(context.getString(R.string.white_hex_color));
                item.labels.add(label);
            }
            if (item.productWholesale != null && item.productWholesale.equals("1")) {
                Label label = new Label();
                label.setTitle(context.getString(R.string.grosir));
                label.setColor(context.getString(R.string.white_hex_color));
                item.labels.add(label);
            }
        }
        setLabels(vholder, item);
        setBadge(vholder, item);
        vholder.shopName.setVisibility(View.GONE);
        vholder.location.setVisibility(View.GONE);

        if (item.shopProductCampaign != null) {
            vholder.price.setTextColor(ContextCompat.getColor(context, R.color.bright_red));
            vholder.textOriginalPrice.setText(item.shopProductCampaign.getOriginalPriceIdr());
            vholder.textOriginalPrice.setPaintFlags(
                    vholder.textOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            vholder.textDiscount.setText(
                String.format(vholder.discount,item.shopProductCampaign.getPercentageAmount())
            );
            vholder.textOriginalPrice.setVisibility(View.VISIBLE);
            vholder.textDiscount.setVisibility(View.VISIBLE);
        } else {
            vholder.textOriginalPrice.setVisibility(View.GONE);
            vholder.textDiscount.setVisibility(View.GONE);
        }
    }

    private void setLabels(VHolder holder, List data) {
        holder.containerLabel.removeAllViews();
        if (data.labels != null) {
            for (Label label : data.labels) {
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
        java.util.List<Badge> badgeList = item.badges;
        holder.containerBadge.removeAllViews();
        if(badgeList != null) {
            for (int i = 0; i < badgeList.size(); i++) {
                Badge badge = badgeList.get(i);
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
