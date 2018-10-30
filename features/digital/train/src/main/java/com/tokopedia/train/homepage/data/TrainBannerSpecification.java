package com.tokopedia.train.homepage.data;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;

import java.util.Map;

public class TrainBannerSpecification implements GqlNetworkSpecification {
    private Map<String, Object> params;

    public TrainBannerSpecification(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_promo_banners_query;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return params;
    }
}
