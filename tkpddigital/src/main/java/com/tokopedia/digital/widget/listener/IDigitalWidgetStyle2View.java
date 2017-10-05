package com.tokopedia.digital.widget.listener;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.digital.product.model.OrderClientNumber;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/8/17.
 */

public interface IDigitalWidgetStyle2View extends BaseDigitalWidgetView {

    void renderDataProducts(List<Product> products);

    void renderEmptyProduct(String message);

    void renderOperators(List<RechargeOperatorModel> operatorModels);

    void renderEmptyOperators(String message);

//    void renderDataRecent(List<String> results);

//    void renderNumberList(List<OrderClientNumber> results);

    void renderProduct(Product product);

    void renderOperator(RechargeOperatorModel rechargeOperatorModel);

    void renderErrorMessage(String message);
}
