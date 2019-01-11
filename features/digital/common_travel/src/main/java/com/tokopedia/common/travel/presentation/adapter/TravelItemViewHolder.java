package com.tokopedia.common.travel.presentation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.common.travel.R;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public class TravelItemViewHolder extends RecyclerView.ViewHolder {

    public TextView passengerName;
    public TextView passengerStatus;
    public RelativeLayout passengerLayout;

    public TravelItemViewHolder(View itemView) {
        super(itemView);

        passengerName = itemView.findViewById(R.id.passenger_name);
        passengerStatus = itemView.findViewById(R.id.passenger_status);
        passengerLayout = itemView.findViewById(R.id.layout_passenger);
    }
}
