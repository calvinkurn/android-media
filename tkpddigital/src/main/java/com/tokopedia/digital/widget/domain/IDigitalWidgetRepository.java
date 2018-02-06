package com.tokopedia.digital.widget.domain.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.widget.data.entity.category.CategoryEntity;
import com.tokopedia.digital.widget.data.entity.operator.OperatorEntity;
import com.tokopedia.digital.widget.data.entity.product.ProductEntity;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 7/28/17.
 * Modified by rizkyfadillah at 10/6/17.
 */
@Deprecated
public interface IDigitalWidgetRepository {

    Observable<List<CategoryEntity>> getObservableCategoryData();

    Observable<List<ProductEntity>> getObservableProducts();

    Observable<List<OperatorEntity>> getObservableOperators();

    Observable<DigitalNumberList> getObservableNumberList(TKPDMapParam<String, String> param);

}
