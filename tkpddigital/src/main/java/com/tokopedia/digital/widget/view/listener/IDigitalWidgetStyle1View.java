package com.tokopedia.digital.widget.view.listener;

import com.tokopedia.digital.widget.view.model.operator.Operator;
import com.tokopedia.digital.widget.view.model.product.Product;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/8/17.
 */
@Deprecated
public interface IDigitalWidgetStyle1View extends BaseDigitalWidgetView {

    void renderDataProducts(List<Product> products, boolean showPrice);

    void renderEmptyProduct(String message);

    void renderDataOperator(Operator operatorModel);

    void renderEmptyOperator(String message);

    void renderProduct(Product product);

    void renderOperator(Operator operatorModel);

    void renderLastTypedClientNumber(String lastClientNumberTyped);

    void renderErrorProduct(String message);

    void renderDefaultError();

    void renderVerifiedNumber(String verifiedNumber);

}
