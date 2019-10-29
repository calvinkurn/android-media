package com.tokopedia.flight.detail.view.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.flight.detail.view.model.FlightDetailRouteInfoViewModel;

/**
 * Created by zulfikarrahman on 10/31/17.
 */

public class FlightDetailFacilityInfoViewHolder extends RecyclerView.ViewHolder {

    private TextView titleInfo;
    private TextView descInfo;

    public FlightDetailFacilityInfoViewHolder(View itemView) {
        super(itemView);
        titleInfo = (TextView) itemView.findViewById(com.tokopedia.flight.R.id.title_info);
        descInfo = (TextView) itemView.findViewById(com.tokopedia.flight.R.id.desc_info);
    }

    public void bindData(FlightDetailRouteInfoViewModel info) {
        titleInfo.setText(info.getLabel());
        descInfo.setText(info.getValue());
    }
}
