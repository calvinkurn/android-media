package com.tokopedia.tkpd.shopinfo.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
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
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.customView.AspectRatioImageView;
import com.tokopedia.tkpd.customwidget.FlowLayout;
import com.tokopedia.tkpd.customwidget.SquareImageView;
import com.tokopedia.tkpd.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.tkpd.shopinfo.models.productmodel.List;
import com.tokopedia.tkpd.var.ProductItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tkpd_Eka on 10/9/2015.
 */
public class ProductLargeDelegate {

    public class VHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.product_image)
        public SquareImageView img;
        @Bind(R.id.title)
        public TextView name;
        @Bind(R.id.price)
        public TextView price;
        @Bind(R.id.container)
        public View mainView;
        @Bind(R.id.badges_container)
        public LinearLayout containerBadge;
        @Bind(R.id.label_container)
        public FlowLayout containerLabel;
        @Bind(R.id.shop_name)
        public View shopName;
        @Bind(R.id.location)
        public View location;

        public VHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_product_item_grid, parent, false);
        return new VHolder(view);
    }

    public void onBindViewHolder(List item, RecyclerView.ViewHolder holder){
        VHolder vholder = (VHolder) holder;
        ImageHandler.LoadImage(vholder.img, item.productImage300);
        vholder.name.setText(Html.fromHtml(item.productName));
        vholder.price.setText(item.productPrice);
        vholder.containerLabel.removeAllViews();
        if(item.productPreorder != null && item.productPreorder.equals("1")) {
            View view = LayoutInflater.from(vholder.mainView.getContext()).inflate(R.layout.label_layout, null);
            TextView labelText = (TextView) view.findViewById(R.id.label);
            labelText.setText("Preorder");
            ColorStateList tint = ColorStateList.valueOf(Color.parseColor("#e55b36"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                labelText.setBackgroundTintList(tint);
            } else {
                ViewCompat.setBackgroundTintList(labelText, tint);
            }
            vholder.containerLabel.addView(view);
        }
        if(item.productWholesale != null && item.productWholesale.equals("1")){
            View view = LayoutInflater.from(vholder.mainView.getContext()).inflate(R.layout.label_layout, null);
            TextView labelText = (TextView) view.findViewById(R.id.label);
            labelText.setText("Grosir");
            ColorStateList tint = ColorStateList.valueOf(Color.parseColor("#44a9ec"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                labelText.setBackgroundTintList(tint);
            } else {
                ViewCompat.setBackgroundTintList(labelText, tint);
            }
            vholder.containerLabel.addView(view);
        }
        setBadge(vholder, item);
        vholder.shopName.setVisibility(View.GONE);
        vholder.location.setVisibility(View.GONE);
    }

    private void setBadge(VHolder holder, List item) {
        java.util.List<ProductItem.Badge> badgeList = item.badges;
        holder.containerBadge.removeAllViews();
        for(int i = 0; i<badgeList.size(); i++){
            ProductItem.Badge badge = badgeList.get(i);
            ImageView badgeImage = BadgeUtil.createDynamicBadge(holder.itemView.getContext());
            badgeImage.setVisibility(View.GONE);
            holder.containerBadge.addView(badgeImage);
            LuckyShopImage.loadImage(badgeImage, badge.getImageUrl());
        }
    }

    public void onItemClickListener(View.OnClickListener onClick, RecyclerView.ViewHolder holder){
        ((VHolder)holder).mainView.setOnClickListener(onClick);
    }
}
