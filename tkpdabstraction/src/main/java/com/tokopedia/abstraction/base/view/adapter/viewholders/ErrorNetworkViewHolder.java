package com.tokopedia.abstraction.base.view.adapter.viewholders;


import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;


/**
 * @author by erry on 02/02/17.
 */

public class ErrorNetworkViewHolder extends AbstractViewHolder<ErrorNetworkModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.partial_empty_page_error;
    private ImageView ivIcon;
    private TextView tvMessage;
    private TextView tvSubMessage;
    private Button tvRetryButton;

    public ErrorNetworkViewHolder(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.iv_icon);
        tvMessage = itemView.findViewById(R.id.message_retry);
        tvSubMessage = itemView.findViewById(R.id.sub_message_retry);
        tvRetryButton = itemView.findViewById(R.id.button_retry);
    }

    @Override
    public void bind(final ErrorNetworkModel errorNetworkModel) {
        if (errorNetworkModel.getIconDrawableRes() != 0) {
            ivIcon.setImageResource(errorNetworkModel.getIconDrawableRes());
        }
        if (errorNetworkModel.getErrorMessage() != null && errorNetworkModel.getErrorMessage().length() > 0) {
            tvMessage.setText(errorNetworkModel.getErrorMessage());
        }
        tvRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ErrorNetworkModel.OnRetryListener listener = errorNetworkModel.getOnRetryListener();
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
