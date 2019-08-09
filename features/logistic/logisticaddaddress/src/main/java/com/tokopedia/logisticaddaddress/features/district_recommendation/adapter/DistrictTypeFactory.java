package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.logisticaddaddress.domain.model.Address;

/**
 * Created by Irfan Khoirul on 16/11/18.
 */

public interface DistrictTypeFactory extends AdapterTypeFactory {

    int type(Address address);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
