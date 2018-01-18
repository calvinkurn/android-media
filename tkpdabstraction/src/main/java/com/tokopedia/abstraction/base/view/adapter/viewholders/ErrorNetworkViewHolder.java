package com.tokopedia.abstraction.base.view.adapter.viewholders;


import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;


/**
 * @author by erry on 02/02/17.
 */

public class ErrorNetworkViewHolder extends AbstractViewHolder<ErrorNetworkModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.design_error_network;
    private ImageView ivIcon;
    private TextView tvMessage;
    private TextView tvSubMessage;
    private TextView tvRetryButton;

    public ErrorNetworkViewHolder(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.iv_icon);
        tvMessage = itemView.findViewById(R.id.message_retry);
        tvSubMessage = itemView.findViewById(R.id.sub_message_retry);
        tvRetryButton = itemView.findViewById(R.id.button_retry);
    }

    @Override
    public void bind(final ErrorNetworkModel element) {
        if (element.getIconDrawableRes() != 0) {
            ivIcon.setImageResource(element.getIconDrawableRes());
        }
        if (element.getErrorMessage() != null && element.getErrorMessage().length() > 0) {
            tvMessage.setText(element.getErrorMessage());
        }
        tvRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ErrorNetworkModel.OnRetryListener listener = element.getOnRetryListener();
                if (listener != null) {
                    listener.onRetryClicked();
                }
            }
        });
    }

    public interface OnRetryListener {
        void onRetryClicked();
    }

}
