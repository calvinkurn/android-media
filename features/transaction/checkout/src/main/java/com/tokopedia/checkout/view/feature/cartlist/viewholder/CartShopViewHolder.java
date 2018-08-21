package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class CartShopViewHolder extends RecyclerView.ViewHolder implements CartItemAdapter.ActionListener {

    public static final int TYPE_VIEW_ITEM_SHOP = R.layout.item_shop;

    private TextView tvShopName;
    private ImageView imgShopBadge;
    private RecyclerView rvCartItem;

    private CartAdapter.ActionListener actionListener;

    public CartShopViewHolder(View itemView, CartAdapter.ActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;

        tvShopName = itemView.findViewById(R.id.tv_shop_name);
        imgShopBadge = itemView.findViewById(R.id.img_shop_badge);
        rvCartItem = itemView.findViewById(R.id.rv_cart_item);
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
        CartItemAdapter cartAdapter = new CartItemAdapter(this);
        cartAdapter.addDataList(cartShopHolderData.getShopGroupData().getCartItemDataList());
        rvCartItem.setLayoutManager(new LinearLayoutManager(rvCartItem.getContext()));
        rvCartItem.setAdapter(cartAdapter);
        ((SimpleItemAnimator) rvCartItem.getItemAnimator()).setSupportsChangeAnimations(false);

    }

    @Override
    public void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemQuantityReseted(int position, boolean needRefreshItemView) {

    }

    @Override
    public void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemRemarkEditChange(CartItemData cartItemData, int position, String remark) {

    }

    @Override
    public void onCartItemListIsEmpty(int shopPosition) {

    }

    @Override
    public void onCartItemQuantityFormEdited(int position, boolean needRefreshItemView) {

    }

    @Override
    public void onCartItemAfterErrorChecked() {

    }

    @Override
    public void onCartItemQuantityInputFormClicked(String qty) {

    }

    @Override
    public void onCartItemLabelInputRemarkClicked() {

    }

    @Override
    public void onQuantityChanged() {

    }
}
