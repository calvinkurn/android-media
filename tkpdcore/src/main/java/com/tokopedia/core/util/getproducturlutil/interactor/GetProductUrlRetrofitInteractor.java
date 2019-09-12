package com.tokopedia.core.util.getproducturlutil.interactor;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.core.shopinfo.models.productmodel.ShopProduct;

import java.util.Map;

/**
 * Created by Nisie on 6/2/16.
 */
public interface GetProductUrlRetrofitInteractor {

    void getProductUrl(@NonNull Context context,
                    @NonNull Map<String, String> params,
                    @NonNull GetProductUrlListener listener);

    interface GetProductUrlListener {
        void onSuccess(ShopProduct shopProduct);

        void onTimeout();

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String error);

        void onNullData();

        void onNoConnection();
    }

    void unSubscribeObservable();

    boolean isRequesting();

    void setRequesting(boolean isRequesting);

}
