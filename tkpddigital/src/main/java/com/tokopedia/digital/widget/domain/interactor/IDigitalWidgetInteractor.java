package com.tokopedia.digital.widget.domain.interactor;

import android.support.v4.util.Pair;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;
import com.tokopedia.digital.widget.view.model.operator.Operator;
import com.tokopedia.digital.widget.view.model.product.Product;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/21/17.
 * Modified by rizkyfadillah at 10/9/17.
 */
@Deprecated
public interface IDigitalWidgetInteractor {

    void getOperatorAndProductsFromPrefix(Subscriber<Pair<Operator, List<Product>>> subscriber, int categoryId, String prefix);

    void getOperatorAndProductsByOperatorId(Subscriber<Pair<Operator, List<Product>>> subscriber,
                                                  int categoryId, String operatorId);

    void getOperatorsByCategoryId(Subscriber<List<Operator>> subscriber, int categoryId);

    void getProductsByOperatorId(Subscriber<List<Product>> subscriber, int categoryId, String operatorId);

    void getOperatorById(Subscriber<Operator> subscriber, String operatorId);

    void getProductById(Subscriber<Product> subscriber, String categoryId, String operatorId, String productId);

    void getNumberList(Subscriber<DigitalNumberList> subscriber,
                       TKPDMapParam<String, String> param);

}
