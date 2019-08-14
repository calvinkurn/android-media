package com.tokopedia.purchase_platform.features.cart.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.features.cart.view.ActionListener;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemTickerErrorHolderData;
import com.tokopedia.unifycomponents.ticker.Ticker;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartTickerErrorViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_TICKER_CART_ERROR = R.layout.holder_item_cart_ticker_error;

    private final ActionListener actionListener;
    private Ticker errorTicker;

    public CartTickerErrorViewHolder(View itemView, ActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;
        this.errorTicker = itemView.findViewById(R.id.ticker_error);
    }

    public void bindData(final CartItemTickerErrorHolderData data, final int position) {
        this.errorTicker.setTickerTitle(data.getCartTickerErrorData().getErrorInfo());
        this.errorTicker.setTextDescription(data.getCartTickerErrorData().getActionInfo());
        this.errorTicker.setTickerType(Ticker.TYPE_WARNING);
        this.errorTicker.setTickerShape(Ticker.SHAPE_LOOSE);
        this.errorTicker.setCloseButtonVisibility(View.GONE);
        this.errorTicker.setOnClickListener(view -> actionListener.onCartItemTickerErrorActionClicked(data, position));
    }
}
