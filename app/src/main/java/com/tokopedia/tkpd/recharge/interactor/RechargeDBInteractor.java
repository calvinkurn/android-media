package com.tokopedia.tkpd.recharge.interactor;

import com.tokopedia.tkpd.database.model.RechargeOperatorModelDB;
import com.tokopedia.tkpd.recharge.model.category.Category;
import com.tokopedia.tkpd.recharge.model.category.CategoryData;
import com.tokopedia.tkpd.recharge.model.operator.OperatorData;
import com.tokopedia.tkpd.recharge.model.product.Product;
import com.tokopedia.tkpd.recharge.model.product.ProductData;
import com.tokopedia.tkpd.recharge.model.recentNumber.RecentData;
import com.tokopedia.tkpd.recharge.model.status.Status;

import java.util.List;

/**
 * @author ricoharisin on 7/18/16.
 */
public interface RechargeDBInteractor {

    void getListProduct(OnGetListProduct onGetListProduct, String prefix, int categoryId, Boolean validatePrefix);

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
        void onSuccess(RechargeOperatorModelDB operator);

        void onError(Throwable e);

        void onEmpty();
    }

    interface OnGetRecentNumberListener {
        void onGetRecentNumberSuccess(List<String> stringList);
        void onError(Throwable throwable);
        void onEmpty();
    }



}
