package com.tokopedia.tkpd.home.recharge.interactor;

import com.tokopedia.core.database.model.RechargeOperatorModel;
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
public interface RechargeInteractor {

    void getListProduct(OnGetListProduct onGetListProduct, String prefix, int categoryId, Boolean validatePrefix);

    void getListProductForOperator(OnGetListProductForOperator onGetListProductForOperator, int categoryId);

    void getListProductDefaultOperator(OnGetListProduct onGetListProduct, int categoryId, String operatorId);

    void getCategoryData(OnGetCategory onGetCategory);

    void getProductById(OnGetProductById listener, String categoryId, String operatorId, String productId);

    void getStatus(OnGetStatus onGetStatus);

    void storeRecentData(RecentData recentData);

    void getRecentData(int categoryId,OnGetRecentNumberListener listener);

    void getOperatorById(String operatorId,OnGetOperatorByIdListener listener);

    void getOperatorListByIds(List<Integer> operatorId,OnGetListOperatorByIdsListener listener);

    interface OnGetListProduct {
        void onSuccess(List<Product> listProduct);

        void onError(Throwable e);
    }

    interface OnGetListProductForOperator {
        void onSuccessFetchProducts(List<Product> listProduct);

        void onErrorFetchProdcuts(Throwable e);
    }

    interface OnGetCategory {
        void onSuccess(CategoryData categoryData);

        void onError(Throwable e);

        void onEmpty();
    }

    interface OnGetStatus {
        void onSuccess(Status status);

        void onError(Throwable e);

        void onEmpty();
    }

    interface OnGetOperatorByIdListener {
        void onSuccess(RechargeOperatorModel operator);

        void onError(Throwable e);

        void onEmpty();
    }

    interface OnGetListOperatorByIdsListener {
        void onSuccessFetchOperators(List<RechargeOperatorModel> operators);

        void onError(Throwable e);

        void onEmpty();
    }

    interface OnGetRecentNumberListener {
        void onGetRecentNumberSuccess(List<String> stringList);
        void onError(Throwable throwable);
        void onEmpty();
    }

    interface OnGetProductById {
        void onSuccessFetchProductById(Product product);

        void onError(Throwable e);
    }

}
