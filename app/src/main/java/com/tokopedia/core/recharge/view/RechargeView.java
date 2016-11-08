package com.tokopedia.core.recharge.view;
import com.tokopedia.core.recharge.model.product.Product;

import java.util.List;

/**
 * @author Kulomady 05 on 7/13/2016.
 */
public interface RechargeView {
    
    void showProgressFetchingData();

    void renderDataProducts(List<Product> products);

    void renderDataProductsEmpty(String message);

    void showImageOperator(String image);

    void hideProgressFetchData();

    void hideFormAndImageOperator();

    void showFormAndImageOperator();

    void renderDataRecent(List<String> results);
}
