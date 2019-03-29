package com.tokopedia.train.checkout.data.specification;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rizky on 26/07/18.
 */
public class TrainCheckoutSpecification implements GqlNetworkSpecification {

    private Map<String, Object> params;

    public TrainCheckoutSpecification(HashMap<String, Object> params) {
        this.params = params;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_checkout_mutation;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return params;
    }
}
