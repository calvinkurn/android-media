package com.tokopedia.checkout.view.feature.removecartitem.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.removecartitem.RemoveCartItemAdapter;
import com.tokopedia.checkout.view.feature.removecartitem.viewmodel.CartProductItemViewModel;

/**
 * @author Irfan Khoirul on 24/05/18.
 */

public class RemoveCartProductItemViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_CART_REMOVE_PRODUCT =
            R.layout.item_cart_remove_product;

    private RemoveCartItemAdapter removeCartItemAdapter;

    private CheckBox cbRemoveProduct;
    private TextView tvSenderName;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvTotalProductItem;
    private TextView tvCashback;
    private TextView tvPreOrder;
    private ImageView imgFreeReturnIcon;
    private ImageView imgShopBadge;

    public RemoveCartProductItemViewHolder(View itemView, RemoveCartItemAdapter removeCartItemAdapter) {
        super(itemView);
        this.removeCartItemAdapter = removeCartItemAdapter;
        cbRemoveProduct = itemView.findViewById(R.id.cb_remove_product);
        tvSenderName = itemView.findViewById(R.id.tv_shop_name);
        ivProductImage = itemView.findViewById(R.id.iv_product_image);
        tvProductName = itemView.findViewById(R.id.tv_product_name);
        tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        tvTotalProductItem = itemView.findViewById(R.id.tv_product_total_item);
        tvCashback = itemView.findViewById(R.id.tv_cashback);
        tvPreOrder = itemView.findViewById(R.id.tv_pre_order);
        imgFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
        imgShopBadge = itemView.findViewById(R.id.img_shop_badge);
    }

    public void bindData(CartProductItemViewModel cartProductItemViewModel) {
        renderItemView(cartProductItemViewModel);
        renderViewListener(cartProductItemViewModel);
    }

    private void renderViewListener(final CartProductItemViewModel cartProductItemViewModel) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCartItemAdapter.updateCheckedCartId(getAdapterPosition(),
                        String.valueOf(cartProductItemViewModel.getCartItemData().getOriginData().getCartId()),
                        !cbRemoveProduct.isChecked());
            }
        });
    }

    private void renderItemView(CartProductItemViewModel cartProductItemViewModel) {
        if (removeCartItemAdapter.getCheckedCartIds().contains(String.valueOf(
                cartProductItemViewModel.getCartItemData().getOriginData().getCartId()))) {
            cbRemoveProduct.setChecked(true);
        } else {
            cbRemoveProduct.setChecked(false);
        }
        cbRemoveProduct.setClickable(false);
        tvProductName.setText(cartProductItemViewModel.getCartItemData().getOriginData().getProductName());
        tvProductPrice.setText(cartProductItemViewModel.getCartItemData().getOriginData().getPriceFormatted());
        tvTotalProductItem.setText(String.valueOf(cartProductItemViewModel.getCartItemData().getUpdatedData().getQuantity()));
        ImageHandler.LoadImage(ivProductImage, cartProductItemViewModel.getCartItemData().getOriginData().getProductImage());

        if (cartProductItemViewModel.getCartItemData().getOriginData().isFreeReturn()) {
            imgFreeReturnIcon.setVisibility(View.VISIBLE);
        } else {
            imgFreeReturnIcon.setVisibility(View.GONE);
        }

        if (cartProductItemViewModel.getCartItemData().getOriginData().isPreOrder()) {
            tvPreOrder.setText(cartProductItemViewModel.getCartItemData().getOriginData().getPreOrderInfo());
            tvPreOrder.setVisibility(View.VISIBLE);
        } else {
            tvPreOrder.setVisibility(View.GONE);
        }

        if (cartProductItemViewModel.getCartItemData().getOriginData().isCashBack()) {
            tvCashback.setText(cartProductItemViewModel.getCartItemData().getOriginData().getCashBackInfo());
            tvCashback.setVisibility(View.VISIBLE);
        } else {
            tvCashback.setVisibility(View.GONE);
        }
    }

}
