package com.tokopedia.digital.product.view.listener;

import com.tokopedia.digital.widget.view.model.product.Product;

import java.util.List;

/**
 * Created by Rizky on 12/21/17.
 */

public interface IProductChooserView {

    void showProducts(List<Product> operators);

    void showInitialProgressLoading();

    void hideInitialProgressLoading();

}
