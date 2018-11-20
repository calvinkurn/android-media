package com.tokopedia.district_recommendation.view;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.district_recommendation.R;
import com.tokopedia.district_recommendation.domain.model.Address;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public class DistrictRecommendationViewHolder extends AbstractViewHolder<AddressViewModel> {

    public static final int LAYOUT = R.layout.listview_district_recomendation;

    private TextView tvAddress;
    
    public DistrictRecommendationViewHolder(View itemView) {
        super(itemView);

        tvAddress = itemView.findViewById(R.id.tvAddress);
    }

    @Override
    public void bind(AddressViewModel addressViewModel) {
        Address address = addressViewModel.getAddress();

        String completeAddress = address.getProvinceName() +
                ", " +
                address.getCityName() +
                ", " +
                address.getDistrictName();

        tvAddress.setText(completeAddress);
    }

}
