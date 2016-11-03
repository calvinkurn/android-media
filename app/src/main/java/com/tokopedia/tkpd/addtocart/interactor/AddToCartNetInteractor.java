package com.tokopedia.tkpd.addtocart.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tkpd.addtocart.model.OrderData;
import com.tokopedia.tkpd.addtocart.model.responseaddress.AddressData;
import com.tokopedia.tkpd.addtocart.model.responseatcform.AtcFormData;
import com.tokopedia.tkpd.addtocart.model.responseatcform.Shipment;

import java.util.List;
import java.util.Map;

import rx.Scheduler;

/**
 * Created by Angga.Prasetiyo on 11/03/2016.
 */
public interface AddToCartNetInteractor {

    void getAddToCartForm(@NonNull Context context,
                          @NonNull Map<String, String> param,
                          @NonNull OnGetCartFormListener listener);

    void getAddressShipping(@NonNull Context context,
                            @NonNull Map<String, String> param,
                            @NonNull OnGetCartAddressListener listener);

    void calculateCartPrice(@NonNull Context context,
                            @NonNull Map<String, String> param,
                            @NonNull OnCalculateProduct listener);

    void calculateCartShipping(@NonNull Context context,
                               @NonNull Map<String, String> param,
                               @NonNull OnCalculateShipping listener);

    void calculateCartAddressShipping(@NonNull Context context,
                                      @NonNull Map<String, String> param,
                                      @NonNull OnCalculateAddressShipping listener);

    void updateAddress(@NonNull Context context,
                       @NonNull Map<String, String> params,
                       @NonNull OnUpdateAddressListener listener);

    void addToCart(@NonNull Context context,
                   @Nullable Scheduler schedulerSubscribe,
                   @NonNull Map<String, String> params,
                   @NonNull OnAddToCart listener);

    void unSubscribeObservable();

    interface OnGetCartFormListener {
        void onSuccess(AtcFormData data);

        void onFailure();
    }

    interface OnUpdateAddressListener {
        void onSuccess();

        void onFailure(String failedMessage);

        void onError();
    }

    interface OnGetCartAddressListener {
        void onSuccess(AddressData addressData);

        void onFailure();
    }

    interface OnCalculateProduct {
        void onSuccess(String price);

        void onFailure();

        void onShowErrorMessage(String errorMessage);
    }

    interface OnCalculateShipping {
        void onSuccess(List<Shipment> datas);

        void onFailure();
    }

    interface OnCalculateAddressShipping {
        void onSuccess(List<Shipment> datas);

        void onFailure();
    }

    interface OnUpdateAddressShippingCart {
        void onSuccess();

        void onFailure();
    }

    interface OnAddToCart {
        void onSuccess(String message);

        void onFailure(String message);

        void onTimeout();

        void onNoConnection();
    }
}
