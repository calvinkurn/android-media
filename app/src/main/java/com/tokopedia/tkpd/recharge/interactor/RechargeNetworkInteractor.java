package com.tokopedia.tkpd.recharge.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.tkpd.recharge.model.category.CategoryData;
import com.tokopedia.tkpd.recharge.model.operator.OperatorData;
import com.tokopedia.tkpd.recharge.model.product.ProductData;
import com.tokopedia.tkpd.recharge.model.recentNumber.RecentData;
import com.tokopedia.tkpd.recharge.model.recentOrder.LastOrder;
import com.tokopedia.tkpd.recharge.model.status.Status;

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
