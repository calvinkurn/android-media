package com.tokopedia.digital.tokocash;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.digital.common.domain.interactor.GetDigitalCategoryByIdUseCase;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 2/28/18.
 */

public class TopupTokoCashPresenter extends BaseDaggerPresenter<TopupTokoCashContract.View>
        implements TopupTokoCashContract.Presenter {

    private GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase;

    @Inject
    public TopupTokoCashPresenter(GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase) {
        this.getDigitalCategoryByIdUseCase = getDigitalCategoryByIdUseCase;
    }

    @Override
    public void processGetDataProductTokoCash() {
        RequestParams requestParams = getDigitalCategoryByIdUseCase.createRequestParam("103");
        getDigitalCategoryByIdUseCase.execute(requestParams, new Subscriber<ProductDigitalData>() {
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
                .action(DigitalCheckoutPassData.Companion.getDEFAULT_ACTION())
                .categoryId(preCheckoutProduct.getCategoryId())
                .clientNumber("")
                .instantCheckout(preCheckoutProduct.isInstantCheckout() ? "1" : "0")
                .isPromo(preCheckoutProduct.isPromo() ? "1" : "0")
                .operatorId(preCheckoutProduct.getOperatorId())
                .productId(preCheckoutProduct.getProductId())
                .utmCampaign((preCheckoutProduct.getCategoryName()))
                .utmContent(getView().getVersionInfoApplication())
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource(DigitalCheckoutPassData.Companion.getUTM_SOURCE_ANDROID())
                .utmMedium(DigitalCheckoutPassData.Companion.getUTM_MEDIUM_WIDGET())
                .voucherCodeCopied(preCheckoutProduct.getVoucherCodeCopied())
                .source(DigitalCheckoutPassData.Companion.getPARAM_NATIVE())
                .build();

        if (getView().getMainApplication() instanceof DigitalRouter) {
            DigitalRouter digitalModuleRouter =
                    (DigitalRouter) getView().getMainApplication();
            getView().navigateToActivityRequest(
                    digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                    DigitalRouter.Companion.getREQUEST_CODE_CART_DIGITAL()
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
        if (getDigitalCategoryByIdUseCase != null) getDigitalCategoryByIdUseCase.unsubscribe();
    }
}
