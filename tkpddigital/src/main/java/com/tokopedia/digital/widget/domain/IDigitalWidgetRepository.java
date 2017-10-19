package com.tokopedia.digital.widget.domain;

import com.tokopedia.digital.widget.data.entity.category.CategoryEntity;
import com.tokopedia.digital.widget.data.entity.lastorder.LastOrderEntity;
import com.tokopedia.digital.widget.data.entity.operator.OperatorEntity;
import com.tokopedia.digital.widget.data.entity.product.ProductEntity;
import com.tokopedia.digital.widget.data.entity.status.StatusEntity;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by nabillasabbaha on 7/28/17.
 */

public interface IDigitalWidgetRepository {

    Observable<List<CategoryEntity>> getObservableCategoryData();

    Observable<List<ProductEntity>> getObservableProducts();

    Observable<List<OperatorEntity>> getObservableOperators();

    Observable<StatusEntity> getObservableStatus();

    Observable<List<String>> getObservableRecentData(int categoryId);

    Observable<Boolean> storeObservableRecentDataNetwork(Map<String, String> params);

    Observable<LastOrderEntity> getObservableLastOrderNetwork(Map<String, String> params);


}
