package com.tokopedia.digital.widget.domain;

import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.Operator;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.status.Status;

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

    Observable<List<String>> getObservableRecentData(int categoryId);

    Observable<Boolean> storeObservableRecentDataNetwork(Map<String, String> params);

    Observable<LastOrder> getObservableLastOrderNetwork(Map<String, String> params);

}
