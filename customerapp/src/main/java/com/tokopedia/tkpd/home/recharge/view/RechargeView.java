package com.tokopedia.tkpd.home.recharge.view;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.product.Product;

import java.util.List;

/**
 * @author Kulomady 05 on 7/13/2016.
 */
public interface RechargeView {

    void showProgressFetchingData();

    void renderDataProducts(List<Product> products);

    void renderDataOperators(List<RechargeOperatorModel> operators);

    void renderDataProductsEmpty(String message);

    void showImageOperator(String image);

    void setOperatorView(RechargeOperatorModel operator);

    void showProductById(Product product);

    void hideProgressFetchData();

    void hideFormAndImageOperator();

    void showFormAndImageOperator();

    void renderDataRecent(List<String> results);
}