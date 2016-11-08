package com.tokopedia.core.shopinfo.adapter;

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
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.shopinfo.models.productmodel.List;
import com.tokopedia.core.var.ProductItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tkpd_Eka on 10/9/2015.
 */
public class ProductMediumDelegate {

    public class VHolder extends RecyclerView.ViewHolder {
        @Bind(R2.id.img)
        public SquareImageView img;
        @Bind(R2.id.product_name)
        public TextView name;
        @Bind(R2.id.product_price)
        public TextView price;
        @Bind(R2.id.preorder)
        public View preorder;
        @Bind(R2.id.grosir)
        public View wholesale;
        public View mainView;
        @Bind(R2.id.container_badge)
        public LinearLayout containerBadge;

        public VHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mainView = itemView;
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_medium, parent, false);
        return new VHolder(view);
    }

    public void onBindViewHolder(List item, RecyclerView.ViewHolder holder) {
        if (item == null)
            return;
        VHolder vholder = (VHolder) holder;
        ImageHandler.loadImageFit2(vholder.itemView.getContext(), vholder.img, item.productImage300);
        vholder.name.setText(Html.fromHtml(item.productName));
        vholder.price.setText(item.productPrice);
        if (item.productPreorder != null)
            vholder.preorder.setVisibility(item.productPreorder.equals("0") ? View.INVISIBLE : View.VISIBLE);
        else
            vholder.preorder.setVisibility(View.INVISIBLE);
        if (item.productWholesale != null)
            vholder.wholesale.setVisibility(item.productWholesale.equals("0") ? View.GONE : View.VISIBLE);
        else
            vholder.wholesale.setVisibility(View.GONE);
        setBadge(vholder, item);

    }

    private void setBadge(VHolder holder, List item) {
        java.util.List<ProductItem.Badge> badgeList = item.badges;
        holder.containerBadge.removeAllViews();
        for (int i = 0; i < badgeList.size(); i++) {
            ProductItem.Badge badge = badgeList.get(i);
            ImageView badgeImage = BadgeUtil.createDynamicBadge(holder.itemView.getContext());
            badgeImage.setVisibility(View.GONE);
            holder.containerBadge.addView(badgeImage);
            LuckyShopImage.loadImage(badgeImage, badge.getImageUrl());
        }
    }

    public void onItemClickListener(View.OnClickListener onClick, RecyclerView.ViewHolder holder) {
        ((VHolder) holder).mainView.setOnClickListener(onClick);
    }

}
