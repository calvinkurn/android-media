package com.tokopedia.flight.detail.view.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.util.FlightAmenityIconUtil;
import com.tokopedia.flight.search.data.api.single.response.Amenity;

/**
 * Created by zulfikarrahman on 10/31/17.
 */

public class FlightDetailFacilityAmenityViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView imageAmenity;
    private TextView textAmenity;
    private AppCompatImageView defaultImage;

    public FlightDetailFacilityAmenityViewHolder(View itemView) {
        super(itemView);
        imageAmenity = (AppCompatImageView) itemView.findViewById(R.id.image_amenity);
        textAmenity = (TextView) itemView.findViewById(R.id.text_amenity);
        defaultImage = (AppCompatImageView) itemView.findViewById(R.id.icon_default);
    }

    public void bindData(Amenity amenity) {
        if(amenity.isDefault()){
            imageAmenity.setVisibility(View.GONE);
            textAmenity.setVisibility(View.GONE);
            defaultImage.setVisibility(View.VISIBLE);
        }else {
            imageAmenity.setVisibility(View.VISIBLE);
            textAmenity.setVisibility(View.VISIBLE);
            defaultImage.setVisibility(View.GONE);
            imageAmenity.setImageResource(FlightAmenityIconUtil.getImageResource(amenity.getIcon()));
            textAmenity.setText(amenity.getLabel());
        }
    }
}
