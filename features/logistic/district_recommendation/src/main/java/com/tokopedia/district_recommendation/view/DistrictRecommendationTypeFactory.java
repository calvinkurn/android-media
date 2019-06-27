package com.tokopedia.district_recommendation.view;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * Created by Irfan Khoirul on 16/11/18.
 */

public interface DistrictRecommendationTypeFactory extends AdapterTypeFactory {

    int type(AddressViewModel addressViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
