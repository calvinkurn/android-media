package com.tokopedia.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.adapter.CartAdapterActionListener;
import com.tokopedia.checkout.view.compoundview.VoucherPromoView;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.design.voucher.VoucherCartHachikoView;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartVoucherPromoViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_PROMO = R.layout.holder_item_cart_promo;
    private final CartAdapterActionListener actionListener;
    private VoucherPromoView voucherCartHachikoView;
    private CartItemPromoHolderData cartItemPromoHolderData;

    public CartVoucherPromoViewHolder(View itemView, CartAdapterActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;
        this.voucherCartHachikoView = itemView.findViewById(R.id.voucher_cart_holder_view);
    }

    public void bindData(final CartItemPromoHolderData data, final int position) {
        cartItemPromoHolderData = data;
        this.voucherCartHachikoView.setActionListener(new VoucherCartHachikoView.ActionListener() {
            @Override
            public void onClickUseVoucher() {
                actionListener.onCartPromoUseVoucherPromoClicked(data, position);
            }

            @Override
            public void disableVoucherDiscount() {
                actionListener.onCartPromoCancelVoucherPromoClicked(data, position);
            }

            @Override
            public void trackingSuccessVoucher(String voucherName) {
                actionListener.onCartPromoTrackingSuccess(data, position);
            }

            @Override
            public void trackingCancelledVoucher() {
                actionListener.onCartPromoTrackingCancelled(data, position);
            }
        });

        if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON) {
            voucherCartHachikoView.setCoupon(data.getCouponTitle(),
                    data.getCouponMessage(),
                    data.getCouponCode()
            );
        } else if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER) {
            voucherCartHachikoView.setVoucher(data.getVoucherCode(),
                    data.getVoucherMessage()
            );
        } else {
            voucherCartHachikoView.setPromoAndCouponLabel();
            voucherCartHachikoView.resetView();
        }
    }

    public CartItemPromoHolderData getCartItemPromoHolderData() {
        return cartItemPromoHolderData;
    }
}
