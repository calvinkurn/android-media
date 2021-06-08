package com.tokopedia.flight.detail.view.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.flight.detail.util.FlightAmenityIconUtil;
import com.tokopedia.flight.search.data.cloud.single.Amenity;

/**
 * Created by zulfikarrahman on 10/31/17.
 */

public class FlightDetailFacilityAmenityViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView imageAmenity;
    private TextView textAmenity;

    public FlightDetailFacilityAmenityViewHolder(View itemView) {
        super(itemView);
        imageAmenity = (AppCompatImageView) itemView.findViewById(com.tokopedia.flight.R.id.image_amenity);
        textAmenity = (TextView) itemView.findViewById(com.tokopedia.flight.R.id.text_amenity);
    }

    public void bindData(Amenity amenity) {
        if (amenity.isDefault()) {
            imageAmenity.setVisibility(View.GONE);
            textAmenity.setVisibility(View.GONE);
        } else {
            imageAmenity.setVisibility(View.VISIBLE);
            textAmenity.setVisibility(View.VISIBLE);
            imageAmenity.setImageDrawable(MethodChecker.getDrawable(imageAmenity.getContext(), FlightAmenityIconUtil.getImageResource(amenity.getIcon())));
            textAmenity.setText(amenity.getLabel());
        }
    }

    public void bindData(com.tokopedia.flight.orderlist.data.cloud.entity.Amenity amenity) {
        if (amenity.isDefault()) {
            imageAmenity.setVisibility(View.GONE);
            textAmenity.setVisibility(View.GONE);
        } else {
            imageAmenity.setVisibility(View.VISIBLE);
            textAmenity.setVisibility(View.VISIBLE);
            imageAmenity.setImageDrawable(MethodChecker.getDrawable(imageAmenity.getContext(), FlightAmenityIconUtil.getImageResource(amenity.getIcon())));
            textAmenity.setText(amenity.getLabel());
        }
    }
}
