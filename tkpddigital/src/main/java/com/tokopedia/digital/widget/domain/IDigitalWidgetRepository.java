package com.tokopedia.digital.widget.domain;

import com.tokopedia.core.database.model.RechargeNumberListModelDB;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.Operator;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by nabillasabbaha on 7/28/17.
 */

public interface IDigitalWidgetRepository {

    Observable<CategoryData> getObservableCategoryData();

    Observable<List<Product>> getObservableProducts();

    Observable<List<Operator>> getObservableOperators();

    Observable<Status> getObservableStatus();

    Observable<Status> getObservableStatusOnResume();

    Observable<DigitalNumberList> getObservableNumberList(TKPDMapParam<String, String> param);

//    Observable<Boolean> storeObservableNumberListNetwork(Map<String, String> params);

//    Observable<LastOrder> getObservableLastOrderNetwork(Map<String, String> params);

    Observable<LastOrder> getObservableLastOrderFromDBByCategoryId(int categoryId);

    Observable<Boolean> hasLastOrder(int categoryId);

}
