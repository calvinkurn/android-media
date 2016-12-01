package com.tokopedia.tkpd.home.recharge.interactor;

import com.tokopedia.core.database.model.RechargeOperatorModelDBAttrs;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.Category;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.status.Status;

import java.util.List;

/**
 * @author ricoharisin on 7/18/16.
 */
public interface RechargeDBInteractor {

    void getListProduct(OnGetListProduct onGetListProduct, String prefix, int categoryId, Boolean validatePrefix);

    void getListProductDefaultOperator(OnGetListProduct onGetListProduct, int categoryId, String operatorId);

    void getCategory(OnGetCategory onGetCategory);

    void getStatus(OnGetStatus onGetStatus);

    void storeCategoryData(CategoryData categoryData);

    void storeOperatorData(OperatorData operatorData);

    void storeProductData(ProductData productData);

    void storeRecentData(RecentData recentData);

    void storeStatus(Status status);

    void getRecentData(int categoryId,OnGetRecentNumberListener listener);

    void getOperatorById(String operatorId,OnGetOperatorByIdListener listener);

    interface OnGetListProduct {
        void onSuccess(List<Product> listProduct);

        void onError(Throwable e);
    }

    interface OnGetCategory {
        void onSuccess(List<Category> listCategory);

        void onError(Throwable e);

        void onEmpty();
    }

    interface OnGetStatus {
        void onSuccess(Status status);

        void onError(Throwable e);

        void onEmpty();
    }

    interface OnGetOperatorByIdListener {
        void onSuccess(RechargeOperatorModelDBAttrs operator);

        void onError(Throwable e);

        void onEmpty();
    }

    interface OnGetRecentNumberListener {
        void onGetRecentNumberSuccess(List<String> stringList);
        void onError(Throwable throwable);
        void onEmpty();
    }



}
