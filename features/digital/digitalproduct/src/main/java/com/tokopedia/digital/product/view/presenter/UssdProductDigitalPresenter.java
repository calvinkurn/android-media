package com.tokopedia.digital.product.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.digital.product.view.listener.IUssdDigitalView;
import com.tokopedia.digital.product.view.model.PulsaBalance;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.network.utils.AuthUtil;

/**
 * Created by ashwanityagi on 19/07/17.
 */

public class UssdProductDigitalPresenter implements IUssdProductDigitalPresenter {
    private IUssdDigitalView view;
    private static final String DIGITAL_USSD_MOBILE_NUMBER = "DIGITAL_USSD_MOBILE_NUMBER";
    private static final String KEY_USSD_SIM1 = "KEY_USSD_SIM1";
    private static final String KEY_USSD_SIM2 = "KEY_USSD_SIM2";

    public UssdProductDigitalPresenter(IUssdDigitalView view) {
        this.view = view;

    }

    @Override
    public DigitalCheckoutPassData generateCheckoutPassData(
            Operator operator, PulsaBalance pulsaBalance, String categoryId, String categoryName,
            String productId, boolean isInstantCheckout) {
        return new DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.Companion.getDEFAULT_ACTION())
                .categoryId(categoryId)
                .clientNumber(pulsaBalance.getMobileNumber())
                .instantCheckout(isInstantCheckout ? "1" : "0")
                .isPromo("0")
                .operatorId(operator.getOperatorId())
                .productId(productId)
                .utmCampaign(categoryName)
                .utmContent(view.getVersionInfoApplication())
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource(DigitalCheckoutPassData.Companion.getUTM_SOURCE_ANDROID())
                .utmMedium(DigitalCheckoutPassData.Companion.getUTM_MEDIUM_WIDGET())
                .voucherCodeCopied("")
                .source(DigitalCheckoutPassData.Companion.getPARAM_NATIVE())
                .build();
    }

    @Override
    public void processAddToCartProduct(DigitalCheckoutPassData digitalCheckoutPassData) {
        if (view.isUserLoggedIn()) {
            if (view.getMainApplication() instanceof DigitalRouter) {
                DigitalRouter digitalModuleRouter =
                        (DigitalRouter) view.getMainApplication();
                view.navigateToActivityRequest(
                        digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                        DigitalRouter.Companion.getREQUEST_CODE_CART_DIGITAL()
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

    @Override
    public void storeUssdPhoneNumber(int selectedSim, String number) {
        number= DeviceUtil.formatPrefixClientNumber(number);
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(view.getActivity(), DIGITAL_USSD_MOBILE_NUMBER);
        if (selectedSim == 0) {
            localCacheHandler.putString(KEY_USSD_SIM1, number);
        } else if (selectedSim == 1) {
            localCacheHandler.putString(KEY_USSD_SIM2, number);
        }
        localCacheHandler.applyEditor();
    }

    @Override
    public  String getUssdPhoneNumberFromCache(int selectedSim) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(view.getActivity(), DIGITAL_USSD_MOBILE_NUMBER);
        if (selectedSim == 0) {
            return localCacheHandler.getString(KEY_USSD_SIM1,null);
        } else if (selectedSim == 1) {
            return localCacheHandler.getString(KEY_USSD_SIM2,null);
        }
        return null;
    }
}
