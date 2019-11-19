package com.tokopedia.topads.common.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.topads.R;

/**
 * Created by hadi.putra on 09/05/18.
 */

public class TopAdsErrorNetworkViewHolder extends ErrorNetworkViewHolder {
    @LayoutRes
    public final static int LAYOUT = R.layout.item_base_network_error;

    private ImageView ivIcon;
    private TextView tvMessage;
    private TextView tvRetryButton;

    public TopAdsErrorNetworkViewHolder(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.image_error);
        tvMessage = itemView.findViewById(R.id.retry_text);
        tvRetryButton = itemView.findViewById(R.id.retry_but);
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
}
