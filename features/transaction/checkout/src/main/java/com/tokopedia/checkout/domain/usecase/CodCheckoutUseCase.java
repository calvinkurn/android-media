package com.tokopedia.checkout.domain.usecase;

import android.content.Context;
import android.os.Build;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transactiondata.entity.request.CheckoutRequest;
import com.tokopedia.transactiondata.entity.request.CodCheckoutRequest;
import com.tokopedia.transactiondata.entity.response.cod.CodResponse;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by fajarnuha on 19/12/18.
 */
public class CodCheckoutUseCase extends GraphqlUseCase {

    private static final String PARAM_CHECKOUT_PARAMS = "checkoutParams";

    private String mFingerPrintSupport;
    private String mFingerPrintPublicKey;

    private Context context;
    private ICheckoutModuleRouter checkoutModuleRouter;

    @Inject
    public CodCheckoutUseCase(Context context,
                              ICheckoutModuleRouter checkoutModuleRouter) {
        this.context = context;
        this.checkoutModuleRouter = checkoutModuleRouter;
    }

    public GraphqlRequest getRequest(CheckoutRequest carts, boolean isOneClickShipment) {
        setFingerPrintParams();
        CodCheckoutRequest request = new CodCheckoutRequest(null, null,
                mFingerPrintSupport, mFingerPrintPublicKey, carts, 0,
                String.valueOf(isOneClickShipment));
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.checkout_cod_query);
        return new GraphqlRequest(query, CodResponse.class, getParam(request), false);
    }

    private Map<String, Object> getParam(Object json) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_CHECKOUT_PARAMS, json);
        return params;
    }

    private void setFingerPrintParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkoutModuleRouter != null
                && checkoutModuleRouter.checkoutModuleRouterGetEnableFingerprintPayment()) {
            PublicKey publicKey = checkoutModuleRouter.checkoutModuleRouterGeneratePublicKey();
            if (publicKey != null) {
                mFingerPrintPublicKey = checkoutModuleRouter.checkoutModuleRouterGetPublicKey(publicKey);
                mFingerPrintSupport = String.valueOf(true);
            } else {
                mFingerPrintSupport = String.valueOf(false);
            }
        } else {
            mFingerPrintSupport = String.valueOf(false);
        }
    }

}
