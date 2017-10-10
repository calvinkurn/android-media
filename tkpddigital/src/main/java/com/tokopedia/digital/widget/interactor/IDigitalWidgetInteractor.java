package com.tokopedia.digital.widget.interactor;

import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/21/17.
 * Modified by rizkyfadillah at 10/9/17.
 */

public interface IDigitalWidgetInteractor {

    void getProductsFromPrefix(Subscriber<List<Product>> subscriber, int categoryId, String prefix, Boolean validatePrefix);

    void getOperatorsFromCategory(Subscriber<List<Operator>> subscriber, int categoryId);

    void getProductsFromOperator(Subscriber<List<Product>> subscriber, int categoryId, String operatorId);

    void getOperatorById(Subscriber<Operator> subscriber, String operatorId);

    void getProductById(Subscriber<Product> subscriber, String categoryId, String operatorId, String productId);

    void getNumberList(Subscriber<DigitalNumberList> subscriber,
                       TKPDMapParam<String, String> param);

    void onDestroy();

}
