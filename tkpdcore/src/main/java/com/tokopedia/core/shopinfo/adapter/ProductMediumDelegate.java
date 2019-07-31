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
import com.tokopedia.core2.R;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.shopinfo.models.productmodel.List;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.util.MethodChecker;


/**
 * Created by Tkpd_Eka on 10/9/2015.
 */
public class ProductMediumDelegate {

    public class VHolder extends RecyclerView.ViewHolder {
        public SquareImageView img;
        public TextView name;
        public TextView price;
        public View preorder;
        public View wholesale;
        public View mainView;
        public LinearLayout containerBadge;

        public VHolder(View itemView) {
            super(itemView);

            img = (SquareImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.prod_name);
            price = (TextView) itemView.findViewById(R.id.prod_price);
            preorder = (View) itemView.findViewById(R.id.preorder);
            wholesale = (View) itemView.findViewById(R.id.grosir);
            containerBadge = (LinearLayout) itemView.findViewById(R.id.container_badge);
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
        vholder.name.setText(MethodChecker.fromHtml(item.productName));
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
        java.util.List<Badge> badgeList = item.badges;
        holder.containerBadge.removeAllViews();
        for (int i = 0; i < badgeList.size(); i++) {
            Badge badge = badgeList.get(i);
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
