package com.tokopedia.tkpd.home.recharge.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.status.Status;

import java.util.Map;

/**
 * @author ricoharisin on 7/4/16.
 */
public interface RechargeNetworkInteractor {

    void getStatus(@NonNull OnGetStatusListener listener);

    void getAllCategory(@NonNull OnGetCategoryListener listener);

    void getAllOperator(@NonNull OnGetOperatorListener listener);

    void getAllProduct(@NonNull OnGetProductListener listener);

    @SuppressWarnings("unused")
    void getOperator(int categoryId);

    void getProduct(int productId);

    void getRecentNumbers(Map<String,String> params, OnGetRecentNumbersListener listener);

    void getLastOrder(Map<String, String>params, OnGetRecentOrderListener listener);



    interface OnGetCategoryListener {
        void onSuccess(CategoryData data);

        void onNetworkError();
    }

    interface OnGetOperatorListener {
        void onSuccess(OperatorData data);

        void onNetworkError();
    }

    interface OnGetProductListener {
        void onSuccess(ProductData data);

        void onNetworkError();
    }

    interface OnGetStatusListener {
        void onSuccess(Status data);
        void onNetworkError();
    }

    interface OnGetRecentNumbersListener {
        void onGetRecentNumbersSuccess(RecentData recentNumber);
        void onNetworkError();
    }

    interface OnGetRecentOrderListener {
        void onGetLastOrderSuccess(LastOrder lastOrder);
        void onNetworkError();
    }
}
