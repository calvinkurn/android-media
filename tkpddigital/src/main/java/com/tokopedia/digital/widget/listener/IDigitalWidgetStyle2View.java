package com.tokopedia.digital.widget.listener;

import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;
import com.tokopedia.digital.product.model.OrderClientNumber;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/8/17.
 */

public interface IDigitalWidgetStyle2View extends BaseDigitalWidgetView {

    void renderDataProducts(List<Product> products);

    void renderEmptyProduct(String message);

    void renderOperators(List<Operator> operatorModels, boolean b);

    void renderEmptyOperators(String message);

    void renderProduct(Product product);

    void renderOperator(Operator rechargeOperatorModel);

    void renderErrorProduct(String message);

    void renderDefaultError();

    void renderLastTypedClientNumber(String lastClientNumberTyped);

}
