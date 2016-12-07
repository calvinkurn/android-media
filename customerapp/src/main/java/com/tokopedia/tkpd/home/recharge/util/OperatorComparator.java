package com.tokopedia.tkpd.home.recharge.util;

import com.tokopedia.core.database.model.RechargeOperatorModelDBAttrs;

import java.util.Comparator;

/**
 * Created by Alifa on 11/28/2016.
 */

public class OperatorComparator implements Comparator<RechargeOperatorModelDBAttrs> {

    @Override
    public int compare(RechargeOperatorModelDBAttrs o1, RechargeOperatorModelDBAttrs o2) {
        return o1.weight.compareTo(o2.weight);
    }

}