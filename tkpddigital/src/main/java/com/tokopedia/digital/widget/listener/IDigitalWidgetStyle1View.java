package com.tokopedia.digital.widget.listener;

import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/8/17.
 */

public interface IDigitalWidgetStyle1View {

    void renderDataProducts(List<Product> products);

    void renderEmptyProduct(String message);

    void renderDataOperator(Operator operatorModel);

    void renderEmptyOperator(String message);

    void renderProduct(Product product);

    void renderDataRecent(List<String> results);

    void renderOperator(Operator operatorModel);

    void renderErrorProduct(String message);

    void renderDefaultError();

}
