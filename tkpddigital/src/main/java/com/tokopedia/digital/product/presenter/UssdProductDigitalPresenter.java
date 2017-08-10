package com.tokopedia.digital.product.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.product.listener.IUssdDigitalView;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.PulsaBalance;

/**
 * Created by ashwanityagi on 19/07/17.
 */

public class UssdProductDigitalPresenter implements IUssdProductDigitalPresenter {
    private IUssdDigitalView view;

    public UssdProductDigitalPresenter(IUssdDigitalView view) {
        this.view = view;

    }

    @Override
    public DigitalCheckoutPassData generateCheckoutPassData(
            Operator operator, PulsaBalance pulsaBalance, String categoryId,String categoryName,
            String productId,boolean isInstantCheckout) {
        return new DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                .categoryId(categoryId)
                .clientNumber(pulsaBalance.getMobileNumber())
                .instantCheckout(isInstantCheckout ? "1" : "0")
                .isPromo("0")
                .operatorId(operator.getOperatorId())
                .productId(productId)
                .utmCampaign(categoryName)
                .utmContent(view.getVersionInfoApplication())
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                .voucherCodeCopied("")
                .build();
    }

    @Override
    public void processAddToCartProduct(DigitalCheckoutPassData digitalCheckoutPassData) {
        if (view.isUserLoggedIn()) {
            if (view.getMainApplication() instanceof IDigitalModuleRouter) {
                IDigitalModuleRouter digitalModuleRouter =
                        (IDigitalModuleRouter) view.getMainApplication();
                view.navigateToActivityRequest(
                        digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                        IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
                );
            }
        } else {
            view.interruptUserNeedLoginOnCheckout(digitalCheckoutPassData);
        }
    }


    @NonNull
    private String generateATokenRechargeCheckout() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return view.getUserLoginId() + "_" + (token.isEmpty() ? timeMillis : token);
    }
}
