package com.tokopedia.digital.tokocash.presenter;

import android.util.Log;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.model.ProductDigitalData;
import com.tokopedia.digital.tokocash.listener.TopUpTokoCashListener;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/21/17.
 */

public class TopUpTokocashPresenter  implements ITopUpTokocashPresenter {

    private final static String TOPUP_CATEGORY_ID = "103";
    private final static String PARAM_IS_RESELLER = "is_reseller";
    private final static String VALUE_RESSELER = "1";
    private final static String CATEGORY_ID = "category_id";

    private final IProductDigitalInteractor productDigitalInteractor;
    private final TopUpTokoCashListener view;

    public TopUpTokocashPresenter(IProductDigitalInteractor productDigitalInteractor,
                                  TopUpTokoCashListener view) {
        this.productDigitalInteractor = productDigitalInteractor;
        this.view = view;
    }

    @Override
    public void processGetCategoryTopUp() {
        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();
        if (GlobalConfig.isSellerApp()) {
            paramQueryCategory.put(PARAM_IS_RESELLER, VALUE_RESSELER);
        }
        TKPDMapParam<String, String> paramQueryBanner = new TKPDMapParam<>();
        paramQueryBanner.put(CATEGORY_ID, TOPUP_CATEGORY_ID);

        productDigitalInteractor.getCategoryAndBanner(
                TOPUP_CATEGORY_ID,
                view.getGeneratedAuthParamNetwork(paramQueryCategory),
                view.getGeneratedAuthParamNetwork(paramQueryBanner),
                view.getGeneratedAuthParamNetwork(new TKPDMapParam<String, String>()),
                view.getGeneratedAuthParamNetwork(new TKPDMapParam<String, String>()),
                getSubscriberProductDigitalData()

        );
    }

    private Subscriber<ProductDigitalData> getSubscriberProductDigitalData() {
        return new Subscriber<ProductDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("TAG", "onError: " + e.getMessage());
            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
                view.renderTopUpDataTokoCash(productDigitalData.getCategoryData());
            }
        };
    }

    @Override
    public void processAddToCartProduct(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct) {
        DigitalCheckoutPassData digitalCheckoutPassData =  new DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                .categoryId(preCheckoutProduct.getCategoryId())
                .clientNumber("")
                .instantCheckout(preCheckoutProduct.isInstantCheckout() ? "1" : "0")
                .isPromo(preCheckoutProduct.isPromo() ? "1" : "0")
                .operatorId(preCheckoutProduct.getOperatorId())
                .productId(preCheckoutProduct.getProductId())
                .utmCampaign((preCheckoutProduct.getCategoryName()))
                .utmContent(view.getVersionInfoApplication())
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                .voucherCodeCopied(preCheckoutProduct.getVoucherCodeCopied())
                .build();

        if (view.getMainApplication() instanceof IDigitalModuleRouter) {
            IDigitalModuleRouter digitalModuleRouter =
                    (IDigitalModuleRouter) view.getMainApplication();
            view.navigateToActivityRequest(
                    digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
            );
        }
    }

    private String generateATokenRechargeCheckout() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return view.getUserLoginId() + "_" + (token.isEmpty() ? timeMillis : token);
    }

    @Override
    public void processGetBalanceTokoCash() {

    }
}
