package com.tokopedia.tkpd.thankyou.data.mapper;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.analytics.data.purchase.PurchaseTrackingData;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.DigitalTrackerData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.GraphqlResponse;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.PaymentGraphql;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentData;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/7/17.
 */

public class MarketplaceTrackerMapper implements Func1<Response<GraphqlResponse<PaymentGraphql>>, Boolean> {
    private GlobalCacheManager globalCacheManager;
    private Gson gson;

    public MarketplaceTrackerMapper(Gson gson) {
        this.globalCacheManager = new GlobalCacheManager();
        this.gson = gson;
    }

    @Override
    public Boolean call(Response<GraphqlResponse<PaymentGraphql>> graphqlResponse) {
        if(graphqlResponse != null
                && graphqlResponse.body() != null
                && graphqlResponse.body().getData() != null
                && graphqlResponse.body().getData().getPayment() != null) {
            List<CartItem> cartItemList = gson.fromJson(
                    globalCacheManager.getValueString(TkpdCache.Key.CART_CACHE_TRACKER),
                    new TypeToken<List<CartItem>>() {
                    }.getType()
            );

            // parse tracking data here
            PurchaseTrackingData purchaseTrackingData = getTrackingData(graphqlResponse.body().getData().getPayment());
            // here

            String rawTrackingData = gson.toJson(purchaseTrackingData);
            Map<String, Object> trackingPayload = gson.fromJson(rawTrackingData, LinkedTreeMap.class);
            PurchaseTracking.marketplace("transaction", trackingPayload);
            return true;
        }

        return false;
    }

    private PurchaseTrackingData getTrackingData(PaymentData data) {
        PurchaseTrackingData trackingData = new PurchaseTrackingData();
        return null;
    }


}
