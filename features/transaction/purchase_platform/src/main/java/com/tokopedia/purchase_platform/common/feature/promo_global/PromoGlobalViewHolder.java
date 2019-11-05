package com.tokopedia.purchase_platform.common.feature.promo_global;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class PromoGlobalViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_PROMO = R.layout.holder_item_cart_promo;
    private final PromoActionListener actionListener;
    private TickerPromoStackingCheckoutView tickerPromoStackingCheckoutView;

    public PromoGlobalViewHolder(View itemView, PromoActionListener actionListener) {
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
            public void onResetPromoDiscount() {
                actionListener.onCartPromoCancelVoucherPromoGlobalClicked(data, position);
                actionListener.onPromoGlobalTrackingCancelled(data, position);
            }

            @Override
            public void onClickDetailPromo() {
                actionListener.onClickDetailPromoGlobal(data, position);
            }

            @Override
            public void onDisablePromoDiscount() {

            }
        });
        tickerPromoStackingCheckoutView.setState(data.getState());

        if (data.getState() == TickerPromoStackingCheckoutView.State.EMPTY) {
            tickerPromoStackingCheckoutView.setTitle(data.getTitleDefault());
            tickerPromoStackingCheckoutView.setCounterCoupons(data.getCounterLabelDefault());
        } else {
            tickerPromoStackingCheckoutView.setTitle(data.getTitle());
        }

        tickerPromoStackingCheckoutView.setDesc(data.getDescription());
        tickerPromoStackingCheckoutView.setVisibility(View.VISIBLE);
    }

}
