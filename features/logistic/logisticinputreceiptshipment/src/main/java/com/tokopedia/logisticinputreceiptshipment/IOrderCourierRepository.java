package com.tokopedia.logisticinputreceiptshipment;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.common.data.order.ListCourierViewModel;

import java.util.Map;

import rx.Observable;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public interface IOrderCourierRepository {

    Observable<ListCourierViewModel> onOrderCourierRepository(String selectedCourierId,
                                                              TKPDMapParam<String, String> params);

    Observable<String> processShipping(TKPDMapParam<String, String> param);

    Observable<String> changeCourier(Map<String, String> param);

}
