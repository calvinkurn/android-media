package com.tokopedia.digital.product.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.model.OrderClientNumber;

import java.util.List;

import rx.Observable;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public interface ILastOrderNumberRepository {

    Observable<OrderClientNumber> getLastOrder(
            TKPDMapParam<String, String> param
    );

    Observable<List<OrderClientNumber>> getRecentNumberOrderList(
            TKPDMapParam<String, String> param
    );
}
