package com.tokopedia.digital.widget.listener;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.digital.product.model.OrderClientNumber;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/8/17.
 */

public interface IDigitalWidgetStyle1View extends BaseDigitalWidgetView {

    void renderDataProducts(List<Product> products);

    void renderEmptyProduct(String message);

    void renderDataOperator(RechargeOperatorModel operatorModel);

    void renderEmptyOperator(String message);

    void renderProduct(Product product);

    void renderOperator(RechargeOperatorModel operatorModel);

//    void renderLastOrder(LastOrder lastOrder);

    void renderErrorProduct(String message);

    void renderDefaultError();

}
