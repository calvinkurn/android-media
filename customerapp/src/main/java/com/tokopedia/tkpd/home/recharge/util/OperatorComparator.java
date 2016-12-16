package com.tokopedia.tkpd.home.recharge.util;

import com.tokopedia.core.database.model.RechargeOperatorModel;

import java.util.Comparator;

/**
 * Created by Alifa on 11/28/2016.
 */

public class OperatorComparator implements Comparator<RechargeOperatorModel> {

    @Override
    public int compare(RechargeOperatorModel o1, RechargeOperatorModel o2) {
        return o1.weight.compareTo(o2.weight);
    }

}