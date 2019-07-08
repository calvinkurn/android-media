package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.feature.cartlist.ActionListener;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartTickerErrorViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_TICKER_CART_ERROR = R.layout.holder_item_cart_ticker_error;

    private final ActionListener actionListener;
    private TextView tvErrorMessage;
    private TextView tvBtnAction;

    public CartTickerErrorViewHolder(View itemView, ActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;
        this.tvErrorMessage = itemView.findViewById(R.id.tv_error);
        this.tvBtnAction = itemView.findViewById(R.id.btn_error_action);
    }

    public void bindData(final CartItemTickerErrorHolderData data, final int position) {
        this.tvErrorMessage.setText(data.getCartTickerErrorData().getErrorInfo());
        this.tvBtnAction.setText(data.getCartTickerErrorData().getActionInfo());
        this.tvBtnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartItemTickerErrorActionClicked(data, position);
            }
        });
    }
}
