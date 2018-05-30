package com.tokopedia.checkout.view.view.cartlist.removecartitem.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.view.cartlist.removecartitem.RemoveCartItemAdapter;
import com.tokopedia.checkout.view.view.cartlist.removecartitem.viewmodel.CartProductItemViewModel;

/**
 * @author Irfan Khoirul on 24/05/18.
 */

public class RemoveCartProductItemViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_CART_REMOVE_PRODUCT =
            R.layout.item_cart_remove_product;

    private RemoveCartItemAdapter removeCartItemAdapter;

    private CheckBox mCbRemoveProduct;
    private TextView mTvSenderName;
    private ImageView mIvProductImage;
    private TextView mTvProductName;
    private TextView mTvProductPrice;
    private TextView mTvTotalProductItem;
    private TextView mTvCashback;
    private TextView mTvPreOrder;
    private ImageView mIvFreeReturnIcon;

    public RemoveCartProductItemViewHolder(View itemView, RemoveCartItemAdapter removeCartItemAdapter) {
        super(itemView);
        this.removeCartItemAdapter = removeCartItemAdapter;
        mCbRemoveProduct = itemView.findViewById(R.id.cb_remove_product);
        mTvSenderName = itemView.findViewById(R.id.tv_shop_name);
        mIvProductImage = itemView.findViewById(R.id.iv_product_image);
        mTvProductName = itemView.findViewById(R.id.tv_product_name);
        mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
        mTvTotalProductItem = itemView.findViewById(R.id.tv_product_total_item);
        mTvCashback = itemView.findViewById(R.id.tv_cashback);
        mTvPreOrder = itemView.findViewById(R.id.tv_pre_order);
        mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
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
                        !mCbRemoveProduct.isChecked());
            }
        });
    }

    private void renderItemView(CartProductItemViewModel cartProductItemViewModel) {
        if (removeCartItemAdapter.getCheckedCartIds().contains(String.valueOf(
                cartProductItemViewModel.getCartItemData().getOriginData().getCartId()))) {
            mCbRemoveProduct.setChecked(true);
        } else {
            mCbRemoveProduct.setChecked(false);
        }
        mCbRemoveProduct.setClickable(false);
        mTvSenderName.setText(cartProductItemViewModel.getCartItemData().getOriginData().getShopName());
        mTvProductName.setText(cartProductItemViewModel.getCartItemData().getOriginData().getProductName());
        mTvProductPrice.setText(cartProductItemViewModel.getCartItemData().getOriginData().getPriceFormatted());
        mTvTotalProductItem.setText(String.valueOf(cartProductItemViewModel.getCartItemData().getUpdatedData().getQuantity()));
        ImageHandler.LoadImage(mIvProductImage, cartProductItemViewModel.getCartItemData().getOriginData().getProductImage());

        if (cartProductItemViewModel.getCartItemData().getOriginData().isFreeReturn()) {
            mIvFreeReturnIcon.setVisibility(View.VISIBLE);
        } else {
            mIvFreeReturnIcon.setVisibility(View.GONE);
        }

        if (cartProductItemViewModel.getCartItemData().getOriginData().isPreOrder()) {
            mTvPreOrder.setVisibility(View.VISIBLE);
        } else {
            mTvPreOrder.setVisibility(View.GONE);
        }

        if (cartProductItemViewModel.getCartItemData().getOriginData().isCashBack()) {
            mTvCashback.setText(cartProductItemViewModel.getCartItemData().getOriginData().getCashBackInfo());
            mTvCashback.setVisibility(View.VISIBLE);
        } else {
            mTvCashback.setVisibility(View.GONE);
        }
    }

}
