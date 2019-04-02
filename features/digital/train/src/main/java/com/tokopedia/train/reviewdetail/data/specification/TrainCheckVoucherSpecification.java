package com.tokopedia.train.reviewdetail.data.specification;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;

import java.util.Map;

/**
 * Created by Rizky on 23/07/18.
 */
public class TrainCheckVoucherSpecification implements GqlNetworkSpecification {

    private Map<String, Object> params;

    public TrainCheckVoucherSpecification(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_check_voucher_mutation;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return params;
    }

}