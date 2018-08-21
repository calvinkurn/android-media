package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class CartShopViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_VIEW_ITEM_SHOP = R.layout.item_shop;

    private TextView tvShopName;
    private ImageView imgShopBadge;

    private CartAdapter.ActionListener actionListener;

    public CartShopViewHolder(View itemView, CartAdapter.ActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;

        this.tvShopName = itemView.findViewById(R.id.tv_shop_name);
        this.imgShopBadge = itemView.findViewById(R.id.img_shop_badge);
    }

    public void bindData(CartShopHolderData cartShopHolderData) {
        tvShopName.setText(cartShopHolderData.getShopGroupData().getShopName());
        tvShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onCartShopNameClicked(cartShopHolderData);
            }
        });

        if (cartShopHolderData.getShopGroupData().isOfficialStore()) {
            imgShopBadge.setImageDrawable(ContextCompat.getDrawable(imgShopBadge.getContext(), R.drawable.ic_badge_official));
            imgShopBadge.setVisibility(View.VISIBLE);
        } else if (cartShopHolderData.getShopGroupData().isGoldMerchant()) {
            imgShopBadge.setImageDrawable(ContextCompat.getDrawable(imgShopBadge.getContext(), R.drawable.ic_shop_gold));
            imgShopBadge.setVisibility(View.VISIBLE);
        } else {
            imgShopBadge.setVisibility(View.GONE);
        }

        // Todo : setup inner recyclerview

    }

}
