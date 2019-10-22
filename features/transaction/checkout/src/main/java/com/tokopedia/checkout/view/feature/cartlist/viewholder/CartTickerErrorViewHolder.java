package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.feature.cartlist.ActionListener;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;

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
        this.errorTicker.setDescriptionClickEvent(new TickerCallback() {
            @Override
            public void onDescriptionViewClick(CharSequence charSequence) {
                actionListener.onCartItemTickerErrorActionClicked(data, position);
            }

            @Override
            public void onDismiss() {

            }
        });
        // Do not send empty link
        this.errorTicker.setHtmlDescription(itemView.getContext().getString(R.string.ticker_action_link, data.getCartTickerErrorData().getActionInfo(), data.getCartTickerErrorData().getActionInfo()));
        this.errorTicker.setTickerType(Ticker.TYPE_WARNING);
        this.errorTicker.setTickerShape(Ticker.SHAPE_LOOSE);
        this.errorTicker.setCloseButtonVisibility(View.GONE);
        // Request layout to update ticker from reused view
        this.errorTicker.requestLayout();
    }
}
