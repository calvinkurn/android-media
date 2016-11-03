package com.tokopedia.tkpd.shipping.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.shipping.model.kero.Attribute;
import com.tokopedia.tkpd.shipping.model.kero.Data;

import java.util.List;

/**
 * Created by Herdi_WORK on 20.09.16.
 */
public interface KeroNetInteractor {

    void calculateShipping(@NonNull Context context, @NonNull TKPDMapParam<String,String> params, @NonNull CalculationListener listener);

    void calculateKeroCartAddressShipping(@NonNull Context context,
                                      @NonNull TKPDMapParam<String, String> param,
                                      @NonNull OnCalculateKeroAddressShipping listener);

    void onViewDestroyed();

    interface CalculationListener{
        void onSuccess(Data rates);
        void onFailed(String error);
        void onTimeout(String timeoutError);
        void onNoConnection();
    }

    interface OnCalculateKeroAddressShipping {
        void onSuccess(List<Attribute> datas);

        void onFailure();
    }

}
