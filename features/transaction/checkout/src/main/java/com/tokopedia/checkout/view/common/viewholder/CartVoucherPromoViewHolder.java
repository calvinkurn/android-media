package com.tokopedia.checkout.view.common.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.common.adapter.CartAdapterActionListener;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartVoucherPromoViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_PROMO = R.layout.holder_item_cart_promo;
    private final CartAdapterActionListener actionListener;
    private TickerCheckoutView tickerCheckoutView;

    public CartVoucherPromoViewHolder(View itemView, CartAdapterActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;
        this.tickerCheckoutView = itemView.findViewById(R.id.voucher_cart_holder_view);
    }

    public void bindData(final PromoData data, final int position) {
        tickerCheckoutView.setActionListener(new TickerCheckoutView.ActionListener() {
            @Override
            public void onClickUsePromo() {
                actionListener.onCartPromoUseVoucherPromoClicked(data, position);
            }

            @Override
            public void onDisablePromoDiscount() {
                actionListener.onCartPromoCancelVoucherPromoClicked(data, position);
                actionListener.onCartPromoTrackingCancelled(data, position);
            }

            @Override
            public void onClickDetailPromo() {
                actionListener.onClickDetailPromo(data, position);
            }
        });
        if(data.getState() != TickerCheckoutView.State.FAILED){
            actionListener.onCartPromoTrackingImpression(data, position);
        }
        tickerCheckoutView.setState(data.getState());
        tickerCheckoutView.setDesc(data.getDescription());
        tickerCheckoutView.setTitle(data.getTitle());
        tickerCheckoutView.setVisibility(View.VISIBLE);
    }

}
