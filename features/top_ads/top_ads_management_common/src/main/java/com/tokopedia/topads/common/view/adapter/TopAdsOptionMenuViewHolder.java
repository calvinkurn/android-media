package com.tokopedia.topads.common.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.topads.common.R;
import com.tokopedia.topads.common.data.model.TopAdsOptionMenu;

/**
 * Created by hadi.putra on 24/05/18.
 */

public class TopAdsOptionMenuViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private ImageView checkImageView;

    public TopAdsOptionMenuViewHolder(View itemView) {
        super(itemView);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        checkImageView = (ImageView) itemView.findViewById(R.id.image_view_check);
    }

    public void onBind(TopAdsOptionMenu optionMenu, boolean isSelected){
        titleTextView.setText(optionMenu.getTitle());
        if (isSelected){
            checkImageView.setVisibility(View.VISIBLE);
        } else {
            checkImageView.setVisibility(View.GONE);
        }
    }
}
