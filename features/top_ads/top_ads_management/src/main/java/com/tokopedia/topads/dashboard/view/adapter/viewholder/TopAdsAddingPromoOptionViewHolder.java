package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.base.list.seller.view.adapter.BaseViewHolder;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.model.TopAdsAddingPromoOptionModel;

/**
 * Created by hadi.putra on 26/04/18.
 */

public class TopAdsAddingPromoOptionViewHolder extends BaseViewHolder<TopAdsAddingPromoOptionModel> {
    private ImageView icon;
    private TextView titleTextView;
    private TextView subtitleTextView;

    public TopAdsAddingPromoOptionViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.image_view);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        subtitleTextView = (TextView) itemView.findViewById(R.id.text_view_sub_title);
    }

    @Override
    public void bindObject(TopAdsAddingPromoOptionModel topAdsAddingPromoOptionModel) {

        titleTextView.setText(topAdsAddingPromoOptionModel.getTitleOption());
        subtitleTextView.setText(topAdsAddingPromoOptionModel.getSubtitleOption());
        icon.setImageResource(topAdsAddingPromoOptionModel.getResIcon());

    }
}
