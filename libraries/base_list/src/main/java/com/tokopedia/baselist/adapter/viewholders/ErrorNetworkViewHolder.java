package com.tokopedia.baselist.adapter.viewholders;


import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.LayoutRes;

import com.tokopedia.baselist.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;


/**
 * @author by erry on 02/02/17.
 */

public class ErrorNetworkViewHolder extends AbstractViewHolder<ErrorNetworkModel> {
    @LayoutRes
    public final static int LAYOUT = com.tokopedia.abstraction.R.layout.partial_empty_page_error;
    private ImageView ivIcon;
    private TextView tvMessage;
    private TextView tvSubMessage;
    private Button tvRetryButton;
    private Context context;

    public ErrorNetworkViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ivIcon = itemView.findViewById(com.tokopedia.abstraction.R.id.iv_icon);
        tvMessage = itemView.findViewById(com.tokopedia.abstraction.R.id.message_retry);
        tvSubMessage = itemView.findViewById(com.tokopedia.abstraction.R.id.sub_message_retry);
        tvRetryButton = itemView.findViewById(com.tokopedia.abstraction.R.id.button_retry);
    }

    @Override
    public void bind(final ErrorNetworkModel errorNetworkModel) {
        if (errorNetworkModel.getIconDrawableRes() != 0) {
            ivIcon.setImageDrawable(MethodChecker.getDrawable(context, errorNetworkModel.getIconDrawableRes()));
        }
        if (errorNetworkModel.getErrorMessage() != null && errorNetworkModel.getErrorMessage().length() > 0) {
            tvMessage.setText(errorNetworkModel.getErrorMessage());
        }
        if (errorNetworkModel.getSubErrorMessage() != null && errorNetworkModel.getSubErrorMessage().length() > 0) {
            tvSubMessage.setText(errorNetworkModel.getSubErrorMessage());
        }
        tvRetryButton.setOnClickListener(new View .OnClickListener() {
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
