package com.tokopedia.checkout.view.common.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.common.adapter.CartAdapterActionListener;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartVoucherPromoViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_PROMO = R.layout.holder_item_cart_promo;
    private final CartAdapterActionListener actionListener;
    private TickerPromoStackingCheckoutView tickerPromoStackingCheckoutView;

    public CartVoucherPromoViewHolder(View itemView, CartAdapterActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;
        this.tickerPromoStackingCheckoutView = itemView.findViewById(R.id.voucher_cart_holder_view);
    }

    public void bindData(final PromoStackingData data, final int position) {
        tickerPromoStackingCheckoutView.setActionListener(new TickerPromoStackingCheckoutView.ActionListener() {
            @Override
            public void onClickUsePromo() {
                actionListener.onCartPromoUseVoucherGlobalPromoClicked(data, position);
            }

            @Override
            public void onDisablePromoDiscount() {
                actionListener.onCartPromoCancelVoucherPromoGlobalClicked(data, position);
                actionListener.onCartPromoGlobalTrackingCancelled(data, position);
            }

            @Override
            public void onClickDetailPromo() {
                actionListener.onClickDetailPromoGlobal(data, position);
            }
        });
        if(data.getState() != TickerPromoStackingCheckoutView.State.FAILED){
            actionListener.onCartPromoGlobalTrackingImpression(data, position);
        }
        // tickerPromoStackingCheckoutView.setState(data.getState());
        // tickerPromoStackingCheckoutView.setDesc(data.getDescription());
        // tickerPromoStackingCheckoutView.setTitle(data.getTitle());

        // tickerPromoStackingCheckoutView.setState(TickerPromoStackingCheckoutView.State.FAILED);
        tickerPromoStackingCheckoutView.setState(data.getState());
        tickerPromoStackingCheckoutView.setTitle("Gratis Ongkir Grab Sudah Terpasang");
        tickerPromoStackingCheckoutView.setDesc("Cashback Tokopedia 15rb sudah terpasang");
        tickerPromoStackingCheckoutView.setVisibility(View.VISIBLE);
    }

}
