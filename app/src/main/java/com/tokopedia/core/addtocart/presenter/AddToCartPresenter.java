package com.tokopedia.core.addtocart.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tokopedia.core.addtocart.model.OrderData;
import com.tokopedia.core.addtocart.model.ProductCartPass;
import com.tokopedia.core.addtocart.model.responseatcform.AtcFormData;
import com.tokopedia.core.addtocart.model.responseatcform.Destination;
import com.tokopedia.core.addtocart.receiver.ATCResultReceiver;

/**
 * Created by Angga.Prasetiyo on 11/03/2016.
 * Edited by Hafizh :: Kero Rates
 */
public interface AddToCartPresenter {

    void getCartFormData(@NonNull Context context, @NonNull ProductCartPass data);

    void calculateProduct(@NonNull Context context, @NonNull OrderData orderData);

    void calculateKeroRates(@NonNull Context context, @NonNull AtcFormData atcFormData);

    void calculateKeroAddressShipping(@NonNull Context context, @NonNull OrderData orderData);

    Destination generateAddressData(Intent data);

    void calculateAllPrices(@NonNull Context context, @NonNull OrderData orderData);

    void processChooseGeoLocation(@NonNull Context context, @NonNull OrderData orderData);

    void updateAddressShipping(@NonNull Context context, @NonNull OrderData orderData);

    @Deprecated
    @SuppressWarnings("unused")
    void addToCart(@NonNull Context context, @NonNull OrderData orderData);

    boolean isValidOrder(@NonNull Context context, @NonNull OrderData orderData);

    void processAddToCartSuccess(@NonNull Context context, String message);

    void processAddToCartFailure(@NonNull Context context, String string);

    void sendAnalyticsATCSuccess(@NonNull Context context, @NonNull ProductCartPass productCartPass,
                                 @NonNull OrderData orderData);

    void addToCartService(@NonNull Context context, @NonNull ATCResultReceiver atcReceiver,
                          @NonNull OrderData orderData);

    void setCacheCart(@NonNull Context context);

    void sendToGTM(@NonNull Context context);

    void sendAppsFlyerATC(@NonNull Context context, @NonNull OrderData orderData);

    void onViewDestroyed();

    void processGetGTMTicker();
}
