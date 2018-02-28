package com.tokopedia.digital.tokocash.newpackage;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.common.domain.interactor.GetCategoryByIdUseCase;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 2/28/18.
 */

public class TopupTokoCashPresenter extends BaseDaggerPresenter<TopupTokoCashContract.View>
        implements TopupTokoCashContract.Presenter {

    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    public TopupTokoCashPresenter(GetCategoryByIdUseCase getCategoryByIdUseCase) {
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    }

    @Override
    public void processGetDataProductTokoCash() {
        RequestParams requestParams = getCategoryByIdUseCase.createRequestParam("103");
        getCategoryByIdUseCase.execute(requestParams, new Subscriber<ProductDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showErrorLoadProductTokoCash(e);
            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
                getView().renderProductTokoCash(productDigitalData.getCategoryData());
            }
        });
    }

    @Override
    public void processAddToCartProduct(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct) {
        DigitalCheckoutPassData digitalCheckoutPassData = new DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                .categoryId(preCheckoutProduct.getCategoryId())
                .clientNumber("")
                .instantCheckout(preCheckoutProduct.isInstantCheckout() ? "1" : "0")
                .isPromo(preCheckoutProduct.isPromo() ? "1" : "0")
                .operatorId(preCheckoutProduct.getOperatorId())
                .productId(preCheckoutProduct.getProductId())
                .utmCampaign((preCheckoutProduct.getCategoryName()))
                .utmContent(getView().getVersionInfoApplication())
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                .voucherCodeCopied(preCheckoutProduct.getVoucherCodeCopied())
                .build();

        if (getView().getMainApplication() instanceof IDigitalModuleRouter) {
            IDigitalModuleRouter digitalModuleRouter =
                    (IDigitalModuleRouter) getView().getMainApplication();
            getView().navigateToActivityRequest(
                    digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
            );
        }
    }

    private String generateATokenRechargeCheckout() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return getView().getUserLoginId() + "_" + (token.isEmpty() ? timeMillis : token);
    }

    @Override
    public void destroyView() {
        if (getCategoryByIdUseCase != null) getCategoryByIdUseCase.unsubscribe();
    }
}
