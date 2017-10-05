package com.tokopedia.digital.widget.interactor;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/21/17.
 */

public interface IDigitalWidgetInteractor {

    void getProductsFromPrefix(Subscriber<List<Product>> subscriber, int categoryId, String prefix, Boolean validatePrefix);

    void getOperatorsFromCategory(Subscriber<List<RechargeOperatorModel>> subscriber, int categoryId);

    void getProductsFromOperator(Subscriber<List<Product>> subscriber, int categoryId, String operatorId);

    void getOperatorById(Subscriber<RechargeOperatorModel> subscriber, String operatorId);

    void getProductById(Subscriber<Product> subscriber, String categoryId, String operatorId, String productId);

    void getNumberList(Subscriber<DigitalNumberList> subscriber,
                       TKPDMapParam<String, String> param);

    void getLastOrderByCategoryId(Subscriber<LastOrder> subscriber, int categoryId);

    void onDestroy();

}
