package com.tokopedia.digital.widget.listener;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.product.Product;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/8/17.
 */

public interface IDigitalWidgetStyle2View {

    void renderDataProducts(List<Product> products);

    void renderEmptyProduct(String message);

    void renderOperators(List<RechargeOperatorModel> operatorModels);

    void renderEmptyOperators(String message);

    void renderDataRecent(List<String> results);

    void renderProduct(Product product);

    void renderOperator(RechargeOperatorModel rechargeOperatorModel);

    void renderErrorMessage(String message);
}
