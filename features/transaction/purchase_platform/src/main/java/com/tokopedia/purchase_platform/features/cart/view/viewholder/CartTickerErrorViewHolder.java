package com.tokopedia.purchase_platform.features.cart.view.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.features.cart.view.ActionListener;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemTickerErrorHolderData;
import com.tokopedia.unifyprinciples.Typography;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartTickerErrorViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_TICKER_CART_ERROR = R.layout.holder_item_cart_ticker_error;

    private final ActionListener actionListener;
    private Typography tickerDescription;
    private Typography tickerAction;

    public CartTickerErrorViewHolder(View itemView, ActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;
        tickerDescription = itemView.findViewById(R.id.ticker_description);
        tickerAction = itemView.findViewById(R.id.ticker_action);
    }

    public void bindData(final CartItemTickerErrorHolderData data, final int position) {
        tickerDescription.setText(data.getCartTickerErrorData().getErrorInfo());
        tickerAction.setOnClickListener(v -> actionListener.onSeeErrorProductsClicked());
    }
}
