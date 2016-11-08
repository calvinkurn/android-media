package com.tokopedia.core.shopinfo.adapter;

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
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.shopinfo.models.productmodel.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tkpd_Eka on 10/22/2015.
 */
public class ProductListDelegate {

    public class VHolder extends RecyclerView.ViewHolder {

        @Bind(R2.id.product_image)
        public ImageView img;
        @Bind(R2.id.title)
        public TextView name;
        @Bind(R2.id.price)
        public TextView price;
        @Bind(R2.id.container)
        public View mainView;
        @Bind(R2.id.shop_name)
        public View shopName;
        @Bind(R2.id.location)
        public View location;
        @Bind(R2.id.label_container)
        public FlowLayout labelContainer;

        public VHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_product_item_list, parent, false);
        parent.getContext();
        return new VHolder(view);
    }

    public void onBindViewHolder(List item, RecyclerView.ViewHolder holder) {
        VHolder vholder = (VHolder) holder;
        ImageHandler.loadImageFit2(vholder.img.getContext(), vholder.img, item.productImage300);
        vholder.name.setText(Html.fromHtml(item.productName));
        vholder.price.setText(item.productPrice);
        vholder.labelContainer.removeAllViews();
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
            vholder.labelContainer.addView(view);
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
            vholder.labelContainer.addView(view);
        }
        vholder.shopName.setVisibility(View.GONE);
        vholder.location.setVisibility(View.GONE);
    }

    public void onItemClickListener(View.OnClickListener onClick, RecyclerView.ViewHolder holder) {
        ((VHolder) holder).mainView.setOnClickListener(onClick);
    }

}
