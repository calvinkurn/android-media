package com.tokopedia.digital.product.listener;

import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;

import java.util.List;

/**
 * Created by Rizky on 12/21/17.
 */

public interface IProductChooserView {

    void showProducts(List<Product> operators);

    void showInitialProgressLoading();

    void hideInitialProgressLoading();

}
