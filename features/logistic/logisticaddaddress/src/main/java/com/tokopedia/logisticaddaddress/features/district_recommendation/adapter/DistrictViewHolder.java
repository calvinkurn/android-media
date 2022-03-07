package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.domain.model.Address;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public class DistrictViewHolder extends AbstractViewHolder<Address> {

    public static final int LAYOUT = R.layout.listview_district_recomendation;

    private Typography tvAddress;
    
    public DistrictViewHolder(View itemView) {
        super(itemView);

        tvAddress = itemView.findViewById(R.id.tvAddress);
    }

    @Override
    public void bind(Address address) {

        String completeAddress = address.getProvinceName() +
                ", " +
                address.getCityName() +
                ", " +
                address.getDistrictName();

        tvAddress.setText(completeAddress);
    }

}
