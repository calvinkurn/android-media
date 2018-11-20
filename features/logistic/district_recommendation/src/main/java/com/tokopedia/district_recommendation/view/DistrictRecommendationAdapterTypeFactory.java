package com.tokopedia.district_recommendation.view;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * Created by Irfan Khoirul on 16/11/18.
 */

public class DistrictRecommendationAdapterTypeFactory extends BaseAdapterTypeFactory implements DistrictRecommendationTypeFactory {

    public DistrictRecommendationAdapterTypeFactory() {
    }

    @Override
    public int type(AddressViewModel addressViewModel) {
        return DistrictRecommendationViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == DistrictRecommendationViewHolder.LAYOUT) {
            return new DistrictRecommendationViewHolder(parent);
        }
        return super.createViewHolder(parent, type);
    }

}
